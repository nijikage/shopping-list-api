package be.wouterversyck.shoppinglistapi.shoppinglist.daos;

import be.wouterversyck.shoppinglistapi.shoppinglist.models.ShoppingList;
import be.wouterversyck.shoppinglistapi.shoppinglist.models.ShoppingListDto;
import be.wouterversyck.shoppinglistapi.users.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ShoppingListDao extends JpaRepository<ShoppingList, Long> {

    List<ShoppingListDto> findAllByOwner(User user);
    Optional<ShoppingListDto> findByIdAndOwner(Long id, User user);
}
