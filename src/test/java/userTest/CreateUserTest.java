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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CreateUserTest {

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
    @DisplayName("Создание нового пользователя")
    @Description("Проверка кода 200 и статуса success true при успешном создании")
    public void createUserPositiveTest() {
        ValidatableResponse response = userMethods.create(user);
        int statusCode = response.extract().statusCode();
        boolean isCreated = response.extract().path("success");
        accessToken = response.extract().path("accessToken");
        userToken = accessToken.substring(7);
        assertEquals(200, statusCode);
        assertTrue(isCreated);
    }

    @Test
    @DisplayName("Создание нового пользователя без обязательного поля (email)")
    @Description("Проверка кода 403 и корректного сообщения об ошибке при попытке создания без обязательного поля")
    public void createUserWithoutEmailTest() {
        ValidatableResponse response = userMethods.create(UserFactory.getUserWithoutEmail());
        userToken = "";
        int statusCode = response.extract().statusCode();
        String messageError = response.extract().path("message");
        String messageExpected = "Email, password and name are required fields";
        assertEquals(403, statusCode);
        assertEquals(messageExpected, messageError);
    }

    @Test
    @DisplayName("Создание двух одинаковых пользователей")
    @Description("Проверка кода 403 и корректного сообщения об ошибке при попытке создания существующего пользователя")
    public void createIdenticalUserTest() {
        ValidatableResponse response1 = userMethods.create(user);
        accessToken = response1.extract().path("accessToken");
        userToken = accessToken.substring(7);
        ValidatableResponse response2 = userMethods.create(user);
        int statusCode = response2.extract().statusCode();
        String messageError = response2.extract().path("message");
        String messageExpected = "User already exists";
        assertEquals(403, statusCode);
        assertEquals(messageExpected, messageError);
    }

    @After
    public void tearDown() {
        userMethods.delete(userToken);
    }
}
