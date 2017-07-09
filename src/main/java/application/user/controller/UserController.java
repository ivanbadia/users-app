package application.user.controller;

import application.auth.handler.BasicAuthFilter;
import application.user.handler.*;
import domain.model.user.AuthenticationService;
import domain.model.user.Role;
import domain.model.user.UserRepository;
import infrastructure.server.Controller;

import static infrastructure.server.Routes.*;


public class UserController implements Controller{

    private final AuthenticationService authenticationService;
    private final UserRepository userRepository;

    public UserController(AuthenticationService authenticationService, UserRepository userRepository){
        this.authenticationService = authenticationService;
        this.userRepository = userRepository;
    }

    @Override
    public void configure() {
        //basic authentication is required
        filter("/api/*")
                .handler(new BasicAuthFilter(authenticationService));


        //Admin actions
        post("/api/users")
                .addAuthority(Role.ADMIN)
                .handler(new CreateUser(userRepository));

        put("/api/users/{id}")
                .addAuthority(Role.ADMIN)
                .handler(new UpdateUser(userRepository));

        delete("/api/users/{id}")
                .addAuthority(Role.ADMIN)
                .handler(new DeleteUser(userRepository));

        //Read actions
        get("/api/users")
                .handler(new GetUsers(userRepository));


        get("/api/users/{id}")
                .handler(new GetUser(userRepository));
    }
}
