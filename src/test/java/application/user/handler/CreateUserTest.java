package application.user.handler;

import application.user.command.CreateUserCommand;
import application.user.representation.UserRepresentation;
import com.fasterxml.jackson.databind.ObjectMapper;
import domain.model.user.Role;
import domain.model.user.User;
import domain.model.user.UserRepository;
import infrastructure.server.ContentType;
import infrastructure.server.HttpContext;
import infrastructure.server.MockHttpContext;
import infrastructure.server.exceptions.BadRequestException;
import io.reactivex.Maybe;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CreateUserTest {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private UserRepository userRepository;


    @InjectMocks
    private CreateUser createUser;

    @Test
    public void should_fail_if_body_is_not_valid() {
        //Given: a request without body
        HttpContext httpContext = MockHttpContext.builder()
                .withContentType(ContentType.JSON.value())
                .build();


        //When
        Maybe<UserRepresentation> result = createUser.handle(httpContext);

        //Then
        assertThatThrownBy(result::blockingGet)
                .isInstanceOf(BadRequestException.class);

    }


    @Test
    public void user_should_be_created() throws Exception{
        //Given: a create user command
        CreateUserCommand command = new CreateUserCommand("username", "password", Collections.singleton(Role.ADMIN));
        HttpContext httpContext = MockHttpContext.builder()
                .withBody(objectMapper.writeValueAsString(command))
                .withContentType(ContentType.JSON.value())
                .build();


        //SetUp: The nextId is generated
        long nextId = 1L;
        when(userRepository.nextId()).thenReturn(nextId);

        //When
        Maybe<UserRepresentation> result = createUser.handle(httpContext);

        //Then: the user is persisted and the UserRepresentation contains the expected values
        User expectedUser = new User(nextId, command.getUsername(), command.getPassword(), command.getRoles());
        verify(userRepository).add(expectedUser);
        assertThat(result.blockingGet())
                .isEqualTo(UserRepresentation.from(expectedUser));

    }

}