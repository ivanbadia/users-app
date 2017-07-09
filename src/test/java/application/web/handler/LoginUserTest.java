package application.web.handler;

import domain.model.user.AuthenticationService;
import domain.model.user.User;
import domain.model.user.UserBuilder;
import infrastructure.server.HttpContext;
import infrastructure.server.MockHttpContext;
import infrastructure.session.Session;
import infrastructure.session.SessionStore;
import infrastructure.template.ModelAndView;
import infrastructure.template.TemplateEngine;
import io.reactivex.Maybe;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Map;
import java.util.Optional;

import static application.web.handler.LoginPageRender.LOGIN_TEMPLATE;
import static infrastructure.server.assertions.HttpContextAssert.assertThat;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LoginUserTest {

    private static final String USERNAME = "user";
    private static final String PASSWORD = "pwd";
    private static final String HOME_PAGE = "home";

    @Mock
    private AuthenticationService authenticationService;
    @Mock
    private SessionStore sessionStore;

    @Mock
    private TemplateEngine templateEngine;

    private LoginUser loginUser;

    @Before
    public void initialize(){
        loginUser = new LoginUser(sessionStore, authenticationService, new LoginPageRender(templateEngine), HOME_PAGE);
    }

    @Test
    public void should_fail_if_username_is_not_sent() {

        //Given: a request without username
        HttpContext httpContext = MockHttpContext.builder()
                .withParameter(LoginUser.USERNAME_PARAMETER, "")
                .withParameter(LoginUser.PASSWORD_PARAMETER, "123")
                .build();

        //SetUp: Return a result when the page is rendered with the username error
        String loginPageWithError = "login page with username error";
        when(templateEngine.render(modelAndViewWithErrorMessage("The username is required"))).thenReturn(loginPageWithError);
        
        //When
        Maybe<String> result = loginUser.handle(httpContext);

        //Then
        Assertions.assertThat(result.blockingGet()).isEqualTo(loginPageWithError);

    }

    private ModelAndView modelAndViewWithErrorMessage(final String loginPageWithError) {
        return argThat(modelAndView -> ((Map)modelAndView.getModel()).get(LoginPageRender.ERROR_MESSAGE_VARIABLE).equals(loginPageWithError) && modelAndView.getView().equals(LOGIN_TEMPLATE));
    }

    @Test
    public void should_fail_if_password_is_not_sent() {

        //Given: a request without password
        HttpContext httpContext = MockHttpContext.builder()
                .withParameter(LoginUser.USERNAME_PARAMETER, "admin")
                .withParameter(LoginUser.PASSWORD_PARAMETER, "")
                .build();

        //SetUp: Return a result when the page is rendered with the password error
        String loginPageWithError = "login page with password error";
        when(templateEngine.render(modelAndViewWithErrorMessage("The password is required"))).thenReturn(loginPageWithError);

        //When
        Maybe<String> result = loginUser.handle(httpContext);

        //Then
        Assertions.assertThat(result.blockingGet()).isEqualTo(loginPageWithError);
    }


    @Test
    public void user_should_be_unauthorized_if_credentials_are_invalid() {

        //Given: a request
        HttpContext httpContext = MockHttpContext.builder()
                .withParameter(LoginUser.USERNAME_PARAMETER, USERNAME)
                .withParameter(LoginUser.PASSWORD_PARAMETER, PASSWORD)
                .build();

        //SetUp

        // Return a result when the page is rendered with the credentials error
        String loginPageWithError = "login page with credentials error";
        when(templateEngine.render(modelAndViewWithErrorMessage("The credentials are not valid"))).thenReturn(loginPageWithError);
        //The user credentials are not valid
        whenAuthenticateUserReturn(Optional.empty());
        
        //When
        Maybe<String> result = loginUser.handle(httpContext);

        //Then
        Assertions.assertThat(result.blockingGet()).isEqualTo(loginPageWithError);

    }


    @Test
    public void previous_page_should_be_kept_in_login_form() {
        //Given: a request
        String previousPage = "/previous";
        HttpContext httpContext = MockHttpContext.builder()
                .withParameter(LoginUser.USERNAME_PARAMETER, USERNAME)
                .withParameter(LoginUser.PASSWORD_PARAMETER, PASSWORD)
                .withParameter(LoginUser.FROM_PARAMETER, previousPage)
                .build();

        //SetUp
        // Return a result when the page is rendered with the from variable
        String loginPageWithError = "login page with from";
        when(templateEngine.render(modelAndViewWithFromKeyEqualTo(previousPage))).thenReturn(loginPageWithError);
        //the user credentials are not valid
        whenAuthenticateUserReturn(Optional.empty());

        //When
        Maybe<String> result = loginUser.handle(httpContext);

        //Then
        Assertions.assertThat(result.blockingGet()).isEqualTo(loginPageWithError);


    }

    private ModelAndView modelAndViewWithFromKeyEqualTo(String previousPage) {
        return argThat(modelAndView -> {
            Map<String, Object> map = (Map<String, Object>) modelAndView.getModel();
            return map.containsKey(LoginPageRender.FROM_VARIABLE) && map.get(LoginPageRender.FROM_VARIABLE).equals(previousPage);
        });
    }


    @Test
    public void user_should_be_authorized_if_credentials_are_valid() {
        //Given: a request
        HttpContext httpContext = MockHttpContext.builder()
                .withParameter(LoginUser.USERNAME_PARAMETER, USERNAME)
                .withParameter(LoginUser.PASSWORD_PARAMETER, PASSWORD)
                .build();

        //SetUp: the user credentials are valid and the session is created
        User user = UserBuilder.anUser();
        whenAuthenticateUserReturn(Optional.of(user));
        sessionIsCreatedSuccessfullyFor(user);

        //When
        loginUser.handle(httpContext);

        //Then
        assertThat(httpContext)
                .containsHeader("Set-Cookie")
                .wasRedirectedTo(HOME_PAGE);
    }

    private void whenAuthenticateUserReturn(Optional<User> user) {
        when(authenticationService.authenticate(USERNAME, PASSWORD)).thenReturn(user);
    }


    @Test
    public void user_should_be_redirected_to_the_page_it_was_trying_to_reach_before() {
        //Given: a request with from parameter
        String previousPage = "/previouspage";
        HttpContext httpContext = MockHttpContext.builder()
                .withParameter(LoginUser.USERNAME_PARAMETER, USERNAME)
                .withParameter(LoginUser.PASSWORD_PARAMETER, PASSWORD)
                .withParameter("from", previousPage)
                .build();

        //SetUp: the user credentials are valid and the session is created
        whenAuthenticateUserReturn(Optional.of(UserBuilder.anUser()));
        sessionIsCreatedSuccessfullyFor(UserBuilder.anUser());

        //When
        loginUser.handle(httpContext);

        //Then
        assertThat(httpContext)
                .wasRedirectedTo(previousPage);
    }

    private void sessionIsCreatedSuccessfullyFor(User user) {
        when(sessionStore.createSession(user)).thenReturn(new Session("sessionId", user, 1000));
    }
}