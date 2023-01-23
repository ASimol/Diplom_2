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

public class CreateUserTest {

    private UserClient userClient;
    private ValidatableResponse response;
    private String token;

    @Before
    public void setUp() {
        userClient = new UserClient();
    }
    @Test
    @DisplayName("Создание пользователя")
    public void createUserSuccessTest() {
        User user = User.getRandomUser();
        response = userClient.create(user);
        token = response.extract().path("accessToken");
        response.assertThat().log().all().statusCode(200);
        response.assertThat().extract().path("success").equals(true);
        response.assertThat().extract().path("accessToken").equals(is(not(null)));
        response.assertThat().extract().path("refreshToken").equals(is(not(null)));

    }

    @Test
    @DisplayName("Создание существующего пользователя")
    public void createExistsUserTest() {
        User user = User.getRandomUser();
        response = userClient.create(user);
        token = response.extract().path("accessToken");
        response = userClient.create(user);
        response.assertThat().extract().path("success").equals(false);
        response.assertThat().extract().path("message").equals("User already exists");
        response.assertThat().log().all().statusCode(403);
    }

    @After
    public void tearDown() {
        userClient.delete(token);
    }
}