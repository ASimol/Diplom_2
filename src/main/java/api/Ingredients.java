package api;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static api.BaseHttp.getSpecification;
import static io.restassured.RestAssured.given;

public class Ingredients {

    private static final String INGREDIENTS_PATH = "api/ingredients";

    @Step("Получение списка ингредиентов")
    public static ValidatableResponse getIngredients() {
        return given()
                .spec(getSpecification())
                .log().uri()
                .when()
                .get(INGREDIENTS_PATH)
                .then();
    }
}
