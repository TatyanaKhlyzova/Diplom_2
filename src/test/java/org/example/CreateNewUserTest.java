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

public class CreateNewUserTest {
    public String email = randomString(10) + "@yandex.ru";
    public String password = randomString(15);
    public String name = randomString(12);


    @Before
    public void setUp() {
        RestAssured.baseURI = Constant.BASE_URL;
    }
    @After
    public void deleteUser(){
        LoginUser loginUser = new LoginUser(email, password);
        String authorization =  given()
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
    @DisplayName("Check create unique user and receive status code 200")
    public void createUniqueUserStatusCode(){
        CreateNewUser newUser = new CreateNewUser(email,password, name);
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(newUser)
                .when()
                .post(Constant.CREATE_USER_API);
        response.then().assertThat()
                .statusCode(200);
        response.then().assertThat().body("success", equalTo(true));
    }




}
