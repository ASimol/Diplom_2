package api;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import java.util.HashMap;

import static api.BaseHttp.getSpecification;
import static io.restassured.RestAssured.given;

public class Order {
    public final static String NEW_ORDER = "/api/orders";

    @Step("Создать заказ")
    public ValidatableResponse create(HashMap ingredient, String token) {
        return given()
                .headers("Authorization", token)
                .spec(getSpecification())
                .body(ingredient)
                .log().body()
                .when()
                .post(NEW_ORDER)
                .then()
                .log().body();
    }

    @Step("Получить заказы авторизованного пользователя")
    public ValidatableResponse getOrders(String accessUserToken) {
        return given()
                .header("Authorization", accessUserToken)
                .spec(getSpecification())
                .when()
                .get(NEW_ORDER)
                .then()
                .log().body();
    }

    @Step("Получить заказы не авторизованного пользователя")
    public ValidatableResponse getOrdersWithoutToken() {
        return given()
                .spec(getSpecification())
                .when()
                .get(NEW_ORDER)
                .then()
                .log().body();
    }
}

