package be.wouterversyck.shoppinglistapi.users.services;

import be.wouterversyck.shoppinglistapi.users.exceptions.UserNotFoundException;
import be.wouterversyck.shoppinglistapi.users.models.RoleEntity;
import be.wouterversyck.shoppinglistapi.users.models.SecureUserView;
import be.wouterversyck.shoppinglistapi.users.models.DangerUserView;
import be.wouterversyck.shoppinglistapi.users.models.User;
import be.wouterversyck.shoppinglistapi.users.persistence.RolesDao;
import be.wouterversyck.shoppinglistapi.users.persistence.UserDao;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

public class UserService {
    private UserDao userDao;
    private RolesDao rolesDao;
    private PasswordEncoder passwordEncoder;

    public UserService(final UserDao userDao, final RolesDao rolesDao, final PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.rolesDao = rolesDao;
        this.passwordEncoder = passwordEncoder;
    }

    // only for internal use
    public DangerUserView getSecurityUserByUsername(final String username) throws UserNotFoundException {
        return userDao.findByUsername(username, DangerUserView.class)
                .orElseThrow(() -> new UserNotFoundException(username));
    }

    public SecureUserView getUserByUsername(final String username) throws UserNotFoundException {
        return userDao.findByUsername(username, SecureUserView.class)
                .orElseThrow(() -> new UserNotFoundException(username));
    }

    public Page<SecureUserView> getAllUsers(final Pageable page) {
        return userDao.findAllProjectedBy(page, SecureUserView.class);
    }

    public void addUser(final User user) {
        user.setPassword(generateRandomPassword());
        userDao.save(user);
    }

    @Cacheable(value = "be.wouterversyck.shoppinglistapi.users.role")
    public List<RoleEntity> getRoles() {
        return rolesDao.findAll();
    }

    private String generateRandomPassword() {
        final var string = RandomStringUtils.randomAlphanumeric(8);
        return passwordEncoder.encode(string);
    }
}
