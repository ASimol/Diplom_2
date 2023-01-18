package usertest;

import api.User;
import api.UserClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class UpdateUserDataWithoutAuthTest {
    private UserClient userClient;
    private ValidatableResponse response;
    private User user;
    private String token;
    String newEmail;

    @Before
    public void setUp() {
        userClient = new UserClient();
        user = User.getRandomUser();
        userClient.create(user);
    }

    @Test
    @DisplayName("Изменение пользователя. Пользователь с такой почтой уже существует!")
    public void changeUserEmailWithoutAuthTest() {
        User userClientTwo = User.getRandomUser();
        userClient.create(userClientTwo);
        newEmail = userClientTwo.email;
        token = userClient.login(user).extract().path("accessToken");
        response = userClient.updateUserInfo(token, "{\"email\": \"" + newEmail + "\"}");

        response.assertThat().statusCode(403);
        response.assertThat().extract().path("success").equals(false);
        response.assertThat().extract().path("message").equals("User with such email already exists");

    }
    @After
    public void tearDown() {
        userClient.delete(token);
    }
}
