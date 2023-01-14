package userTest;

import api.User;
import api.UserClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
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
    public void createNewUser() {
        userClient = new UserClient();
        user = User.getRandomUser();
        userClient.create(user);
    }

    @Test
    @DisplayName("Изменение пользователя без авторизации. Ошибка!")
    public void changeUserEmailWithoutAuthTest() {
        token = RandomStringUtils.randomAlphabetic(171);

        newEmail = RandomStringUtils.randomAlphabetic(7) + "@mail.ru";
        response = userClient.updateUserInfo(token, "{\"email\": \"" + newEmail + "\"}");

        response.assertThat().statusCode(401);
        response.assertThat().extract().path("success").equals(false);
        response.assertThat().extract().path("message").equals("You should be authorised");
    }

    @After
    public void tearDown() {
        userClient.delete(token);
    }
}
