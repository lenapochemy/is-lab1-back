package org.example.labbb1.services;


import org.example.labbb1.exceptions.*;
import org.example.labbb1.model.User;
import org.example.labbb1.model.UserRole;
import org.example.labbb1.repositories.UserRepository;
import org.example.labbb1.utils.PasswordHasher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;

    @Autowired
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
        this.passwordHasher = new PasswordHasher();
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
        userRepository.save(user);
    }

    public User findUserByToken(){
        var caller_name = SecurityContextHolder.getContext().getAuthentication().getName();
        var userNoOpt = userRepository.findByLogin(caller_name);
        var user = userRepository.findById(userNoOpt.getId());
        if(user.isPresent()){
            User user1 =  user.get();
            return user1;
        } else return null;
    }

    public List<User> findAllAdmins(){
        return userRepository.findAllByRole(UserRole.APPROVED_ADMIN);
    }

    public boolean becomeAdmin(User us) throws BadRequestException, AlreadyAdminException{
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

    @Transactional(isolation = Isolation.READ_COMMITTED)
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