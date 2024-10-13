package org.example.labbb1.controllers;

import org.example.labbb1.model.User;
import org.example.labbb1.services.UserService;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }


    @PostMapping("/reg")
    public ResponseEntity<?> registration(@RequestBody User user){
        System.out.println("req query: " + user.getLogin() + " " + user.getPassword());

        try {
            if (userService.regNewUser(user)) {
                return new ResponseEntity<>(HttpStatus.OK);
            } else return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (PSQLException e){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/logIn")
    public ResponseEntity<?> logIn(@RequestBody User user){
        if(userService.logIn(user)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }


}