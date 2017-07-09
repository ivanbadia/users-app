package application;


import application.user.controller.UserController;
import application.web.controller.ApplicationController;
import domain.model.DomainContext;
import infrastructure.InfrastructureContext;
import infrastructure.server.Controller;

import java.util.Arrays;
import java.util.List;

class ApplicationContext {

    public static List<Controller> controllers(){
        return Arrays.asList(userController(), applicationController());
    }

    private static UserController userController(){
        return new UserController(DomainContext.authenticationService(), DomainContext.userRepository());
    }

    private static ApplicationController applicationController(){
        return  new ApplicationController(InfrastructureContext.templateEngine(), InfrastructureContext.sessionStore(), DomainContext.authenticationService());
    }

}
