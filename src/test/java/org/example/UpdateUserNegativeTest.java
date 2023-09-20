package org.example;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.example.RandomData.randomString;
import static org.hamcrest.CoreMatchers.equalTo;

public class UpdateUserNegativeTest {
    public String email = randomString(8) + "@yandex.ru";
    public String password = randomString(12);
    public String name = randomString(10);

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
    @Test
    @DisplayName("Negative test - check update email for unauthorized user")
    public void updateEmailForUnauthorizedUser(){
        String email = randomString(10) + "@yandex.ru";
        UpdateUser updateUser = new UpdateUser(email, password, name);
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(updateUser)
                .when()
                .patch(Constant.UPDATE_USER_API);
        response.then().assertThat()
                .statusCode(401);
        response.then().assertThat().body("message", equalTo("You should be authorised"));
    }
    @Test
    @DisplayName("Negative test - check update password for unauthorized user")
    public void updatePasswordForUnauthorizedUser(){
        String password = randomString(12);
        UpdateUser updateUser = new UpdateUser(email, password, name);
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(updateUser)
                .when()
                .patch(Constant.UPDATE_USER_API);
        response.then().assertThat()
                .statusCode(401);
        response.then().assertThat().body("message", equalTo("You should be authorised"));
    }
    @Test
    @DisplayName("Negative test - check update name for unauthorized user")
    public void updateNameForUnauthorizedUser(){
        String name = randomString(10);
        UpdateUser updateUser = new UpdateUser(email, password, name);
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(updateUser)
                .when()
                .patch(Constant.UPDATE_USER_API);
        response.then().assertThat()
                .statusCode(401);
        response.then().assertThat().body("message", equalTo("You should be authorised"));
    }
    @Test
    @DisplayName("Negative test - check update email to email that is already in use")
    public void updateEmailToExistingEmail(){
        LoginUser loginUser = new LoginUser(email, password);
        String authorization = given()
                .header("Content-type", "application/json")
                .and()
                .body(loginUser)
                .post(Constant.LOGIN_USER_API)
                .then().extract().body().path("accessToken");

        String email = "existingMail@yandex.ru";
        CreateNewUser newUser = new CreateNewUser(email,password, name);
            given()
                    .header("Content-type", "application/json")
                    .and()
                    .body(newUser)
                    .post(Constant.CREATE_USER_API);

        UpdateUser updateUser = new UpdateUser(email, password, name);
        Response response = given()
                .header("Content-type", "application/json")
                .header("Authorization", authorization)
                .and()
                .body(updateUser)
                .when()
                .patch(Constant.UPDATE_USER_API);
        response.then().assertThat()
                .statusCode(403);
        response.then().assertThat().body("message", equalTo("User with such email already exists"));
    }
}
