package org.example;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.example.RandomData.randomString;
import static org.hamcrest.CoreMatchers.equalTo;


public class LoginUserNegativeTest {
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
        LoginUser loginUser = new LoginUser(email, password);
        authorization =  given()
                .header("Content-type", "application/json")
                .and()
                .body(loginUser)
                .when()
                .post(Constant.LOGIN_USER_API)
                .then().extract().body().path("accessToken");
        DeleteUser delete = new DeleteUser(email,password);
        given()
                .header("Authorization", authorization)
                .body(delete)
                .delete(Constant.DELETE_USER_API);
    }
    @Test
    @DisplayName("Negative test - Check login courier in system with non-existent email")
    public void loginUserInSystemWithNonExistentEmail() {
        String email = randomString(9) + "@yandex.ru";
        LoginUser loginUser = new LoginUser(email, password);
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(loginUser)
                .when()
                .post(Constant.LOGIN_USER_API);
        response.then().assertThat()
                .statusCode(401);
        response.then().assertThat().body("message", equalTo("email or password are incorrect"));

    }
    @Test
    @DisplayName("Negative test - Check login courier in system with non-existent password")
    public void loginUserInSystemWithNonExistentPassword() {
        String password = randomString(9);
        LoginUser loginUser = new LoginUser(email, password);
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(loginUser)
                .when()
                .post(Constant.LOGIN_USER_API);
        response.then().assertThat()
                .statusCode(401);
        response.then().assertThat().body("message", equalTo("email or password are incorrect"));

    }
}
