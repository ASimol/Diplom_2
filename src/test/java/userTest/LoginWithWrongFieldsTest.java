package userTest;

import api.User;
import api.UserClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;

@RunWith(value = Parameterized.class)
public class LoginWithWrongFieldsTest {
    private UserClient userClient;
    private User body;
    private ValidatableResponse response;

    @Before
    public void createNewUser() {
        userClient = new UserClient();
    }

    @Parameterized.Parameters()
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][]{
                        {new User().setRandomEmail().setRandomName()},
                        {new User().setRandomPassword().setRandomEmail()},
                        {new User().setRandomPassword().setRandomName()},
                }
        );
    }

    public LoginWithWrongFieldsTest(User body) {
        this.body = body;
    }

    @Test
    @DisplayName("Логин пользователя с неверным логином и паролем")
    public void LoginUserWithoutMandatoryFields() {
        response = userClient.login(body);
        response.assertThat().statusCode(401);
        response.assertThat().extract().path("message").equals("email or password are incorrect");
    }
}

