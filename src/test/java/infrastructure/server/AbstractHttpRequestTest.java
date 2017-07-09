package infrastructure.server;

import org.junit.Test;

import java.util.Optional;

import static infrastructure.server.ContentType.JSON;
import static org.assertj.core.api.Assertions.assertThat;


public class AbstractHttpRequestTest {

    @Test
    public void path_parameters_should_be_parsed() {

        //Given
        HttpRequest mockHttpRequest = MockHttpRequest.builder()
                                                .withPath("/resource/1/resource2")
                                                .withRoute(Routes.get("/resource/{id}/resource2"))
                                                .build();

        //When
        Optional<String> value = mockHttpRequest.pathParameter("id");

        //Then
        assertThat(value)
                .isPresent()
                .contains("1");
    }

    @Test
    public void should_parse_body_when_the_content_type_is_supported(){
        //Given
        String json = "{\"id\":1}";
        HttpRequest mockHttpRequest = MockHttpRequest.builder()
                .withBody(json)
                .withContentType(JSON.value())
                .build();

        //When
        Optional<EntityForTesting> entityForTesting = mockHttpRequest.body(EntityForTesting.class);

        //Then
        assertThat(entityForTesting)
                .isPresent()
                .hasValue(new EntityForTesting(1, null));
    }

    @Test
    public void should_not_parse_body_when_the_content_type_is_not_supported(){
        //Given: a request with a non supported content type
        String json = "{\"id\":1}";
        HttpRequest mockHttpRequest = MockHttpRequest.builder()
                .withBody(json)
                .withContentType("")
                .build();

        //When
        Optional<EntityForTesting> entityForTesting = mockHttpRequest.body(EntityForTesting.class);

        //Then
        assertThat(entityForTesting)
                .isNotPresent();
    }


}