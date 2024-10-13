package org.example.labbb1.controllers;


import org.example.labbb1.model.*;
import org.example.labbb1.services.SpaceService;
import org.example.labbb1.services.UserService;
import org.example.labbb1.model.AstartesCategory;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Date;

@RestController
@CrossOrigin
@RequestMapping("/space")
public class SpaceController {

    private final SpaceService spaceService;
    private final UserService userService;

    @Autowired
    public SpaceController(SpaceService spaceService, UserService userService){
        this.spaceService = spaceService;
        this.userService = userService;
    }


    @GetMapping("getSpaceMarine")
    public Iterable<SpaceMarine> getAll(){
        return spaceService.getAllSpaceMarine();
    }


    @GetMapping("/hello")
    public String getHello(){
//        Coordinates coord = new Coordinates(2l, 3, 7.0f);
//        Chapter chapter = new Chapter(2l, "dsdd", "mar");
//        LocalDateTime dateTime = LocalDateTime.now();
//        User user = userService.findUserByLogin("ee");
//        SpaceMarine spaceMarine = new SpaceMarine(1l, "lena", coord, dateTime, chapter, 233l, AstartesCategory.SCOUT, Weapon.BOLTGUN, MeleeWeapon.CHAIN_AXE, user);
//        spaceService.addNewSpaceMarine(spaceMarine);
        return "Hello";
    }

    @PostMapping("/addSpaceMarine")
    public ResponseEntity<?> createNewSpaceMarine(@RequestBody SpaceMarine spaceMarine){
        spaceMarine.setCreationDate(LocalDateTime.now());
//        User user = userService.findUserByLogin("ee");
//        spaceMarine.setUser(user);
        spaceService.addNewSpaceMarine(spaceMarine);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/getCoord")
    public Iterable<Coordinates> getAllCoordinates(){
        return spaceService.getAllCoordinates();
    }

    @GetMapping("/getChapter")
    public Iterable<Chapter> getAllChapters(){
        return spaceService.getAllChapters();
    }

    @PostMapping("/newCoord")
    public ResponseEntity<?> addNewCoordination(@RequestBody Coordinates coordinates){
        try {
            spaceService.addNewCoordination(coordinates);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (PSQLException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/newChapter")
    public ResponseEntity<?> addNewChapter(@RequestBody Chapter chapter){
        spaceService.addNewChapter(chapter);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/chapter/{id}")
    public ResponseEntity<?> deleteChapter(@PathVariable Long id){
        spaceService.deleteChapter(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/coord/{id}")
    public ResponseEntity<?> deleteCoord(@PathVariable Long id){
        spaceService.deleteCoord(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
