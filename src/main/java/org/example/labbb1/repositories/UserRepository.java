package org.example.labbb1.repositories;

import org.example.labbb1.model.User;
import org.example.labbb1.model.UserRole;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

//@Component("userRepository")
@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

    User findByLogin(String login);
    Optional<User> findById(Integer id);

    List<User> findAllByRole(UserRole role);
}
