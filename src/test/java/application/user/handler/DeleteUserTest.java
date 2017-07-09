package application.user.handler;

import domain.model.user.User;
import domain.model.user.UserBuilder;
import domain.model.user.UserRepository;
import infrastructure.server.HttpContext;
import infrastructure.server.MockHttpContext;
import infrastructure.server.Routes;
import infrastructure.server.exceptions.NotFoundException;
import io.reactivex.Maybe;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DeleteUserTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private DeleteUser deleteUser;

    @Test
    public void should_return_error_if_user_does_not_exist(){
        //Given
        HttpContext httpContext = MockHttpContext.builder()
                .withPath("/users/1")
                .withRoute(Routes.delete("/users/{id}"))
                .build();

        //SetUp: User is not found
        when(userRepository.byId(1)).thenReturn(Optional.empty());

        //When
        Maybe result = deleteUser.handle(httpContext);

        //Then: NotFoundException is thrown
        assertThatThrownBy(result::blockingGet).isInstanceOf(NotFoundException.class);

    }

    @Test
    public void user_should_be_removed()  {

        //Given
        HttpContext httpContext = MockHttpContext.builder()
                .withPath("/users/1")
                .withRoute(Routes.delete("/users/{id}"))
                .build();

        //SetUp: User is found
        User user = UserBuilder.anUser();
        when(userRepository.byId(1)).thenReturn(Optional.ofNullable(user));

        //When
        Maybe result = deleteUser.handle(httpContext);

        //Then: user has been removed and the result is empty
        verify(userRepository).remove(user);
        boolean isEmpty = (boolean) result.isEmpty().blockingGet();
        assertThat(isEmpty).isTrue();
    }

}