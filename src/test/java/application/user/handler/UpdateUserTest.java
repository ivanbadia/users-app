package application.user.handler;

import application.user.command.UpdateUserCommand;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import domain.model.user.*;
import infrastructure.server.*;
import infrastructure.server.exceptions.BadRequestException;
import infrastructure.server.exceptions.NotFoundException;
import io.reactivex.Maybe;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UpdateUserTest {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UpdateUser updateUser;

    @Test
    public void should_fail_if_body_is_not_valid() {
        //Given a request without body
        HttpContext httpContext = MockHttpContext.builder()
                .withContentType(ContentType.JSON.value())
                .build();

        //When
        Maybe result = updateUser.handle(httpContext);

        //Then bad request is thrown
        assertThatThrownBy(result::blockingGet)
                .isInstanceOf(BadRequestException.class);

    }


    @Test
    public void should_fail_if_user_does_not_exist() throws JsonProcessingException {
        //Given
        UpdateUserCommand command = new UpdateUserCommand( "password", Collections.singleton(Role.ADMIN));
        HttpContext httpContext = MockHttpContext.builder()
                .withBody(objectMapper.writeValueAsString(command))
                .withPath("/users/1")
                .withRoute(Routes.get("/users/{id}"))
                .withContentType(ContentType.JSON.value())
                .build();

        //SetUp: User is not found
        when(userRepository.byId(1)).thenReturn(Optional.empty());

        //When
        Maybe result = updateUser.handle(httpContext);

        //Then not found exception is thrown
        assertThatThrownBy(result::blockingGet)
                .isInstanceOf(NotFoundException.class);

    }


    @Test
    public void user_should_be_updated() throws JsonProcessingException {
        //Given
        UpdateUserCommand command = new UpdateUserCommand( "password", Collections.singleton(Role.ADMIN));

        int userId = 1;
        HttpContext httpContext = MockHttpContext.builder()
                .withBody(objectMapper.writeValueAsString(command))
                .withPath("/users/"+userId)
                .withRoute(Routes.put("/users/{id}"))
                .withContentType(ContentType.JSON.value())
                .build();


        //SetUp
        User updatedUser = UserBuilder.anUser();
        when(userRepository.byId(userId)).thenReturn(Optional.ofNullable(updatedUser));


        //When
        updateUser.handle(httpContext);

        //Then: The user has been modified
        User expectedUser = new User(updatedUser.getId(), updatedUser.getUsername(), command.getPassword(), command.getRoles());
        assertThat(updatedUser)
                .isEqualTo(expectedUser);
    }

}