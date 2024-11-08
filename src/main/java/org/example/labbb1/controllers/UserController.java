package org.example.labbb1.controllers;

import jakarta.annotation.security.RolesAllowed;
import org.example.labbb1.dto.UserDTO;
import org.example.labbb1.dto.UserWithPasswordDTO;
import org.example.labbb1.exceptions.*;
import org.example.labbb1.model.User;
import org.example.labbb1.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }


    @PostMapping("/reg")
    public ResponseEntity<?> registration(@RequestBody UserWithPasswordDTO user){
        try {
             userService.regNewUser(user);
             return new ResponseEntity<>(HttpStatus.OK);
        } catch (LoginAlreadyExistsException e){
            return new ResponseEntity<>("login", HttpStatus.UNAUTHORIZED);
        } catch (PasswordAlreadyExistsException e){
            return new ResponseEntity<>("password", HttpStatus.UNAUTHORIZED);
        }
    }



    @GetMapping("/role")
    public ResponseEntity<?> getRole(){
        User user = userService.findUserByToken();
        return ResponseEntity.ok(user.getRole());
    }

    @PostMapping("/becomeAdmin")
    public ResponseEntity<?> becomeAdmin(){
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
        List<UserDTO> users = userService.getWaitingAdmins();
        return ResponseEntity.ok(users);
    }


//    private void attributeToNull(List<User> users){
//        users.forEach(user -> {
//            user.setPassword(null);
//            user.setSpaceMarines(null);
//            user.setChapters(null);
//            user.setCoordinates(null);
//        });
//    }
}