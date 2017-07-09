package application.web.handler;

import infrastructure.server.HttpContext;
import infrastructure.server.MockHttpContext;
import infrastructure.server.assertions.HttpContextAssert;
import infrastructure.session.SessionStore;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class LogoutUserTest {

    private static final String LOGIN_PAGE = "/login";
    @Mock
    private SessionStore sessionStore;

    private LogoutUser logoutUser;

    @Before
    public void initialize(){
        logoutUser = new LogoutUser(sessionStore, LOGIN_PAGE);
    }

    @Test
    public void session_should_be_removed(){
        //Given
        String sessionId = "sessionId";
        HttpContext httpContext = MockHttpContext.builder()
                .withSessionId(sessionId)
                .build();

        //When
        logoutUser.handle(httpContext);

        //Then
        verify(sessionStore).deleteSession(sessionId);
        HttpContextAssert.assertThat(httpContext)
                .wasRedirectedTo(LOGIN_PAGE);

    }

}