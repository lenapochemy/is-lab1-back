package org.example.labbb1.controllers;

import org.example.labbb1.model.Chapter;
import org.example.labbb1.model.MeleeWeapon;
import org.example.labbb1.model.SpaceMarine;
import org.example.labbb1.model.User;
import org.example.labbb1.services.SpecialService;
import org.example.labbb1.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/special")
public class SpecialController {

    private final SpecialService specialService;
    private final UserService userService;

    @Autowired
    public SpecialController(SpecialService specialService, UserService userService){
        this.specialService = specialService;
        this.userService = userService;
    }

    @GetMapping("/minChapter")
    public ResponseEntity<?> getSpaceMarineWithMinChapter(@RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        if(token == null || token.isEmpty()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(!userService.verifyToken(token)){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        SpaceMarine spaceMarine = specialService.getSpaceMarineWithMinChapter();
        attrToNull(spaceMarine);
//        spaceMarine.setUser(null);
//        spaceMarine.getCoordinates().setUser(null);
//        spaceMarine.getCoordinates().setSpaceMarines(null);
//        spaceMarine.getChapter().setUser(null);
//        spaceMarine.getChapter().setSpaceMarines(null);

        return ResponseEntity.ok(spaceMarine);
    }

    @GetMapping("/nameStart/{name}")
    public ResponseEntity<?> getSpaceMarineWithNameStart(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                                         @PathVariable String name){
        if(token == null || token.isEmpty()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(!userService.verifyToken(token)){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        List<SpaceMarine> spaceMarines = specialService.getSpaceMarinesWithNameStart(name);
        spaceMarines.forEach(this::attrToNull);
        return ResponseEntity.ok(spaceMarines);
    }


    @GetMapping("/greaterMeleeWeapon/{weapon}")
    public ResponseEntity<?> getSpaceMarineWithGreaterMeleeWeapon(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                                                  @PathVariable MeleeWeapon weapon){
        if(token == null || token.isEmpty()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(!userService.verifyToken(token)){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        List<SpaceMarine> spaceMarines = specialService.getSpaceMarinesWithGreaterMeleeWeapon(weapon);
        spaceMarines.forEach(this::attrToNull);
        return ResponseEntity.ok(spaceMarines);
    }

    @PostMapping("/newChapter")
    public ResponseEntity<?> createNewChapter(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                                                  @RequestBody Chapter chapter){
        if(chapter == null || token == null || token.isEmpty()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(!userService.verifyToken(token)){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        User user = userService.findUserByToken(token);
        System.out.println(chapter.getName());
        System.out.println(chapter.getParentLegion());
        specialService.saveNewChapter(chapter, user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/spaceMarineToChapter/{id}")
    public ResponseEntity<?> spaceMarineToChapter(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                                  @RequestBody SpaceMarine spaceMarine, @PathVariable Long id){
        if(token == null || token.isEmpty()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(!userService.verifyToken(token)){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        User user = userService.findUserByToken(token);
        if(specialService.marineToChapter(spaceMarine, id, user)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    private void attrToNull(SpaceMarine spaceMarine){
        spaceMarine.setUser(null);
        spaceMarine.getCoordinates().setUser(null);
        spaceMarine.getCoordinates().setSpaceMarines(null);
        spaceMarine.getChapter().setUser(null);
        spaceMarine.getChapter().setSpaceMarines(null);
    }
}
