package userTest;

import api.User;
import api.UserClient;
import com.github.javafaker.Faker;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Locale;

public class UpdateUserDataTest {
    private UserClient userClient;
    private ValidatableResponse response;
    private User user;
    private String token;
    String newEmail;
    String newPassword;
    String newName;

    static Faker faker = new Faker(new Locale("en_EN"));

    @Before
    public void createNewUser() {
        userClient = new UserClient();
        user = User.getRandomUser();
        token = userClient.create(user).extract().path("accessToken");
    }

    @Test
    @DisplayName("Изменение почты авторизованного пользователя")
    public void updateEmailUserWithAuthTest() {

        newEmail = RandomStringUtils.randomAlphabetic(5) + "@mail.ru";
        response = userClient.updateUserInfo(token, "{\"email\": \"" + newEmail + "\"}");
        response.assertThat().statusCode(200);
        response.assertThat().extract().path("success").equals(true);
        HashMap<String, String> userInfo = response.extract().path("user");
        userInfo.get("email").equals(newEmail.toLowerCase());
        userInfo.get("name").equals(user.name.toLowerCase());
    }

    @Test
    @DisplayName("Изменение имени авторизованного пользователя")
    public void updateUserNameWithAuthTest() {

        newName = faker.name().username();
        response = userClient.updateUserInfo(token, "{\"name\": \"" + newName + "\"}");
        response.assertThat().statusCode(200);
        response.assertThat().extract().path("success").equals(true);
        HashMap<String, String> userInfo = response.extract().path("user");
        userInfo.get("email").equals(user.email.toLowerCase());
        userInfo.get("name").equals(newName.toLowerCase());
    }

    @Test
    @DisplayName("Изменение пароля авторизованного пользователя")
    public void updateUserPasswordWithAuthTest() {

        newPassword = RandomStringUtils.randomAlphabetic(8);
        response = userClient.updateUserInfo(token, "{\"password\": \"" + newPassword + "\"}");

        response.assertThat().statusCode(200);
        response.assertThat().extract().path("success").equals(true);

        User userNew = new User(user.email, newPassword, user.name);
        ValidatableResponse responseLoginNewPass = userClient.login(userNew);
        responseLoginNewPass.assertThat().statusCode(200);
        responseLoginNewPass.assertThat().extract().path("success").equals(true);
    }

    @After
    public void tearDown() {
        userClient.delete(token);
    }

}
