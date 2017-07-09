package infrastructure.server;

import domain.model.user.Role;
import domain.model.user.UserBuilder;
import io.reactivex.Maybe;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.HttpURLConnection;
import java.util.HashSet;

import static infrastructure.server.Routes.*;
import static infrastructure.server.assertions.HttpContextAssert.assertThat;
import static java.net.HttpURLConnection.*;

public class RouterTest {

    private Router router;

    @Before
    public void initialise(){
        router = Router.instance();
    }

    @After
    public void cleanUp(){
        Routes.instance().clearAll();
    }


    @Test
    public void should_exist_just_one_instance(){
        //When
        Router instance1 = Router.instance();
        Router instance2 = Router.instance();

        //Then
        Assertions.assertThat(instance1)
                .isSameAs(instance2);
    }

    @Test
    public void should_return_not_found_when_route_does_not_exist() {
        //Given: a context with a path that doesn't exist
        HttpContext requestContext =  MockHttpContext.builder()
                .withHttpMethod(HttpMethod.GET)
                .withPath("notfound")
                .build();

        //When: we handle the request
        handleRequest(requestContext);

        //Then: not found is returned
        assertThat(requestContext)
                .hasStatusCode(HttpURLConnection.HTTP_NOT_FOUND);

    }



    @Test
    public void get_route_should_be_executed() {
        //Given: a GET route and a request to the route
        String response = "GET OK";

        get("/resource")
                .handler(returnBody(response));

        HttpContext requestContext =  MockHttpContext.builder()
                .withHttpMethod(HttpMethod.GET)
                .withPath("/resource")
                .build();


        //When: the request is handled
        handleRequest(requestContext);


        //Then: the expected answer is received
        assertThat(requestContext)
                .hasResponseBody(response);


    }


    @Test
    public void delete_route_should_be_executed() {
        //Given: a DELETE route and a request to the route
        String response = "DELETE OK";
        delete("/resource")
                .handler(returnBody(response));

        HttpContext requestContext =  MockHttpContext.builder()
                .withHttpMethod(HttpMethod.DELETE)
                .withPath("/resource")
                .build();


        //When the request is handled
        handleRequest(requestContext);


        //Then the expected answer is received
        assertThat(requestContext)
                .hasResponseBody(response);


    }



    @Test
    public void put_route_should_be_executed() {
        //Given: a PUT route and a request to the route
        String response = "PUT OK";
        put("/resource")
                .handler(returnBody(response));

        HttpContext requestContext = MockHttpContext.builder()
                .withHttpMethod(HttpMethod.PUT)
                .withPath("/resource")
                .build();


        //When: the request is handled
        handleRequest(requestContext);


        //Then: the expected answer is received
        assertThat(requestContext)
                .hasResponseBody(response);

    }


    @Test
    public void post_route_should_be_executed() {
        //Given: a POST route and a request to the route
        String response = "POST OK";
        post("/resource")
                .handler(returnBody(response));

        HttpContext requestContext = MockHttpContext.builder()
                .withHttpMethod(HttpMethod.POST)
                .withPath("/resource")
                .build();


        //When: the request is handled
        handleRequest(requestContext);


        //Then: the expected answer is received
        assertThat(requestContext)
                .hasResponseBody(response);

    }

    @Test
    public void request_result_should_be_written_to_the_response(){
        //Given: a route that returns "OK" and a request to the route
        String response = "OK";
        post("/resource")
                .handler(returnBody(response));

        HttpContext requestContext = MockHttpContext.builder()
                .withHttpMethod(HttpMethod.POST)
                .withPath("/resource")
                .build();


        //When: the request is handled
        handleRequest(requestContext);


        //Then: the expected answer is received
        assertThat(requestContext)
                .hasResponseBody(response)
                .hasStatusCode(HttpURLConnection.HTTP_OK)
                .isEnded();
    }

    private Handler returnBody(String body) {
        return context -> Maybe.just(body);
    }


    @Test
    public void should_return_no_content_when_no_body_is_returned() {

        //Given: a route that returns empty
        get("/resource")
                .handler(context -> Maybe.empty());

        HttpContext requestContext =  MockHttpContext.builder()
                .withHttpMethod(HttpMethod.GET)
                .withPath("/resource")
                .build();


        //When: the request is handled
        handleRequest(requestContext);


        //Then: the status code is no content
        assertThat(requestContext)
                .hasStatusCode(HTTP_NO_CONTENT);


    }

    @Test
    public void errors_executing_request_should_be_handled(){
        //Given: a route that throws an exception and a request to the route
        final String exceptionMessage = "Something is null";
        get("/resource")
                .handler(context -> {
                    throw new NullPointerException(exceptionMessage);
                });

        HttpContext requestContext =  MockHttpContext.builder()
                .withHttpMethod(HttpMethod.GET)
                .withPath("/resource")
                .build();

        //When: the request is handled
        handleRequest(requestContext);


        //Then: the expected answer is received
        assertThat(requestContext)
                .hasStatusCode(HTTP_INTERNAL_ERROR)
                .hasResponseBody(new Error(HTTP_INTERNAL_ERROR, exceptionMessage));
    }

    @Test
    public void path_parameters_should_be_parsed(){
        //Given: a route that returns a path parameter value and a request to this route
        String pathParamName = "pathParam";
        String paramValue = "paramValue";

        get(String.format("/resource1/{%s}/resource2", pathParamName))
                .handler(returnValueOfThePathParam(pathParamName));

        HttpContext requestContext =  MockHttpContext.builder()
                .withHttpMethod(HttpMethod.GET)
                .withPath(String.format("/resource1/%s/resource2", paramValue))
                .build();


        //When: the request is handled
        handleRequest(requestContext);


        //Then: the answer contains the parameter value
        assertThat(requestContext)
                .hasResponseBody(paramValue);
    }

    private void handleRequest(HttpContext requestContext) {
        router.handle(requestContext).blockingGet();
    }

    private Handler returnValueOfThePathParam(String pathParamName) {
        return context -> Maybe.just(context.request().pathParameter(pathParamName).get());
    }



    @Test
    public void filters_should_be_executed_before_route(){
        //Given: a filter and a route
        String filterResult = "KO";
        filter("/restricted/area").handler(returnBody(filterResult));
        get("/restricted/area").handler(returnBody("OK"));

        HttpContext requestContext = MockHttpContext.builder()
                .withHttpMethod(HttpMethod.GET)
                .withPath("/restricted/area")
                .build();


        //When: the request is handled
        handleRequest(requestContext);


        //Then: the answer contains the error returned by the filter
        assertThat(requestContext)
                .hasResponseBody(filterResult);
    }


    @Test
    public void access_should_be_denied_to_user_without_authority_if_authority_is_required(){
        //Given: a route with a required authority and a user without roles
        get("/restricted/area")
                .addAuthority(Role.ADMIN)
                .handler(returnBody("OK"));

        HttpContext requestContext = MockHttpContext.builder()
                .withHttpMethod(HttpMethod.GET)
                .withUser(UserBuilder.builder().withRoles(new HashSet<>()).build())
                .withPath("/restricted/area")
                .build();


        //When: the request is handled
        handleRequest(requestContext);


        //Then: the access is denied
        assertThat(requestContext)
                .hasStatusCode(HTTP_FORBIDDEN);
    }



    @Test
    public void access_should_be_denied_if_no_user_logged_and_authority_is_required(){
        //Given: a route with a required authority and no user logged
        get("/restricted/area")
                .addAuthority(Role.ADMIN)
                .handler(returnBody("OK"));

        HttpContext requestContext = MockHttpContext.builder()
                .withHttpMethod(HttpMethod.GET)
                .withPath("/restricted/area")
                .build();


        //When: the request is handled
        handleRequest(requestContext);


        //Then: the access is denied
        assertThat(requestContext)
                .hasStatusCode(HTTP_FORBIDDEN);
    }


    @Test
    public void the_response_content_type_should_be_the_produces_content_type(){
        //Given: a route with produces
        get("/resource1")
                .produces(ContentType.JSON)
                .handler(returnBody("Hi"));

        HttpContext requestContext = MockHttpContext.builder()
                .withHttpMethod(HttpMethod.GET)
                .withPath("/resource1")
                .build();


        //When: the request is handled
        handleRequest(requestContext);


        //Then: the content type is the expected content type
        assertThat(requestContext)
                .hasContentType(ContentType.JSON.value());
    }

}
