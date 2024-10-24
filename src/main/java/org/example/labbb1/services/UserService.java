package org.example.labbb1.services;


import org.example.labbb1.model.User;
import org.example.labbb1.repositories.UserRepository;
import org.example.labbb1.utils.PasswordHasher;
import org.example.labbb1.utils.TokenHasher;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;
    private final TokenHasher tokenHasher;

    @Autowired
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
        this.passwordHasher = new PasswordHasher();
        this.tokenHasher = new TokenHasher();
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

    public String logIn(User user){
        User user1 = userRepository.findByLogin(user.getLogin());
        if(user1 == null){
            return null;
        }
        if(user1.getPassword().equals(passwordHasher.hashPassword(user.getPassword()))){
            return tokenHasher.generateToken(user1);
        } else return null;
    }

    public boolean verifyToken(String token){
        return tokenHasher.verifyToken(token);
    }

    public User findUserByLogin(String login){
        return userRepository.findByLogin(login);
    }

    public User findUserByToken(String token){
        return tokenHasher.decodeToken(token);
    }

//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
//        User user = userRepository.findByUsername(username);
//
//        if(user == null) throw new UsernameNotFoundException("User not found");
//        return user;
//    }
}