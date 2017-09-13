package infrastructure.persistence.user;

import domain.model.user.DuplicatedUserException;
import domain.model.user.User;
import domain.model.user.UserBuilder;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


public class InMemoryUserRepositoryTest {
    private static final User USER_1 = UserBuilder.builder().withId(1).withUsername("username1").withPassword("password1").build();
    private static final User USER_2 = UserBuilder.builder().withId(2).withUsername("username2").withPassword("password2").build();
    private InMemoryUserRepository userRepository;


    @Before
    public void initialise(){
        userRepository = new InMemoryUserRepository();
    }

    @Test
    public void should_exist_just_one_instance(){
        //When
        InMemoryUserRepository instance1 = InMemoryUserRepository.instance();
        InMemoryUserRepository instance2 = InMemoryUserRepository.instance();

        //Then
        assertThat(instance1)
                .isSameAs(instance2);
    }


    @Test
    public void should_return_1_as_next_user_id_when_there_are_no_users() {

        //Given a repository without users

        //When
        long id = userRepository.nextId();

        //Then
        assertThat(id)
                .isEqualTo(1);
    }


    @Test
    public void should_return_the_max_id_plus_one_as_next_user_id() {
        //When
        long id1 = userRepository.nextId();
        long id2 = userRepository.nextId();

        //Then
        assertThat(id2)
                .isEqualTo(id1 + 1);
    }


    @Test
    public void should_add_a_new_user(){

        //Given
        User user = anUser();

        //When
        userRepository.add(user);

        //Then
        assertThat(userRepository.all())
                .contains(user);
    }


    @Test
    public void should_return_user_by_id() {
        //SetUp: two users are inserted
        User userWithId1 = USER_1;
        User userWithId2 = USER_2;
        userRepository.add(userWithId1);
        userRepository.add(userWithId2);

        //When
        Optional<User> result = userRepository.byId(userWithId1.getId());

        //Then
        assertThat(result)
                .isPresent()
                .contains(userWithId1);
    }


    @Test
    public void should_remove_user() {
        //SetUp: Insert one user
        User user = anUser();
        userRepository.add(user);

        //When
        userRepository.remove(user);

        //Then
        assertThat(userRepository.all())
                .doesNotContain(user);
    }

    private User anUser() {
        return UserBuilder.builder().build();
    }

    @Test
    public void should_return_user_by_username() {
        //SetUp: Two users are inserted
        User userWithUsername1 = USER_1;
        User userWithUsername2 = USER_2;
        userRepository.add(userWithUsername1);
        userRepository.add(userWithUsername2);

        //When
        Optional<User> result = userRepository.byUsername(userWithUsername1.getUsername());

        //Then
        assertThat(result)
                .isPresent()
                .contains(userWithUsername1);
    }


    @Test
    public void should_return_all_users() {
        //SetUp: Two users are inserted
        User userWithUsername1 = USER_1;
        User userWithUsername2 = USER_2;
        userRepository.add(userWithUsername1);
        userRepository.add(userWithUsername2);

        //When
        Collection<User> users = userRepository.all();

        //Then
        assertThat(users)
                .hasSize(2)
                .contains(USER_1, USER_2);
    }


    @Test
    public void should_fail_if_the_added_user_has_an_already_existing_username() {
        //SetUp: Insert one user
        User user = anUser();
        userRepository.add(user);

        //Then: It fails when we insert the same user again
        assertThatThrownBy(() -> userRepository.add(user))
                .isInstanceOf(DuplicatedUserException.class)
                .hasMessage("The user "+ user.getUsername()+" already exists");

    }

}