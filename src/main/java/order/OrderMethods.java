package order;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import user.BaseSettings;

import static constants.Endpoints.ORDERS;
import static io.restassured.RestAssured.given;

public class OrderMethods extends BaseSettings {

    @Step("Создание заказа")
    public ValidatableResponse createOrder(String accessToken, Order order) {
        return given()
                .spec(getBaseUrlAndContentType())
                .auth().oauth2(accessToken)
                .body(order)
                .when()
                .post(ORDERS)
                .then();
    }

    @Step("Получние заказа")
    public ValidatableResponse getOrder(String accessToken) {
        return given()
                .spec(getBaseUrlAndContentType())
                .auth().oauth2(accessToken)
                .when()
                .get(ORDERS)
                .then();
    }
}
