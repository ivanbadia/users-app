package application.web.controller;

import application.AppUsers;
import application.test.ApplicationIT;
import io.restassured.filter.session.SessionFilter;
import org.junit.Test;

import static application.web.controller.ApplicationController.*;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static java.net.HttpURLConnection.HTTP_OK;
import static org.hamcrest.core.IsEqual.equalTo;


public class ApplicationControllerIT extends ApplicationIT{

    private static final String HTML_TITLE = "html.head.title";
    private static final String HELLO_PAGE_TITLE = "Hello Page";
    private static final String HTML_BODY = "html.body";
    private static final String HOME_PAGE_TITLE = "Home Page";
    public static final String LOGIN_PAGE_TITLE = "Login";


    @Test
    public void login_page_should_be_shown(){

        when()
                .get("/")
        .then()
                .statusCode(HTTP_OK)
                .body(HTML_TITLE, equalTo(LOGIN_PAGE_TITLE));
    }


    @Test
    public void user_should_be_able_to_login(){

        given()
                .auth().form(AppUsers.ADMIN_USERNAME, AppUsers.ADMIN_PASSWORD).
        when()
                .get(ApplicationController.HOME_PAGE)
        .then()
                .statusCode(HTTP_OK)
                .body(HTML_TITLE, equalTo(HOME_PAGE_TITLE));
    }

    @Test
    public void user_without_authority_page1_should_not_be_able_to_access_page1(){

        given()
                .auth().form(AppUsers.PAGE2_USERNAME, AppUsers.PAGE2_PASSWORD).
        when()
                .get(PAGE1)
        .then()
                .body(HTML_BODY, equalTo("User hasn't got the authority PAGE_1"));


    }


    @Test
    public void user_with_authority_page1_should_be_able_to_access_page1(){

        given()
                .auth().form(AppUsers.PAGE1_USERNAME, AppUsers.PAGE1_PASSWORD).
        when()
                .get(PAGE1)
        .then()
                .body(HTML_TITLE, equalTo(ApplicationControllerIT.HELLO_PAGE_TITLE));


    }


    @Test
    public void user_without_authority_page2_should_not_be_able_to_access_page2(){

        given()
                .auth().form(AppUsers.PAGE1_USERNAME, AppUsers.PAGE1_PASSWORD).
        when()
                .get(PAGE2)
        .then()
                .body(HTML_BODY, equalTo("User hasn't got the authority PAGE_2"));


    }


    @Test
    public void user_with_authority_page2_should_be_able_to_access_page2(){
        given()
                .auth().form(AppUsers.PAGE2_USERNAME, AppUsers.PAGE2_PASSWORD).
        when()
                .get(PAGE2)
        .then()
                .body(HTML_TITLE, equalTo(ApplicationControllerIT.HELLO_PAGE_TITLE));

    }




    @Test
    public void user_without_authority_page3_should_not_be_able_to_access_page3(){

        given()
                .auth().form(AppUsers.PAGE1_USERNAME, AppUsers.PAGE1_PASSWORD).
        when()
                .get(PAGE3)
        .then()
                .body(HTML_BODY, equalTo("User hasn't got the authority PAGE_3"));


    }


    @Test
    public void user_with_authority_page3_should_be_able_to_access_page3(){

        given()
                .auth().form(AppUsers.PAGE3_USERNAME, AppUsers.PAGE3_PASSWORD).
        when()
                .get(PAGE3)
        .then()
                .body(HTML_TITLE, equalTo(HELLO_PAGE_TITLE));


    }

    @Test
    public void user_should_logout(){
        SessionFilter sessionFilter = new SessionFilter();

        //SetUp: Login as page1 user
        given()
                .auth().form(AppUsers.PAGE1_USERNAME, AppUsers.PAGE1_PASSWORD)
                .filter(sessionFilter).
        when()
                .get(HOME_PAGE);

        //When: logout
        given()
                .filter(sessionFilter)
        .when()
                .post(LOGOUT);



        //Then: The user is redirected to the login page
        given()
                .filter(sessionFilter)
        .when()
                .post(HOME_PAGE)
        .then()
                .body(HTML_TITLE, equalTo(LOGIN_PAGE_TITLE));

    }
}
