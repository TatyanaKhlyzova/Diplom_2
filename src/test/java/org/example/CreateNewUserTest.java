package org.example;

import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;

import static org.example.RandomData.randomString;

public class CreateNewUserTest {
    public String email = randomString(10) + "@yandex.ru";
    public String password = randomString(15);
    public String name = randomString(12);


    @Before
    public void setUp() {
        RestAssured.baseURI = Constant.BASE_URL;
    }
    @Test
    public void createUniqueUser(){



    }

}
