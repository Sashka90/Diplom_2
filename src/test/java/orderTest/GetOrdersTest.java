package orderTest;

import factory.OrderFactory;
import factory.UserFactory;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import order.OrderMethods;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.User;
import user.UserMethods;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GetOrdersTest {

    private OrderMethods orderMethods;
    private User user;
    private UserMethods userMethods;
    private String userToken;
    private String accessToken;

    @Before
    public void setUp() {
        orderMethods = new OrderMethods();
        user = UserFactory.getRandomDefaultUser();
        userMethods = new UserMethods();
    }

    @Test
    @DisplayName("Получение списка заказаов авторизованным пользователем")
    @Description("Проверка кода 200 и статуса success true при успешном получении списка заказаов авторизованой персононой")
    public void getOrdersAuthorizationUserTest() {
        ValidatableResponse responseUser = userMethods.create(user);
        accessToken = responseUser.extract().path("accessToken");
        userToken = accessToken.substring(7);
        orderMethods.createOrder(userToken, OrderFactory.orderWithValidIngredients());
        ValidatableResponse responseOrder = orderMethods.getOrder(userToken);
        int statusCode = responseOrder.extract().statusCode();
        boolean isGetting = responseOrder.extract().path("success");
        assertEquals(200, statusCode);
        assertTrue(isGetting);
    }

    @Test
    @DisplayName("Получение списка заказаов не авторизованным пользователем")
    @Description("Проверка кода 401 и сообщения об ошибке при получении списка не авторизованной персоной авторизованой персононой")
    public void getOrdersNotAuthorizationUserTest() {
        userToken = "";
        ValidatableResponse responseOrder = orderMethods.getOrder(userToken);
        int statusCode = responseOrder.extract().statusCode();
        String error = responseOrder.extract().path("message");
        assertEquals(401, statusCode);
        assertEquals("You should be authorised", error);
    }

    @After
    public void tearDown() {
        userMethods.delete(userToken);
    }


}
