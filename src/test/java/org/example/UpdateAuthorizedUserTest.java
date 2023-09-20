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

public class UpdateAuthorizedUserTest {
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
        LoginUser loginUser = new LoginUser(email, password);
        authorization = given()
                .header("Content-type", "application/json")
                .and()
                .body(loginUser)
                .post(Constant.LOGIN_USER_API)
                .then().extract().body().path("accessToken");
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
    @DisplayName("Check update email for authorized user")
    public void updateEmailForAuthorizedUser(){
        String email = randomString(10) + "@yandex.ru";
        UpdateUser updateUser = new UpdateUser(email, password, name);
        Response response = given()
                .header("Content-type", "application/json")
                .header("Authorization", authorization)
                .and()
                .body(updateUser)
                .when()
                .patch(Constant.UPDATE_USER_API);
        response.then().assertThat()
                .statusCode(200);
        response.then().assertThat().body("success", equalTo(true));
   }
    @Test
    @DisplayName("Check update password for authorized user")
    public void updatePasswordForAuthorizedUser(){
        String password = randomString(12);
        UpdateUser updateUser = new UpdateUser(email, password, name);
        Response response = given()
                .header("Content-type", "application/json")
                .header("Authorization", authorization)
                .and()
                .body(updateUser)
                .when()
                .patch(Constant.UPDATE_USER_API);
        response.then().assertThat()
                .statusCode(200);
        response.then().assertThat().body("success", equalTo(true));
    }
    @Test
    @DisplayName("Check update name for authorized user")
    public void updateNameForAuthorizedUser(){
        String name = randomString(9);
        UpdateUser updateUser = new UpdateUser(email, password, name);
        Response response = given()
                .header("Content-type", "application/json")
                .header("Authorization", authorization)
                .and()
                .body(updateUser)
                .when()
                .patch(Constant.UPDATE_USER_API);
        response.then().assertThat()
                .statusCode(200);
        response.then().assertThat().body("success", equalTo(true));
    }

}
