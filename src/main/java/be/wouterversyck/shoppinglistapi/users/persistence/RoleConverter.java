package be.wouterversyck.shoppinglistapi.users.persistence;

import be.wouterversyck.shoppinglistapi.users.models.Role;

import javax.persistence.AttributeConverter;

public class RoleConverter implements AttributeConverter<Role, Integer> {

    @Override
    public Integer convertToDatabaseColumn(final Role role) {
        return role.getId();
    }

    @Override
    public Role convertToEntityAttribute(final Integer id) {
        return Role.fromId(id);
    }
}
