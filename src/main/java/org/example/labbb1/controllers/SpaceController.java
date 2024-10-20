package org.example.labbb1.controllers;


import jakarta.json.JsonArrayBuilder;
import jakarta.json.Json;
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
import java.util.List;

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
        Iterable<SpaceMarine> spaceMarines = spaceService.getAllSpaceMarine();
        spaceMarines.forEach(spaceMarine -> {
            spaceMarine.getCoordinates().setSpaceMarines(null);
            spaceMarine.getChapter().setSpaceMarines(null);
        });

        return spaceMarines;
    }


    @GetMapping("/hello")
    public String getHello(){
        return "Hello";
    }

    @PostMapping("/addSpaceMarine")
    public ResponseEntity<?> createNewSpaceMarine(@RequestBody SpaceMarine spaceMarine){
        if (spaceMarine.getName().isEmpty() ||
                spaceMarine.getCoordinates() == null ||
                spaceMarine.getChapter() == null ||
                spaceMarine.getHealth() <= 0 ||
                spaceMarine.getCategory() == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try{
            spaceMarine.setCreationDate(LocalDateTime.now());
            spaceService.addNewSpaceMarine(spaceMarine);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (PSQLException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/getCoord")
    public Iterable<Coordinates> getAllCoordinates(){
        Iterable<Coordinates> coords = spaceService.getAllCoordinates();
        coords.forEach(coordinates -> {
            coordinates.setSpaceMarines(null);
        });
        return coords;
    }

    @GetMapping("/getChapter")
    public Iterable<Chapter> getAllChapters(){
        Iterable<Chapter> chapters = spaceService.getAllChapters();
        chapters.forEach(chapter -> {
            chapter.setSpaceMarines(null);
        });
        return chapters;
    }

    @PostMapping("/newCoord")
    public ResponseEntity<?> addNewCoordination(@RequestBody Coordinates coordinates){
        if (coordinates.getX() == null || coordinates.getY() == null || coordinates.getX() <= -147){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            spaceService.addNewCoordination(coordinates);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (PSQLException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/newChapter")
    public ResponseEntity<?> addNewChapter(@RequestBody Chapter chapter){
        if(chapter.getName() == null ||chapter.getName().isEmpty()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            spaceService.addNewChapter(chapter);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (PSQLException e){
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
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

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSpaceMarine(@PathVariable Long id){
        spaceService.deleteSpaceMarine(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
