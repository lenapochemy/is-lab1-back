package org.example.labbb1.repositories;

import org.example.labbb1.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

//@Component("userRepository")
@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

    User findByLogin(String login);
}
