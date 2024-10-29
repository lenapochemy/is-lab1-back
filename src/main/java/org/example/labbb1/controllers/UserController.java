package org.example.labbb1.controllers;

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
@CrossOrigin
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


    @GetMapping("/admin/role")
    public ResponseEntity<?> getRole(@RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        if(token == null || token.isEmpty()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(!userService.verifyToken(token)){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        UserRole role = userService.getRoleByToken(token);
        System.out.println(role);
        return ResponseEntity.ok(role);
    }

    @PostMapping("/admin/become")
    public ResponseEntity<?> becomeAdmin(@RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        if(token == null || token.isEmpty()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(!userService.verifyToken(token)){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        try {
            if(userService.becomeAdmin(token)){
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

    @PostMapping("/admin/approve/{id}")
    public ResponseEntity<?> approveAdmin(@PathVariable Integer id, @RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        if(id == null || token == null || token.isEmpty()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(!userService.verifyToken(token)){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        try{
            userService.approveAdmin(id, token);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (BadRequestException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (ForbiddenException e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/admin")
    public ResponseEntity<?> getWaitingAdmins(@RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        if(token == null || token.isEmpty()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(!userService.verifyToken(token)){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
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