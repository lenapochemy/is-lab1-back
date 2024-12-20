package org.example.labbb1.controllers;


import org.example.labbb1.exceptions.ForbiddenException;
import org.example.labbb1.exceptions.IncorrectValueException;
import org.example.labbb1.model.*;
import org.example.labbb1.services.ChapterService;
import org.example.labbb1.services.CoordinatesService;
import org.example.labbb1.services.SpaceService;
import org.example.labbb1.services.UserService;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/space")
public class SpaceController {

    private final SpaceService spaceService;
    private final CoordinatesService coordinatesService;
    private final ChapterService chapterService;
    private final UserService userService;

    @Autowired
    public SpaceController(SpaceService spaceService, UserService userService,
                           CoordinatesService coordinatesService, ChapterService chapterService){
        this.spaceService = spaceService;
        this.userService = userService;
        this.coordinatesService = coordinatesService;
        this.chapterService = chapterService;
    }

    @GetMapping("/hello")
    public String getHello(){
        return "Hello";
    }

    @PostMapping("/create")
    public ResponseEntity<?> createNewSpaceMarine(@RequestBody SpaceMarine spaceMarine){
        if (spaceMarine.getName().isEmpty() ||
                spaceMarine.getCoordinates() == null ||
                spaceMarine.getChapter() == null ||
                spaceMarine.getHealth() <= 0 ||
                spaceMarine.getCategory() == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        User user = userService.findUserByToken();
        spaceMarine.setUser(user);
        try{
            spaceMarine.setCreationDate(LocalDateTime.now());
            spaceService.addNewSpaceMarine(spaceMarine);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IncorrectValueException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Too many marines for one chapter, choose another chapter");
        }
    }

//    @GetMapping(value = {"{filter_param}/{filter_value}/{sort_param}/{page}/{size}", "{filter_param}", "{filter_param}/{filter_value}"})
//    public ResponseEntity<?> getPageSpaceMarineByName(@PathVariable String filter_param,
//                                                      @PathVariable(required = false) String filter_value,
//                                                      @PathVariable(required = false) String sort_param,
//                                                      @PathVariable(required = false) Integer page,
//                                                      @PathVariable(required = false) Integer size){
@GetMapping()
public ResponseEntity<?> getPageSpaceMarineByName(String filter_param, String filter_value, String sort_param,
                Integer page, Integer size){
        Iterable<SpaceMarine> spaceMarines;
        switch (filter_param){
            case "name":{
                spaceMarines = spaceService.getPageSpaceMarineByName(sort_param, page, size, filter_value);
                break;
            }
            case "coord":{
                Coordinates coord = coordinatesService.getCoordById(Long.parseLong(filter_value));
                spaceMarines = spaceService.getPageSpaceMarineByCoordinates(sort_param, page, size, coord);
                break;
            }
            case "chapter":{
                Chapter chapter = chapterService.getChapterById(Long.parseLong(filter_value));
                spaceMarines = spaceService.getPageSpaceMarineByChapter(sort_param, page, size, chapter);
                break;
            }
            case "health": {
                Long health = Long.parseLong(filter_value);
                spaceMarines = spaceService.getPageSpaceMarineByHealth(sort_param, page, size, health);
                break;
            }
            case "user":{
                User user = userService.findUserByToken();
                spaceMarines = spaceService.getAllSpaceMarineByUser(user);
                if(filter_value != null){
                    List<Long> spaceMarinesId = new ArrayList<>();
                    spaceMarines.forEach(spaceMarine -> spaceMarinesId.add(spaceMarine.getId()));
                    return ResponseEntity.ok(spaceMarinesId);
                }
                break;
            }
            case "all": {
                if(size == null) {
                    spaceMarines = spaceService.getAllSpaceMarine();
                } else
                    spaceMarines = spaceService.getPageSpaceMarine(sort_param, page, size);
                break;
            }
            default:{
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }
        attributeToNull(spaceMarines);
        return ResponseEntity.ok(spaceMarines);
    }



    @PostMapping("/update")
    public ResponseEntity<?> updateSpaceMarine(@RequestBody SpaceMarine spaceMarine){
        SpaceMarine spaceMarine1 = spaceService.getSpaceMarine(spaceMarine.getId());
        if (spaceMarine.getName().isEmpty() ||
                spaceMarine.getCoordinates() == null ||
                spaceMarine.getChapter() == null ||
                spaceMarine.getHealth() <= 0 ||
                spaceMarine.getCategory() == null || spaceMarine1 == null ){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try{
            spaceMarine.setCreationDate(LocalDateTime.now());
            User user = userService.findUserByToken();
            if(spaceService.updateSpaceMarine(spaceMarine, user)) {
                return new ResponseEntity<>(HttpStatus.OK);
            } else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (PSQLException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (ForbiddenException e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSpaceMarine(@PathVariable Long id){
        User user = userService.findUserByToken();
        try {
            if (spaceService.deleteSpaceMarine(id, user)) {
                return new ResponseEntity<>(HttpStatus.OK);
            } else
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (ForbiddenException e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    private void attributeToNull(Iterable<SpaceMarine> spaceMarines) {
        if (spaceMarines != null) {
            spaceMarines.forEach(spaceMarine -> {
                spaceMarine.getCoordinates().setSpaceMarines(null);
                spaceMarine.getCoordinates().setUser(null);
                spaceMarine.getChapter().setSpaceMarines(null);
                spaceMarine.getChapter().setUser(null);
                spaceMarine.getUser().setChapters(null);
                spaceMarine.getUser().setCoordinates(null);
                spaceMarine.getUser().setSpaceMarines(null);
                spaceMarine.getUser().setId(null);
                spaceMarine.getUser().setPassword(null);
                spaceMarine.getUser().setImports(null);
                spaceMarine.setEditSpaceMarines(null);
                spaceMarine.getCoordinates().setEditCoordinates(null);
                spaceMarine.getChapter().setEditChapters(null);
            });
        }
    }

}
