package application.web.handler;

import domain.model.user.User;
import domain.model.user.UserBuilder;
import infrastructure.server.HttpContext;
import infrastructure.server.MockHttpContext;
import infrastructure.template.ModelAndView;
import infrastructure.template.TemplateEngine;
import io.reactivex.Maybe;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RenderHelloPageTest {

    @Mock
    private TemplateEngine templateEngine;

    @InjectMocks
    private RenderHelloPage renderHelloPage;

    @Test
    public void should_render_hello_page(){
        //Given
        User user = UserBuilder.anUser();
        HttpContext httpContext = MockHttpContext.builder().withUser(user).build();

        //SetUp
        String html = "Hi";
        when(templateEngine.render(new ModelAndView(user, "page"))).thenReturn(html);

        //When
        Maybe<String> result = renderHelloPage.handle(httpContext);

        //Then
        assertThat(result.blockingGet()).isEqualTo(html);
    }

}