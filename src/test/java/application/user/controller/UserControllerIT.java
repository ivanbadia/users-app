package application.user.controller;


import application.test.ApplicationIT;
import application.user.command.CreateUserCommand;
import application.user.command.UpdateUserCommand;
import application.user.representation.UserRepresentation;
import application.user.representation.UserRepresentationList;
import domain.model.user.Role;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.net.HttpURLConnection;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static application.AppUsers.asAdmin;
import static application.AppUsers.asPage1;
import static java.net.HttpURLConnection.*;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class UserControllerIT extends ApplicationIT{

    private final ContentType contentType;

    public UserControllerIT(ContentType contentType){
        this.contentType = contentType;
    }


    @Parameterized.Parameters(name="ContentType - {0}")
    public static Collection<ContentType> contentTypes() {
        //In order to test all the calls with the supported content types JSON and XML
        return Arrays.asList(io.restassured.http.ContentType.JSON, io.restassured.http.ContentType.XML);
    }

    @Test
    public void user_without_admin_role_should_not_be_able_to_create_users(){

        asPage1();

        given()
            .body(new CreateUserCommand(randomUserName(), "123", Collections.singleton(Role.PAGE_1)))
        .when()
                .post("/api/users")
        .then()
                .statusCode(HTTP_FORBIDDEN);
    }



    @Test
    public void user_should_be_created(){

        asAdmin();

        Set<Role> roles = Arrays.stream(new Role[] {Role.PAGE_1, Role.PAGE_2, Role.PAGE_3}).collect(Collectors.toSet());
        CreateUserCommand command = new CreateUserCommand(randomUserName(), "123", roles);

        UserRepresentation user = given()
                                         .body(command)
                                    .when()
                                        .post("/api/users")
                                    .then()
                                        .statusCode(HttpURLConnection.HTTP_OK)
                                        .extract().as(UserRepresentation.class);

        assertThat(user.getRoles()).isEqualTo(roles);
    }


    @Test
    public void user_without_admin_role_should_not_be_able_to_update_users(){

        asPage1();

        UpdateUserCommand command = new UpdateUserCommand("123", Collections.singleton(Role.PAGE_1));

        given()
            .body(command)
        .when()
            .put("/api/users/3")
        .then()
            .statusCode(HTTP_FORBIDDEN);
    }


    @Test
    public void user_should_be_updated(){

        asAdmin();

        //SetUp: create user
        UserRepresentation user = createUser();


        //Update user
        UpdateUserCommand command = new UpdateUserCommand("123", Collections.singleton(Role.PAGE_1));

        given()
            .body(command)
        .when()
            .put("/api/users/"+user.getId())
        .then()
                .statusCode(HTTP_NO_CONTENT);
    }

    @Test
    public void user_without_admin_role_should_not_be_able_to_delete_users(){

        asPage1();

        given()
        .when()
            .delete("/api/users/3")
        .then()
            .statusCode(HTTP_FORBIDDEN);
    }


    private UserRepresentation createUser() {
        return given()
                    .contentType(ContentType.JSON)
                    .accept(ContentType.JSON)
                    .body(new CreateUserCommand(randomUserName(), "123", Collections.singleton(Role.PAGE_1)))
            .when()
                    .post("/api/users")
            .then()
                    .statusCode(HttpURLConnection.HTTP_OK)
                    .extract()
                    .as(UserRepresentation.class);
    }


    @Test
    public void user_should_be_deleted(){

        asAdmin();

        //SetUp: create user
        UserRepresentation user = createUser();


        //Delete user
        RestAssured. given()
        .when()
            .delete("/api/users/"+user.getId())
        .then()
            .statusCode(HTTP_NO_CONTENT);
    }

    @Test
    public void user_should_be_retrieved(){

        asAdmin();

        //SetUp: create user
        UserRepresentation user = createUser();


        //Get user
        asPage1();

        UserRepresentation retrievedUser = given()
                                            .when()
                                                .get("/api/users/"+user.getId())
                                            .then()
                                                .statusCode(HTTP_OK)
                                                .extract().as(UserRepresentation.class);

        assertThat(retrievedUser)
                .matches(usernameEqualTo(user.getUsername()))
                .matches(rolesEqualTo(user.getRoles()));
    }

    private Predicate<UserRepresentation> rolesEqualTo(Set<Role> roles) {
        return u -> u.getRoles().equals(roles);
    }

    private Predicate<UserRepresentation> usernameEqualTo(String username) {
        return u -> u.getUsername().equals(username);
    }

    private String randomUserName() {
        return UUID.randomUUID().toString().substring(0, 10);
    }

    @Test
    public void users_should_be_retrieved(){

        asPage1();

        List<UserRepresentation> users = given()
                        .when()
                            .get("/api/users")
                        .then()
                            .statusCode(HTTP_OK)
                            .extract()
                            .as(UserRepresentationList.class).getUsers();

        assertThat(users)
                .isNotEmpty();
    }

    private RequestSpecification given() {
        return RestAssured.given()
                            .accept(contentType)
                            .contentType(contentType);
    }


}