package org.example;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.example.RandomData.randomString;
import static org.hamcrest.CoreMatchers.equalTo;

public class CreateOrderNegativeTest {
        public String email = randomString(10) + "@yandex.ru";
        public String password = randomString(15);
        public String name = randomString(12);
        public String authorization;

        @Before
        public void setUp() {
            RestAssured.baseURI = Constant.BASE_URL;
        }
        @Before
        public void createNewUser(){
            CreateNewUser newUser = new CreateNewUser(email,password, name);
            given()
                    .header("Content-type", "application/json")
                    .and()
                    .body(newUser)
                    .post(Constant.CREATE_USER_API);

        }
    @After
    public void deleteUser(){
        DeleteUser delete = new DeleteUser(email,password);
        given()
                .header("Authorization", authorization)
                .body(delete)
                .delete(Constant.DELETE_USER_API);
    }
        @Test
        @DisplayName("Negative test - check create order without ingredients for authorized user")
        public void createOrderWithoutIngredientsForAuthorizedUser(){
            LoginUser loginUser = new LoginUser(email, password);
            authorization =  given()
                    .header("Content-type", "application/json")
                    .and()
                    .body(loginUser)
                    .when()
                    .post(Constant.LOGIN_USER_API)
                    .then().extract().body().path("accessToken");
            List<String> ingredients = new ArrayList<>();

            CreateOrder order = new CreateOrder(ingredients);
            Response response = given()
                    .header("Content-type", "application/json")
                    .header("Authorization", authorization)
                    .and()
                    .body(order)
                    .when()
                    .post(Constant.CREATE_ORDER_API);
            response.then().assertThat()
                    .statusCode(400);
            response.then().assertThat().body("success", equalTo(false));
            response.then().assertThat().body("message", equalTo("Ingredient ids must be provided"));
        }
    @Test
    @DisplayName("Negative test - check create order with wrong ingredients for authorized user")
    public void createOrderWithWrongIngredientsForAuthorizedUser(){
        LoginUser loginUser = new LoginUser(email, password);
        authorization =  given()
                .header("Content-type", "application/json")
                .and()
                .body(loginUser)
                .when()
                .post(Constant.LOGIN_USER_API)
                .then().extract().body().path("accessToken");

        List<String> ingredients = new ArrayList<>();
        ingredients.add(randomString(24));

        CreateOrder order = new CreateOrder(ingredients);
        Response response = given()
                .header("Content-type", "application/json")
                .header("Authorization", authorization)
                .and()
                .body(order)
                .when()
                .post(Constant.CREATE_ORDER_API);
        response.then().assertThat()
                .statusCode(500);
    }

      /*Согласно документации API - создавать заказ может только авторизованный пользователь, тест упадет,
    потому что заказ создает успешно и неавторизованный пользователь - баг в приложении
     */

    @Test
    @DisplayName("Check create order  with ingredients for unauthorized user")
    public void createOrderWithIngredientsForUnauthorizedUser(){
        String firstIngredientId = given()
                .get(Constant.GET_INGREDIENTS_API)
                .then().extract().body().path("data[1]._id");
        String secondIngredientId = given()
                .get(Constant.GET_INGREDIENTS_API)
                .then().extract().body().path("data[6]._id");

        List<String> ingredients = new ArrayList<>();
        ingredients.add(firstIngredientId);
        ingredients.add(secondIngredientId);

        CreateOrder order = new CreateOrder(ingredients);
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(order)
                .when()
                .post(Constant.CREATE_ORDER_API);
        response.then().assertThat()
                .statusCode(401);
        response.then().assertThat().body("success", equalTo(false));
    }
}
