package application.test;

import application.Application;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import java.util.concurrent.atomic.AtomicBoolean;

public abstract class ApplicationIT {
    private static AtomicBoolean init = new AtomicBoolean(false);

    @BeforeClass
    public static void setUp(){
        if(init.compareAndSet(false, true)) {
            Application.setUp();
            RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
        }
        Application.startServer();
    }

    @AfterClass
    public static void stop(){
        Application.stopServer();
    }
}
