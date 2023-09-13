package org.example;

import io.restassured.RestAssured;
import org.junit.Before;

import static io.restassured.RestAssured.given;
import static org.example.RandomData.randomString;

public class UpdateAuthorizedUserTest {
    public String email = randomString(10) + "@yandex.ru";
    public String password = randomString(15);
    public String name = randomString(12);
    LoginUser loginUser = new LoginUser(email, password);
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

}
