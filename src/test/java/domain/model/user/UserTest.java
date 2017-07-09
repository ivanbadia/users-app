package domain.model.user;

import org.junit.Test;

import java.util.Collections;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class UserTest {


    @Test
    public void username_should_be_required_to_create_an_user(){
        assertThatThrownBy(() -> new User(1, null, "password", new HashSet<>()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void password_should_be_required_to_create_an_user(){
        assertThatThrownBy(() -> new User(1, "username", null, new HashSet<>()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void user_should_not_be_authorized_if_the_user_has_not_got_the_role() {
        //Given
        User user = UserBuilder.builder().withRoles(Collections.singleton(Role.ADMIN)).build();

        //When
        boolean authorized = user.isAuthorized(Role.PAGE_1);

        //Then
        assertThat(authorized).isFalse();

    }

    @Test
    public void user_should_be_authorized_if_the_user_has_the_role() {
        //Given
        User user = UserBuilder.builder().withRoles(Collections.singleton(Role.PAGE_1)).build();

        //When
        boolean authorized = user.isAuthorized(Role.PAGE_1);

        //Then
        assertThat(authorized).isTrue();

    }


    @Test
    public void password_validation_should_be_valid_if_the_password_is_equal_to_the_user_password()  {
        //Given
        String password = "password";
        User user = UserBuilder.builder().withPassword(password).build();

        //When
        boolean hasPassword = user.hasPassword(password);

        //Then
        assertThat(hasPassword).isTrue();
    }

    @Test
    public void password_validation_should_be_invalid_if_the_password_is_not_equal_to_the_user_password()  {
        //Given
        String password = "password";
        User user = UserBuilder.builder().withPassword(password).build();

        //When
        boolean hasPassword = user.hasPassword("p");

        //Then
        assertThat(hasPassword).isFalse();
    }

}