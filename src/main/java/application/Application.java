package application;

import domain.model.DomainContext;
import domain.model.user.Role;
import domain.model.user.User;
import domain.model.user.UserRepository;
import infrastructure.InfrastructureContext;
import infrastructure.server.Controller;
import infrastructure.server.Server;

import java.io.IOException;
import java.util.Collections;


public class Application {

    private static Server server;

    public static void main(String[] args) {
        setUp();
        startServer();
        waitForKeyPressed();
        stopServer();
    }

    public static void setUp() {
        initDB();
        configureControllers();
    }

    public static void startServer() {
        System.out.println("Starting server...");
        server = InfrastructureContext.server();
        server.start();
        System.out.println("Server started");
    }

    private static void configureControllers() {
        System.out.println("Configuring controllers...");
        ApplicationContext.controllers()
                .forEach(Controller::configure);
    }

    private static void initDB() {
        System.out.println("Initializing database data...");
        UserRepository userRepository = DomainContext.userRepository();
        userRepository.add(new User(userRepository.nextId(), "admin", "admin", Collections.singleton(Role.ADMIN)));
        userRepository.add(new User(userRepository.nextId(), "page1", "123", Collections.singleton(Role.PAGE_1)));
        userRepository.add(new User(userRepository.nextId(), "page2", "123", Collections.singleton(Role.PAGE_2)));
        userRepository.add(new User(userRepository.nextId(), "page3", "123", Collections.singleton(Role.PAGE_3)));
    }

    public static void stopServer() {
        System.out.println("Stopping server");
        server.stop();
        System.out.println("Server stopped");
    }

    private static void waitForKeyPressed() {
        System.out.println("\n\nPress Enter to stop the server\n\n");
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
