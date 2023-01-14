package api;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static api.BaseHttp.getSpecification;
import static io.restassured.RestAssured.given;

public class UserClient {
    public final static String AUTH_UPDATE = "/api/auth/user";
    public final static String AUTH_REGISTER = "/api/auth/register";
    public final static String AUTH_LOGIN = "/api/auth/login";
    @Step("Создание пользователя")
    public ValidatableResponse create(User user) {
        return given()
                .spec(getSpecification())
                .and()
                .body(user)
                .log().body()
                .when()
                .post(AUTH_REGISTER)
                .then()
                .log().body();
    }

    @Step("Логирование в систему под пользователем")
    public ValidatableResponse login(User user) {
        return given()
                .spec(getSpecification())
                .body(user)
                .log().body()
                .when()
                .post(AUTH_LOGIN)
                .then()
                .log().body();
    }

    @Step("Изменение пользователя")
    public ValidatableResponse updateUserInfo(String token, String body) {
        return given()
                .headers("Authorization", token)
                .spec(getSpecification())
                .body(body)
                .log().body()
                .when()
                .patch(AUTH_UPDATE)
                .then()
                .log().body();
    }

    @Step("Удаление пользователя")
    public void delete(String accessToken) {
        if (accessToken == null) {
            return;
        }
        given()
                .header("Authorization",accessToken)
                .spec(getSpecification())
                .when()
                .delete(AUTH_UPDATE)
                .then()
                .statusCode(202);
    }
}

