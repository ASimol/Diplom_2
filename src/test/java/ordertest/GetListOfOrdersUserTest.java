package ordertest;

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

public class GetListOfOrdersUserTest {
    private UserClient userClient;
    public Order order;
    public Ingredients ingredients;

    public User user = User.getRandomUser();
    public String accessUserToken;
    private ValidatableResponse response;
    private HashMap<String, List> orderHash;
    private List<String> ingredient = new ArrayList<>();


    @Before
    public void createNewUser() {
        order = new Order();
        userClient = new UserClient();
        ingredients = new Ingredients();
        orderHash = new HashMap<>();
        accessUserToken = userClient.create(user).extract().path("accessToken");
        userClient.login(user);
        List<String> deleteIngredient = ingredients.getIngredients().extract().path("data._id");
        ingredient.add(deleteIngredient.get(0));
        ingredient.add(deleteIngredient.get(deleteIngredient.size() - 1));
        orderHash.put("ingredients", ingredient);
    }

    @Test
    @DisplayName("Получение заказа пользователя")
    public void getOrderAuthUserTest() {
        order.create(orderHash, accessUserToken);
        response = order.getOrders(accessUserToken);
        response.assertThat().statusCode(200);
        response.assertThat().extract().path("total").equals(1);
        response.assertThat().extract().path("totalToday").equals(1);
    }


    @Test
    @DisplayName("Получение заказа не авторизованного пользователя")
    public void getOrderNotAuthUserTest() {
        order.create(orderHash, accessUserToken);
        response = order.getOrdersWithoutToken();
        response.assertThat().statusCode(401);
        response.assertThat().extract().path("success").equals(false);
        response.assertThat().extract().path("message").equals("You should be authorised");
    }

    @After
    public void tearDown() {
        userClient.delete(accessUserToken);
    }
}

