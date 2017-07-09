package domain.model.user;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthenticationService authenticationService;


    @Test
    public void username_should_be_required_to_be_authenticated(){
        //Given
        String username = null;
        String password = "password";

        //Then
        assertThatThrownBy(() -> authenticationService.authenticate(username, password))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("username is required");
    }


    @Test
    public void password_should_be_required_to_be_authenticated() {
        //Given
        String username = "username";
        String password = null;

        //Then
        assertThatThrownBy(() -> authenticationService.authenticate(username, password))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("password is required");
    }

    @Test
    public void user_should_not_be_authenticated_when_credentials_are_invalid(){
        //Given
        String username = "username";
        String password = "password";

        //SetUp: The user is not found
        when(userRepository.byUsername(username)).thenReturn(Optional.empty());

        //When
        Optional<User> user = authenticationService.authenticate(username, password);

        //Then
        assertThat(user)
                .isNotPresent();

    }

    @Test
    public void user_should_be_authenticated_when_credentials_are_valid(){
        //Given
        String username = "username";
        String password = "password";

        //SetUp: The user is found
        User user = UserBuilder.builder().withPassword(password).build();
        when(userRepository.byUsername(username)).thenReturn(Optional.of(user));

        //When
        Optional<User> authenticatedUser = authenticationService.authenticate(username, password);

        //Then
        assertThat(authenticatedUser)
                .isPresent()
                .contains(user);

    }

}