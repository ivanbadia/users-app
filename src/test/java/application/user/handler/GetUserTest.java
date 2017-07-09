package application.user.handler;

import application.user.representation.UserRepresentation;
import domain.model.user.User;
import domain.model.user.UserBuilder;
import domain.model.user.UserRepository;
import infrastructure.server.*;
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
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GetUserTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private GetUser getUser;


    @Test
    public void should_fail_if_user_does_not_exist(){

        //Given
        HttpContext httpContext = MockHttpContext.builder()
                .withPath("/users/1")
                .withRoute(Routes.get("/users/{id}"))
                .build();

        //SetUp
        when(userRepository.byId(1)).thenReturn(Optional.empty());


        //When
        Maybe<UserRepresentation> result = getUser.handle(httpContext);

        //Then
        assertThatThrownBy(result::blockingGet)
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    public void user_should_be_retrieved(){

        //Given
        HttpContext httpContext = MockHttpContext.builder()
                .withPath("/users/1")
                .withRoute(Routes.get("/users/{id}"))
                .build();

        //SetUp
        User user = UserBuilder.anUser();
        when(userRepository.byId(1)).thenReturn(Optional.of(user));


        //When
        Maybe<UserRepresentation> result = getUser.handle(httpContext);

        //Then
        assertThat(result.blockingGet())
                .isEqualTo(UserRepresentation.from(user));
    }

}