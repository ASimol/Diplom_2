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

public class CreateUserWithoutOnOfFields {
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

    public CreateUserWithoutOnOfFields(User body) {
        this.body = body;
    }

    @Test
    @DisplayName("Создание пользователя без обязательных полей невозможно")
    public void createUserWithoutMandatoryFieldsTest() {
        User user = body;
        response = userClient.create(user);
        response.assertThat().log().all().statusCode(403);
        response.assertThat().log().all().extract().path("message").equals("Email, password and name are required fields");
    }

}

