package application.user.handler;

import application.user.representation.UserRepresentation;
import application.user.representation.UserRepresentationList;
import domain.model.user.User;
import domain.model.user.UserBuilder;
import domain.model.user.UserRepository;
import infrastructure.server.HttpContext;
import infrastructure.server.MockHttpContext;
import io.reactivex.Maybe;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GetUsersTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private GetUsers getUsers;

    @Test
    public void users_should_be_retrieved(){

        //Given
        HttpContext httpContext = MockHttpContext.builder().build();

        //SetUp
        User user = UserBuilder.anUser();
        Set<User> allUsers = Collections.singleton(user);
        when(userRepository.all()).thenReturn(allUsers);

        //When
        Maybe<UserRepresentationList> result = getUsers.handle(httpContext);

        //Then
        List<UserRepresentation> expectedResult = allUsers.stream().map(UserRepresentation::from).collect(Collectors.toList());
        assertThat(result.blockingGet().getUsers())
                .isEqualTo(expectedResult);
    }

}