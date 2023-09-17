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

import static org.junit.Assert.*;

public class CreateOrderTest {

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
    @DisplayName("Создание заказа с авторизацией пользователя")
    @Description("Проверка кода 200, статуса success true и номера заказа !=0 при успешном создании заказа")
    public void createOrderWithAuthorization() {
        ValidatableResponse responseUser = userMethods.create(user);
        accessToken = responseUser.extract().path("accessToken");
        userToken = accessToken.substring(7);
        ValidatableResponse responseOrder = orderMethods.createOrder(userToken, OrderFactory.orderWithValidIngredients());
        int statusCode = responseOrder.extract().statusCode();
        boolean isCreated = responseOrder.extract().path("success");
        int number = responseOrder.extract().path("order.number");
        assertEquals(200, statusCode);
        assertTrue(isCreated);
        assertNotEquals(0, number);
    }

    @Test
    @DisplayName("Создание заказа без авторизациии пользователя")
    @Description("Проверка кода 401 при создании заказа без авторизации")
    public void createOrderWithoutAuthorization() {
        userToken = "";
        ValidatableResponse response = orderMethods.createOrder(userToken, OrderFactory.orderWithValidIngredients());
        int statusCode = response.extract().statusCode();
        assertEquals(401, statusCode); //тут баг
    }

    @Test
    @DisplayName("Создание заказа без ингридиентов")
    @Description("Проверка кода 400 и корректного сообщения об ошибке при создании заказа без ингридиентов")
    public void createOrderWithoutIngredients() {
        ValidatableResponse responseUser = userMethods.create(user);
        accessToken = responseUser.extract().path("accessToken");
        userToken = accessToken.substring(7);
        ValidatableResponse responseOrder = orderMethods.createOrder(userToken, OrderFactory.orderWithoutIngredients());
        int statusCode = responseOrder.extract().statusCode();
        String error = responseOrder.extract().path("message");
        assertEquals(400, statusCode);
        assertEquals("Ingredient ids must be provided", error);
    }

    @Test
    @DisplayName("Создание заказа с неверным хешем ингредиентов")
    @Description("Проверка кода 400 и корректного сообщения об ошибке при создании заказа с неверным хешем ингредиентов")
    public void createOrderWithInvalidIngredientsHash() {
        ValidatableResponse responseUser = userMethods.create(user);
        accessToken = responseUser.extract().path("accessToken");
        userToken = accessToken.substring(7);
        ValidatableResponse responseOrder = orderMethods.createOrder(userToken, OrderFactory.orderWithInvalidIngredients());
        int statusCode = responseOrder.extract().statusCode();
        String error = responseOrder.extract().path("message");
        assertEquals(400, statusCode);
        assertEquals("One or more ids provided are incorrect", error);
    }

    @After
    public void tearDown() {
        userMethods.delete(userToken);
    }
}
