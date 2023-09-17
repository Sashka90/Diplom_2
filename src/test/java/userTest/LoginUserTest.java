package userTest;

import factory.UserFactory;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.User;
import user.UserMethods;
import user.UserAuthData;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LoginUserTest {

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
    @DisplayName("Успешный логин пользователя")
    @Description("Проверка кода 200 и статуса success true при успешном логине")
    public void loginUserPositiveTest() {
        ValidatableResponse response = userMethods.create(user);
        ValidatableResponse loginResponse = userMethods.login(UserAuthData.from(user));
        accessToken = response.extract().path("accessToken");
        userToken = accessToken.substring(7);
        int statusCode = loginResponse.extract().statusCode();
        boolean isLogin = loginResponse.extract().path("success");
        assertEquals(200, statusCode);
        assertTrue(isLogin);
    }

    @Test
    @DisplayName("Неуспешный логин пользователя")
    @Description("Проверка кода 401 и корректного сообщения об ошибке при не успешном логине")
    public void loginUserNegativeTest() {
        ValidatableResponse response = userMethods.login(UserAuthData.from(UserFactory.getUserWithIncorrectLogin()));
        userToken = "";
        int statusCode = response.extract().statusCode();
        String messageError = response.extract().path("message");
        String messageExpected = "email or password are incorrect";
        assertEquals(401, statusCode);
        assertEquals(messageExpected, messageError);
    }

    @After
    public void tearDown() {
        userMethods.delete(userToken);
    }
}
