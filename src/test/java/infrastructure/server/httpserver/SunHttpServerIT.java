package infrastructure.server.httpserver;

import infrastructure.server.Server;
import io.reactivex.Maybe;
import io.restassured.http.ContentType;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.net.HttpURLConnection;


import static infrastructure.server.Routes.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;


public class SunHttpServerIT {
    private static final String PATH = "/test";
    private static final String REDIRECT_PATH = "/redirect";
    private static final String EXPECTED_BODY = "ok";
    private static Server server;


    @BeforeClass
    public static void initialize(){
        get(PATH)
                .handler(context -> Maybe.just(EXPECTED_BODY));

        get(REDIRECT_PATH)
                .handler(context -> {
                    context.response().redirect(PATH);
                    return Maybe.empty();
                });

        server = new SunHttpServer();
        server.start();
    }

    @AfterClass
    public static void stop(){
        server.stop();
    }

    @Test
    public void server_should_handle_request(){

        given()
                .accept(ContentType.HTML)
        .when()
                .get(PATH)
        .then()
                .statusCode(HttpURLConnection.HTTP_OK)
                .body(equalTo(EXPECTED_BODY));

    }


    @Test
    public void server_should_redirect_to_another_route(){
        given()
                .accept(ContentType.HTML)
        .when()
            .get(REDIRECT_PATH)
        .then()
            .statusCode(HttpURLConnection.HTTP_OK)
            .body(equalTo(EXPECTED_BODY));

    }
}
