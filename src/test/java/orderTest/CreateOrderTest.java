package orderTest;

import api.Ingredients;
import api.Order;
import api.User;
import api.UserClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

public class CreateOrderTest {
    private Order order;
    private UserClient userClient;
    private Ingredients ingredients;

    private HashMap<String, List> ingredientsHash = new HashMap<>();
    private ValidatableResponse response;
    private User user;
    private String accessToken;

    @Before
    public void createNewUser() {
        order = new Order();
        userClient = new UserClient();
        ingredients = new Ingredients();
        user = User.getRandomUser();
        userClient.create(user);

        List<String> ingredients = new ArrayList<>();
        List<String> clearIngredients = Ingredients.getIngredients().extract().path("data._id");
        for (int i = 0; i <= 3; i++) {
            ingredients.add(clearIngredients.get(i));
        }
        ingredientsHash.put("ingredients", ingredients);
    }

    @Test
    @DisplayName("Заказ с авторизацией с ингридиентами")
    public void createOrderWithAuthTest() {
        accessToken = userClient.login(user).extract().path("accessToken");
        response = order.create(ingredientsHash, accessToken);
        response.assertThat().statusCode(200);
        response.assertThat().extract().path("success").equals(true);
        response.assertThat().extract().path("name").equals(is(not(null)));
        response.assertThat().extract().path("order.number").equals(is(not(null)));
    }

    @Test
    @DisplayName("Заказ без авторизации. Ошибка!")
    public void createOrderWithoutAuthTest() {
        response = order.create(ingredientsHash, "ERRROOORRrrr");

        response.assertThat().log().all().statusCode(200);
        response.assertThat().log().all().extract().path("success").equals(false);
        response.assertThat().log().all().extract().path("message").equals("Ingredient ids must be provided");
    }

    @Test
    @DisplayName("Заказ без ингредиентов. Ошибка!")
    public void createOrderWithoutIngredientsTest() {
        accessToken = userClient.login(user).extract().path("accessToken");

        response = order.create(new HashMap<>(), accessToken);

        response.assertThat().statusCode(400);
        response.assertThat().extract().path("success").equals(false);
        response.assertThat().extract().path("message").equals("Ingredient ids must be provided");
    }

    @Test
    @DisplayName("Заказ с неверным хешем ингредиентов. Ошибка!")
    public void createOrderInvalidHashTest() {
        accessToken = userClient.login(user).extract().path("accessToken");
        HashMap<String, List> ingredientsHash = new HashMap<>();
        ingredientsHash.put("ingredients", List.of("майонез", "чесночный"));
        response = order.create(ingredientsHash, accessToken);
        response.assertThat().statusCode(500);
        response.assertThat().extract().path("success").equals(false);
        response.assertThat().extract().path("message").equals("Ingredient ids must be provided");
    }

    @After
    public void tearDown() {
        userClient.delete(accessToken);
    }
}

