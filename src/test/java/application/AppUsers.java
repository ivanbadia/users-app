package application;


import io.restassured.RestAssured;

import static io.restassured.RestAssured.preemptive;

public class AppUsers {


    public static final String PAGE1_USERNAME = "page1";
    public static final String PAGE1_PASSWORD = "123";

    public static final String ADMIN_USERNAME = "admin";
    public static final String ADMIN_PASSWORD = "admin";

    public static final String PAGE2_USERNAME = "page2";
    public static final String PAGE2_PASSWORD = "123";

    public static final String PAGE3_USERNAME = "page3";
    public static final String PAGE3_PASSWORD = "123";

    private static void asUser(String username, String password) {
        RestAssured.authentication = preemptive().basic(username, password);
    }

    public static void asPage1() {
        asUser(PAGE1_USERNAME, PAGE1_PASSWORD);
    }

    public static void asAdmin() {
        asUser(ADMIN_USERNAME, ADMIN_PASSWORD);
    }


}
