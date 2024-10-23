package org.example.labbb1.controllers;


import jakarta.json.JsonArrayBuilder;
import jakarta.json.Json;
import org.example.labbb1.model.*;
import org.example.labbb1.services.ChapterService;
import org.example.labbb1.services.CoordinatesService;
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
        try{
            spaceMarine.setCreationDate(LocalDateTime.now());
            spaceService.addNewSpaceMarine(spaceMarine);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (PSQLException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{sort}/{page}")
    public Iterable<SpaceMarine> getPageSpaceMarine(@PathVariable String sort, @PathVariable int page){
        Iterable<SpaceMarine> spaceMarines = spaceService.getPageSpaceMarine(sort, page);
        spaceMarines.forEach(spaceMarine -> {
            spaceMarine.getCoordinates().setSpaceMarines(null);
            spaceMarine.getChapter().setSpaceMarines(null);
        });
        return spaceMarines;
    }

    @GetMapping("byName/{name}/{sort}/{page}")
    public Iterable<SpaceMarine> getPageSpaceMarineByName(@PathVariable String name, @PathVariable String sort, @PathVariable int page){
        Iterable<SpaceMarine> spaceMarines = spaceService.getPageSpaceMarineByName(sort, page, name);
        spaceMarines.forEach(spaceMarine -> {
            spaceMarine.getCoordinates().setSpaceMarines(null);
            spaceMarine.getChapter().setSpaceMarines(null);
        });
        return spaceMarines;
    }

    @GetMapping("byCoord/{id}/{sort}/{page}")
    public Iterable<SpaceMarine> getPageSpaceMarineByCoord(@PathVariable Long id, @PathVariable String sort, @PathVariable int page){
        Coordinates coord = coordinatesService.getCoordById(id);
        Iterable<SpaceMarine> spaceMarines = spaceService.getPageSpaceMarineByCoordinates(sort, page, coord);
        spaceMarines.forEach(spaceMarine -> {
            spaceMarine.getCoordinates().setSpaceMarines(null);
            spaceMarine.getChapter().setSpaceMarines(null);
        });
        return spaceMarines;
    }

    @GetMapping("byDate/{date}/{sort}/{page}")
    public Iterable<SpaceMarine> getPageSpaceMarineByDate(@PathVariable LocalDateTime date, @PathVariable String sort, @PathVariable int page){
        Iterable<SpaceMarine> spaceMarines = spaceService.getPageSpaceMarineByCreationDate(sort, page, date);
        spaceMarines.forEach(spaceMarine -> {
            spaceMarine.getCoordinates().setSpaceMarines(null);
            spaceMarine.getChapter().setSpaceMarines(null);
        });
        return spaceMarines;
    }
    @GetMapping("byChapter/{id}/{sort}/{page}")
    public Iterable<SpaceMarine> getPageSpaceMarineByChapter(@PathVariable Long id, @PathVariable String sort, @PathVariable int page){
        Chapter chapter = chapterService.getChapterById(id);
        Iterable<SpaceMarine> spaceMarines = spaceService.getPageSpaceMarineByChapter(sort, page, chapter);
        spaceMarines.forEach(spaceMarine -> {
            spaceMarine.getCoordinates().setSpaceMarines(null);
            spaceMarine.getChapter().setSpaceMarines(null);
        });
        return spaceMarines;
    }

    @GetMapping("byHealth/{health}/{sort}/{page}")
    public Iterable<SpaceMarine> getPageSpaceMarineByHealth(@PathVariable Long health, @PathVariable String sort, @PathVariable int page){
        Iterable<SpaceMarine> spaceMarines = spaceService.getPageSpaceMarineByHealth(sort, page, health);
        spaceMarines.forEach(spaceMarine -> {
            spaceMarine.getCoordinates().setSpaceMarines(null);
            spaceMarine.getChapter().setSpaceMarines(null);
        });
        return spaceMarines;
    }

    @GetMapping("byCategory/{category}/{sort}/{page}")
    public Iterable<SpaceMarine> getPageSpaceMarineByName(@PathVariable AstartesCategory category, @PathVariable String sort, @PathVariable int page){
        Iterable<SpaceMarine> spaceMarines = spaceService.getPageSpaceMarineByCategory(sort, page, category);
        spaceMarines.forEach(spaceMarine -> {
            spaceMarine.getCoordinates().setSpaceMarines(null);
            spaceMarine.getChapter().setSpaceMarines(null);
        });
        return spaceMarines;
    }

    @GetMapping("byWeapon/{weapon}/{sort}/{page}")
    public Iterable<SpaceMarine> getPageSpaceMarineByName(@PathVariable Weapon weapon, @PathVariable String sort, @PathVariable int page){
        Iterable<SpaceMarine> spaceMarines = spaceService.getPageSpaceMarineByWeaponType(sort, page, weapon);
        spaceMarines.forEach(spaceMarine -> {
            spaceMarine.getCoordinates().setSpaceMarines(null);
            spaceMarine.getChapter().setSpaceMarines(null);
        });
        return spaceMarines;
    }

    @GetMapping("byMeleeWeapon/{weapon}/{sort}/{page}")
    public Iterable<SpaceMarine> getPageSpaceMarineByName(@PathVariable MeleeWeapon weapon, @PathVariable String sort, @PathVariable int page){
        Iterable<SpaceMarine> spaceMarines = spaceService.getPageSpaceMarineByMeleeWeapon(sort, page, weapon);
        spaceMarines.forEach(spaceMarine -> {
            spaceMarine.getCoordinates().setSpaceMarines(null);
            spaceMarine.getChapter().setSpaceMarines(null);
        });
        return spaceMarines;
    }



    @PostMapping("/update")
    public ResponseEntity<?> updateSpaceMarine(@RequestBody SpaceMarine spaceMarine){
        SpaceMarine spaceMarine1 = spaceService.getSpaceMarine(spaceMarine.getId());
        if (spaceMarine.getName().isEmpty() ||
                spaceMarine.getCoordinates() == null ||
                spaceMarine.getChapter() == null ||
                spaceMarine.getHealth() <= 0 ||
                spaceMarine.getCategory() == null || spaceMarine1 == null
                ){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try{
            spaceMarine.setCreationDate(LocalDateTime.now());
            spaceService.updateSpaceMarine(spaceMarine);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (PSQLException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSpaceMarine(@PathVariable Long id){
        spaceService.deleteSpaceMarine(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }



}
