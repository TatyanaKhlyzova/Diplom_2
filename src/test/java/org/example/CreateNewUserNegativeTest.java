package org.example;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.example.RandomData.randomString;
import static org.hamcrest.CoreMatchers.equalTo;

public class CreateNewUserNegativeTest {
    public String email = randomString(8) + "@yandex.ru";
    public String password = randomString(12);
    public String name = randomString(11);
    @Before
    public void setUp() {
        RestAssured.baseURI = Constant.BASE_URL;
    }
    @Test
    @DisplayName("Negative test - Check create double user")
    public void createDoubleUser() {
        CreateNewUser newUser = new CreateNewUser(email,password, name);
        given()
                .header("Content-type", "application/json")
                .and()
                .body(newUser)
                .when()
                .post(Constant.CREATE_USER_API);
        Response response =  given()
                .header("Content-type", "application/json")
                .and()
                .body(newUser)
                .when()
                .post(Constant.CREATE_USER_API);
        response.then().assertThat()
                .statusCode(403);
        response.then().assertThat().body("message", equalTo("User already exists"));
    }
    @Test
    @DisplayName("Negative test - Check create user without email")
    public void createUserWithoutEmail(){
        String email = "";
        CreateNewUser newUser = new CreateNewUser(email,password, name);
        Response response =  given()
                .header("Content-type", "application/json")
                .and()
                .body(newUser)
                .when()
                .post(Constant.CREATE_USER_API);
        response.then().assertThat()
                .statusCode(403);
        response.then().assertThat().body("message", equalTo("Email, password and name are required fields"));
    }
    @Test
    @DisplayName("Negative test - Check create user without password")
    public void createUserWithoutPassword(){
        String password = "";
        CreateNewUser newUser = new CreateNewUser(email,password, name);
        Response response =  given()
                .header("Content-type", "application/json")
                .and()
                .body(newUser)
                .when()
                .post(Constant.CREATE_USER_API);
        response.then().assertThat()
                .statusCode(403);
        response.then().assertThat().body("message", equalTo("Email, password and name are required fields"));
    }
    @Test
    @DisplayName("Negative test - Check create user without name")
    public void createUserWithoutName(){
        String name = "";
        CreateNewUser newUser = new CreateNewUser(email,password, name);
        Response response =  given()
                .header("Content-type", "application/json")
                .and()
                .body(newUser)
                .when()
                .post(Constant.CREATE_USER_API);
        response.then().assertThat()
                .statusCode(403);
        response.then().assertThat().body("message", equalTo("Email, password and name are required fields"));
    }

}
