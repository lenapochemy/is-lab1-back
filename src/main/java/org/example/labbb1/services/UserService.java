package org.example.labbb1.services;


import org.example.labbb1.model.User;
import org.example.labbb1.repositories.UserRepository;
import org.example.labbb1.utils.PasswordHasher;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;

    @Autowired
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
        this.passwordHasher = new PasswordHasher();
    }

    public boolean regNewUser(User user) throws PSQLException {
        User user1 = userRepository.findByLogin(user.getLogin());
        if(user1 != null){
            return false;
        }
            user.setPassword(passwordHasher.hashPassword(user.getPassword()));
//        user.setRoles(Collections.singleton(new Role(1L, "ROLE_USER")));
        userRepository.save(user);
        return true;
    }

    public boolean logIn(User user){
        User user1 = userRepository.findByLogin(user.getLogin());
        if(user1 == null){
            return false;
        }
        return user1.getPassword().equals(passwordHasher.hashPassword(user.getPassword()));
    }

    public User findUserByLogin(String login){
        return userRepository.findByLogin(login);
    }

//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
//        User user = userRepository.findByUsername(username);
//
//        if(user == null) throw new UsernameNotFoundException("User not found");
//        return user;
//    }
}