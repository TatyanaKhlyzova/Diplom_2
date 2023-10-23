package org.example;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;


import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class GetOrdersNegativeTest {
    @Before
    public void setUp() {
        RestAssured.baseURI = Constant.BASE_URL;
    }
    @Test
    @DisplayName("Negative test - check receive orders for unauthorized user")
    public void receiveOrderForUnauthorizedUser(){

        Response response = given()
                .get(Constant.GET_ORDER_API);
        response.then().assertThat()
                .statusCode(401);
        response.then().assertThat().body("message", equalTo("You should be authorised"));

    }
}
