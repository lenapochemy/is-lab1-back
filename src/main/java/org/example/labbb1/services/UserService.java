package org.example.labbb1.services;


import org.example.labbb1.exceptions.*;
import org.example.labbb1.model.User;
import org.example.labbb1.model.UserRole;
import org.example.labbb1.repositories.UserRepository;
import org.example.labbb1.utils.PasswordHasher;
import org.example.labbb1.utils.TokenHasher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;
    private final TokenHasher tokenHasher;

    @Autowired
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
        this.passwordHasher = new PasswordHasher();
        this.tokenHasher = new TokenHasher();
    }

    public void regNewUser(User user) throws LoginAlreadyExistsException, PasswordAlreadyExistsException{
        User user1 = userRepository.findByLogin(user.getLogin());
        if(user1 != null){
            throw new LoginAlreadyExistsException();
        }
        String hashedPassword = passwordHasher.hashPassword(user.getPassword());
        user1 = userRepository.findByPassword(hashedPassword);
        if(user1 != null){
            throw new PasswordAlreadyExistsException();
        }
        user.setPassword(hashedPassword);
        user.setRole(UserRole.USER);
//        user.setRoles(Collections.singleton(new Role(1L, "ROLE_USER")));
        userRepository.save(user);
//        return true;
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

    public User findUserByToken(){
//        Integer id =  tokenHasher.userIdDecodeToken(token);
//        var user = userRepository.findById(id);
        var caller_name = SecurityContextHolder.getContext().getAuthentication().getName();
        var userNoOpt = userRepository.findByLogin(caller_name);
        var user = userRepository.findById(userNoOpt.getId());
        if(user.isPresent()){
            User user1 =  user.get();
            return user1;
        } else return null;
    }

    public UserRole getRoleByToken(String token){
//        User us = findUserByToken(token);
        var us = userRepository.findById(findUserByToken().getId());
        if(us.isPresent()) {
            User user = us.get();
//            System.out.println(user.toString());
            System.out.println(user.getRole());
            return user.getRole();
        } else
            return null;
    }

    public List<User> findAllAdmins(){
        return userRepository.findAllByRole(UserRole.APPROVED_ADMIN);
    }

    public boolean becomeAdmin(User us) throws BadRequestException, AlreadyAdminException{
//        User us = findUserByToken(token);
        if(us.getRole().equals(UserRole.APPROVED_ADMIN)){
            throw new AlreadyAdminException();
        }
        var userVar = userRepository.findById(us.getId());
        if(userVar.isPresent()) {
            User user = userVar.get();
            List<User> allAdmins = findAllAdmins();
            if (allAdmins.isEmpty()) {
                user.setRole(UserRole.APPROVED_ADMIN);
                userRepository.save(user);
                return true;
            } else {
                user.setRole(UserRole.WAITING_ADMIN);
                userRepository.save(user);
                return false;
            }
        }
        throw new BadRequestException();
    }

    public void approveAdmin(Integer id, User admin) throws ForbiddenException, BadRequestException{
        if(admin.getRole().equals(UserRole.APPROVED_ADMIN)){
            var us = userRepository.findById(id);
            if(us.isPresent()){
                User user = us.get();
                user.setRole(UserRole.APPROVED_ADMIN);
                userRepository.save(user);
            } else throw new BadRequestException();
        } else throw new ForbiddenException();
    }

    public List<User> getWaitingAdmins(){
        return userRepository.findAllByRole(UserRole.WAITING_ADMIN);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByLogin(username);

        if(user == null) throw new UsernameNotFoundException("User not found");
        return user;
    }
}