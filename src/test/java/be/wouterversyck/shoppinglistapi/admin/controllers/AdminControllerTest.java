package be.wouterversyck.shoppinglistapi.admin.controllers;

import be.wouterversyck.shoppinglistapi.users.models.SecureUserView;
import be.wouterversyck.shoppinglistapi.users.services.UserService;
import be.wouterversyck.shoppinglistapi.users.testmodels.SecureUserImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminControllerTest {

    private static final String USERNAME_1 = "USERNAME_1";
    private static final String USERNAME_2 = "USERNAME_2";

    @Mock
    private UserService userService;

    @InjectMocks
    private AdminController adminController;

    @Test
    public void shouldReturnUsersPage_WhenControllerMethodIsCalled() {
        ;
        when(userService.getAllUsers(PageRequest.of(0, 1)))
                .thenReturn(createUserPage());

        Page<SecureUserView> result = adminController.getUsers(0, 1);

        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getContent())
                .extracting("username")
                .contains(USERNAME_1, USERNAME_2);
    }

    private Page<SecureUserView> createUserPage() {
        return new PageImpl<>(List.of(
                SecureUserImpl.builder()
                    .username(USERNAME_1).build(),
                SecureUserImpl.builder()
                    .username(USERNAME_2).build()
        ));
    }
}