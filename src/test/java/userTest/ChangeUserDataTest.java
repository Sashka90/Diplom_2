package userTest;

import factory.UserFactory;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;
import user.User;
import user.UserMethods;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ChangeUserDataTest {

    private User user;
    private UserMethods userMethods;
    private String userToken;
    private String accessToken;

    @Before
    public void setUp() {
        user = UserFactory.getRandomDefaultUser();
        userMethods = new UserMethods();
    }

    @Test
    @DisplayName("Изменение данных пользователя с авторизацией")
    @Description("Проверка кода 200 и статуса success true при успешном изменении данных пользователя с авторизацией")
    public void changeUserDataWithAuthorizationTest() {
        ValidatableResponse response = userMethods.create(user);
        accessToken = response.extract().path("accessToken");
        userToken = accessToken.substring(7);
        ValidatableResponse changeResponse = userMethods.change(UserFactory.getRandomDefaultUser(), userToken);
        int statusCode = changeResponse.extract().statusCode();
        boolean isChanged = changeResponse.extract().path("success");
        assertEquals(200, statusCode);
        assertTrue(isChanged);
    }

    @Test
    @DisplayName("Изменение данных пользователя без авторизации")
    @Description("Проверка кода 401 и корректного сообщения об ошибке при попытке изменения данных пользователя без авторизации")
    public void changeUserDataWithoutAuthorizationTest() {
        userToken = "";
        ValidatableResponse changeResponse = userMethods.change(UserFactory.getRandomDefaultUser(), userToken);
        int statusCode = changeResponse.extract().statusCode();
        String messageError = changeResponse.extract().path("message");
        String messageExpected = "You should be authorised";
        assertEquals(401, statusCode);
        assertEquals(messageExpected, messageError);
    }


}
