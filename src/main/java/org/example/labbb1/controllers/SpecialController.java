package org.example.labbb1.controllers;

import org.example.labbb1.dto.ChapterDTO;
import org.example.labbb1.dto.CoordinatesDTO;
import org.example.labbb1.dto.SpaceMarineDTO;
import org.example.labbb1.dto.UserDTO;
import org.example.labbb1.model.Chapter;
import org.example.labbb1.model.MeleeWeapon;
import org.example.labbb1.model.SpaceMarine;
import org.example.labbb1.model.User;
import org.example.labbb1.services.SpecialService;
import org.example.labbb1.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<?> getSpaceMarineWithMinChapter(){
        SpaceMarineDTO spaceMarine = specialService.getSpaceMarineWithMinChapter();
//        attrToNull(spaceMarine);
        return ResponseEntity.ok(spaceMarine);
    }

    @GetMapping("/nameStart/{name}")
    public ResponseEntity<?> getSpaceMarineWithNameStart(@PathVariable String name){
        List<SpaceMarineDTO> spaceMarines = specialService.getSpaceMarinesWithNameStart(name);
//        spaceMarines.forEach(this::attrToNull);
        return ResponseEntity.ok(spaceMarines);
    }


    @GetMapping("/greaterMeleeWeapon/{weapon}")
    public ResponseEntity<?> getSpaceMarineWithGreaterMeleeWeapon(@PathVariable MeleeWeapon weapon){
        List<SpaceMarineDTO> spaceMarines = specialService.getSpaceMarinesWithGreaterMeleeWeapon(weapon);
//        spaceMarines.forEach(this::attrToNull);
        return ResponseEntity.ok(spaceMarines);
    }

    @PostMapping("/newChapter")
    public ResponseEntity<?> createNewChapter(@RequestBody ChapterDTO chapter){
        User user = userService.findUserByToken();
//        System.out.println(chapter.getName());
//        System.out.println(chapter.getParentLegion());
        specialService.saveNewChapter(chapter, user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/spaceMarineToChapter/{id}")
    public ResponseEntity<?> spaceMarineToChapter(@RequestBody SpaceMarineDTO spaceMarine, @PathVariable Long id){
        User user = userService.findUserByToken();
        if(specialService.marineToChapter(spaceMarine, id, user)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

//    private void attrToNull(SpaceMarine spaceMarine){
//        spaceMarine.setUser(null);
//        spaceMarine.getCoordinates().setUser(null);
//        spaceMarine.getCoordinates().setSpaceMarines(null);
//        spaceMarine.getChapter().setUser(null);
//        spaceMarine.getChapter().setSpaceMarines(null);
//        spaceMarine.setEditSpaceMarines(null);
//        spaceMarine.getChapter().setEditChapters(null);
//        spaceMarine.getCoordinates().setEditCoordinates(null);
//    }

}
