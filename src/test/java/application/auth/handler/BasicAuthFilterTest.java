package application.auth.handler;

import domain.model.user.AuthenticationService;
import domain.model.user.User;
import domain.model.user.UserBuilder;
import infrastructure.server.HttpContext;
import infrastructure.server.MockHttpContext;
import infrastructure.server.assertions.HttpContextAssert;
import infrastructure.server.exceptions.UnauthorizedException;
import io.reactivex.Maybe;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@RunWith(MockitoJUnitRunner.class)
public class BasicAuthFilterTest {

    //Basic authentication with values username:password
    private static final String BASIC_AUTHENTICATION = "Basic dXNlcm5hbWU6cGFzc3dvcmQ=";

    private static final String USERNAME = "username";

    private static final String PASSWORD = "password";

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private BasicAuthFilter basicAuthFilter;

    @Test
    public void user_should_be_unauthorized_if_credentials_are_invalid() {

        //Given
        HttpContext requestContext =  MockHttpContext.builder()
                .withHeader("Authorization", BASIC_AUTHENTICATION)
                .build();

        //SetUp: the user credentials are not valid
        Mockito.when(authenticationService.authenticate(USERNAME, PASSWORD)).thenReturn(Optional.empty());

        //When
        Maybe result = basicAuthFilter.handle(requestContext);

        //Then user is unauthorized
        assertThatThrownBy(result::blockingGet)
                .isInstanceOf(UnauthorizedException.class);

    }

    @Test
    public void user_should_be_authorized_if_credentials_are_valid()  {

        //Given
        HttpContext requestContext =  MockHttpContext.builder()
                .withHeader("Authorization", BASIC_AUTHENTICATION)
                .build();

        //SetUp: The user credentials are valid
        User user = UserBuilder.anUser();
        Mockito.when(authenticationService.authenticate(USERNAME, PASSWORD)).thenReturn(Optional.of(user));

        //When
        basicAuthFilter.handle(requestContext);

        //Then the user is assigned to the request context
        HttpContextAssert.assertThat(requestContext)
                .hasUser(user);

    }

}