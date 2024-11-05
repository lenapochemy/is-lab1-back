package org.example.labbb1.controllers;

import jakarta.annotation.security.RolesAllowed;
import org.example.labbb1.exceptions.*;
import org.example.labbb1.model.User;
import org.example.labbb1.model.UserRole;
import org.example.labbb1.services.UserService;
import org.example.labbb1.utils.TokenHasher;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final TokenHasher tokenHasher;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
        this.tokenHasher = new TokenHasher();
    }


    @PostMapping("/reg")
    public ResponseEntity<?> registration(@RequestBody User user){
//        System.out.println("req query: " + user.getLogin() + " " + user.getPassword());
        try {
             userService.regNewUser(user);
             return new ResponseEntity<>(HttpStatus.OK);
        } catch (LoginAlreadyExistsException e){
            return new ResponseEntity<>("login", HttpStatus.UNAUTHORIZED);
        } catch (PasswordAlreadyExistsException e){
            return new ResponseEntity<>("password", HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/logIn")
    public ResponseEntity<?> logIn(@RequestBody User user) {
        String token = userService.logIn(user);
        if (token != null) {
            return ResponseEntity.ok(token);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }


    @GetMapping("/role")
    public ResponseEntity<?> getRole(){
//        if(token == null || token.isEmpty()){
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//        if(!userService.verifyToken(token)){
//            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
//        }
        User user = userService.findUserByToken();
//        System.out.println(role);
        return ResponseEntity.ok(user.getRole());
    }

    @PostMapping("/becomeAdmin")
    public ResponseEntity<?> becomeAdmin(){
//        if(token == null || token.isEmpty()){
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//        if(!userService.verifyToken(token)){
//            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
//        }
        try {
            User user = userService.findUserByToken();
            if(userService.becomeAdmin(user)){
                return ResponseEntity.ok("Now you are admin!");
            } else {
                return ResponseEntity.ok("Your request will be approved in future");
            }
        } catch (AlreadyAdminException e){
            return ResponseEntity.ok("You are already admin");
        } catch (BadRequestException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RolesAllowed("APPROVED_ADMIN")
    @PostMapping("/admin/approve/{id}")
    public ResponseEntity<?> approveAdmin(@PathVariable Integer id){
//        if(id == null || token == null || token.isEmpty()){
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//        if(!userService.verifyToken(token)){
//            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
//        }
        try{
            User user = userService.findUserByToken();
            userService.approveAdmin(id, user);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (BadRequestException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (ForbiddenException e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @RolesAllowed("APPROVED_ADMIN")
    @GetMapping("/admin")
    public ResponseEntity<?> getWaitingAdmins(){
//        if(token == null || token.isEmpty()){
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//        if(!userService.verifyToken(token)){
//            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
//        }
        List<User> users = userService.getWaitingAdmins();
        attributeToNull(users);
        return ResponseEntity.ok(users);
    }


    private void attributeToNull(List<User> users){
        users.forEach(user -> {
            user.setPassword(null);
            user.setSpaceMarines(null);
            user.setChapters(null);
            user.setCoordinates(null);
        });
    }
}