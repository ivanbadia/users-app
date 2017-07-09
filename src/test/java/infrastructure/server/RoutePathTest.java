package infrastructure.server;


import org.junit.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class RoutePathTest {



    @Test
    public void a_different_path_should_not_match(){
        //Given
        RoutePath routePath = new RoutePath("/resource1/{id}/resource2/{id2}");
        String path = "/x/1/y/2";

        //When
        boolean matches = routePath.matches(path);

        //Then
        assertThat(matches)
                .isFalse();
    }


    @Test
    public void the_same_path_should_match(){
        //Given
        RoutePath routePath = new RoutePath("/resource1/{id}/resource2/{id2}");
        String path = "/resource1/1/resource2/2";

        //When
        boolean matches = routePath.matches(path);

        //Then
        assertThat(matches)
                .isTrue();
    }


    @Test
    public void a_path_that_matches_just_the_beginning_should_not_match(){
        //Given
        RoutePath routePath = new RoutePath("/resource1/{id}/resource2/{id2}");
        String path = "/resource1/1/resource2/2/x";

        //When
        boolean matches = routePath.matches(path);

        //Then
        assertThat(matches).isFalse();
    }


    @Test
    public void a_route_path_defined_with_asterisk_should_match(){
        //Given
        RoutePath routePath = new RoutePath("/resource1/{id}/*");
        String path = "/resource1/1/resource2/2/x";

        //When
        boolean matches = routePath.matches(path);

        //Then
        assertThat(matches).isTrue();
    }


    @Test
    public void path_parameters_should_be_retrieved()  {
        //Given
        RoutePath routePath = new RoutePath("/x/{id}/y/{id2}");
        String path = "/x/1/y/2";

        //When
        Map<String,String> pathParams = routePath.pathParamsOf(path);

        //Then
        assertThat(pathParams)
                .hasSize(2)
                .containsEntry("id", "1")
                .containsEntry("id2", "2");


    }

}
