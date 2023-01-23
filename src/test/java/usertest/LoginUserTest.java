package usertest;

import api.User;
import api.UserClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

public class LoginUserTest {
    private UserClient userClient;
    private User user;
    private ValidatableResponse response;
    private String accessToken;

    @Before
    public void createNewUser() {
        userClient = new UserClient();
        user = User.getRandomUser();
        userClient.create(user);
    }

    @Test
    @DisplayName("Проверка авторизации пользователя")
    public void loginUserSuccessTest() {
        response = userClient.login(user);
        accessToken = response.extract().path("accessToken");
        response.assertThat().statusCode(200);
        response.assertThat().extract().path("success").equals(true);
        response.assertThat().extract().path("accessToken").equals(is(not(null)));
        response.assertThat().extract().path("refreshToken").equals(is(not(null)));
        response.assertThat().extract().path("user").equals(is(not(null)));
    }

    @After
    public void tearDown() {
        userClient.delete(accessToken);
    }
}

