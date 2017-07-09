package application.auth.handler;

import application.web.handler.LoginPageRender;
import domain.model.user.User;
import domain.model.user.UserBuilder;
import infrastructure.server.HttpContext;
import infrastructure.server.MockHttpContext;
import infrastructure.server.assertions.HttpContextAssert;
import infrastructure.session.SessionStore;
import infrastructure.template.ModelAndView;
import infrastructure.template.TemplateEngine;
import io.reactivex.Maybe;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SessionAuthFilterTest {

    private static final String PATH = "/resource";
    @Mock
    private TemplateEngine templateEngine;

    @Mock
    private SessionStore sessionStore;

    private SessionAuthFilter authHandler;

    @Before
    public void initialise(){
        authHandler = new SessionAuthFilter(sessionStore, new LoginPageRender(templateEngine));
    }

    @Test
    public void user_should_be_sent_to_login_page_if_no_session(){
        //Given
        HttpContext httpContext = MockHttpContext.builder()
                .withPath("/resource")
                .build();

        //SetUp
        String loginPage = "loginPage";
        whenLoginPageIsRenderedReturn(loginPage);


        //When
        Maybe<String> result = authHandler.handle(httpContext);

        //Then
        assertThat(result.blockingGet()).isEqualTo(loginPage);

    }


    @Test
    public void user_should_be_sent_to_login_page_if_session_is_expired(){
        //Given
        String sessionId = "sessionId";
        HttpContext httpContext = MockHttpContext.builder()
                .withPath(PATH)
                .withSessionId(sessionId)
                .build();

        //SetUp
        //login page is rendered
        String loginPage = "loginPage";
        whenLoginPageIsRenderedReturn(loginPage);
        //session is expired
        when(sessionStore.userOf(sessionId)).thenReturn(Optional.empty());

        //When
        Maybe<String> result = authHandler.handle(httpContext);

        //Then
        assertThat(result.blockingGet()).isEqualTo(loginPage);

    }

    private void whenLoginPageIsRenderedReturn(String loginPage) {
        Map<String, Object> scopes = new HashMap<>();
        scopes.put("from", PATH);
        when(templateEngine.render(new ModelAndView(scopes, LoginPageRender.LOGIN_TEMPLATE))).thenReturn(loginPage);
    }


    @Test
    public void user_should_be_added_to_context_if_session_exists(){
        //Given
        String sessionId = "sessionId";
        HttpContext httpContext = MockHttpContext.builder()
                .withPath("/resource")
                .withSessionId(sessionId)
                .build();

        //SetUp
        User user = UserBuilder.anUser();
        when(sessionStore.userOf(sessionId)).thenReturn(Optional.of(user));


        //When
        Maybe<String> result = authHandler.handle(httpContext);

        //Then
        HttpContextAssert.assertThat(httpContext).hasUser(user);
        assertThat(result.isEmpty().blockingGet()).isTrue();

    }

}