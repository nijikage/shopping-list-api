package be.wouterversyck.shoppinglistapi.users.testmodels;

import be.wouterversyck.shoppinglistapi.users.models.Role;
import be.wouterversyck.shoppinglistapi.users.models.SecureUserView;
import lombok.Builder;

@Builder
public class SecureUserImpl implements SecureUserView {

    private long id;
    private String username;
    private String email;
    private Role role;

    @Override
    public long getId() {
        return id;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public Role getRole() {
        return role;
    }

    @Override
    public String getEmail() {
        return email;
    }
}
