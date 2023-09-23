package user;

import factory.UserFactory;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

import static constants.Endpoints.*;
import static io.restassured.RestAssured.given;

public class UserMethods extends BaseSettings{

    private Response userData;
    private User user;

    public UserMethods() {
        this.user = UserFactory.getRandomDefaultUser();
    }

    @Step("Создание юзера")
    public ValidatableResponse create(User user) {
        return given()
                .spec(getBaseUrlAndContentType())
                .body(user)
                .when()
                .post(CREATE_USER)
                .then();
    }

    @Step("Удаление юзера")
    public ValidatableResponse delete(String accessToken) {
        return given()
                .spec(getBaseUrlAndContentType())
                .auth().oauth2(accessToken)
                .when()
                .delete(USER)
                .then();
    }

    @Step("Логин юзера")
    public ValidatableResponse login(UserAuthData credentials) {
        return given()
                .spec(getBaseUrlAndContentType())
                .body(credentials)
                .when()
                .post(LOGIN_USER)
                .then();
    }

    @Step("Изменение данных пользователя")
    public ValidatableResponse change(User user, String accessToken) {
        return given()
                .spec(getBaseUrlAndContentType())
                .auth().oauth2(accessToken)
                .body(user)
                .when()
                .patch(USER)
                .then();
    }
}
