package application.user.representation;

import domain.model.user.User;
import domain.model.user.UserBuilder;
import org.junit.Test;

import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;

public class UserRepresentationTest {

    @Test
    public void should_create_a_representation_from_domain(){
        //Given
        User user = UserBuilder.anUser();

        //When
        UserRepresentation userRepresentation = UserRepresentation.from(user);

        //Then
        assertThat(userRepresentation)
                .matches(hasSameUsernameAs(user))
                .matches(hasSameRolesAs(user))
                .matches(hasSameIdAs(user));

    }

    private Predicate<UserRepresentation> hasSameIdAs(User user) {
        return u -> u.getId()==user.getId();
    }

    private Predicate<UserRepresentation> hasSameRolesAs(User user) {
        return u -> u.getRoles().equals(user.getRoles());
    }

    private Predicate<UserRepresentation> hasSameUsernameAs(User user) {
        return u -> u.getUsername().equals(user.getUsername());
    }
}
