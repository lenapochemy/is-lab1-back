package org.example.labbb1.controllers;


import jakarta.json.JsonArrayBuilder;
import jakarta.json.Json;
import org.example.labbb1.exceptions.ForbiddenException;
import org.example.labbb1.model.*;
import org.example.labbb1.services.ChapterService;
import org.example.labbb1.services.CoordinatesService;
import org.example.labbb1.services.SpaceService;
import org.example.labbb1.services.UserService;
import org.example.labbb1.model.AstartesCategory;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
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
    public ResponseEntity<?> createNewSpaceMarine(@RequestBody SpaceMarine spaceMarine,
                                                  @RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        if (spaceMarine.getName().isEmpty() ||
                spaceMarine.getCoordinates() == null ||
                spaceMarine.getChapter() == null ||
                spaceMarine.getHealth() <= 0 ||
                spaceMarine.getCategory() == null || token == null || token.isEmpty()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(!userService.verifyToken(token)){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        User user = userService.findUserByToken(token);
        spaceMarine.setUser(user);
        try{
            spaceMarine.setCreationDate(LocalDateTime.now());
            spaceService.addNewSpaceMarine(spaceMarine);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (PSQLException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{sort}/{page}")
    public ResponseEntity<?> getPageSpaceMarine(@PathVariable String sort, @PathVariable Integer page,
                                                    @RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        if(sort == null || page == null || token == null || token.isEmpty()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(!userService.verifyToken(token)){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        Iterable<SpaceMarine> spaceMarines = spaceService.getPageSpaceMarine(sort, page);
//        spaceMarines.forEach(spaceMarine -> {
//            spaceMarine.getCoordinates().setSpaceMarines(null);
//            spaceMarine.getChapter().setSpaceMarines(null);
//            spaceMarine.getUser().setChapters(null);
//            spaceMarine.getUser().setCoordinates(null);
//            spaceMarine.getUser().setSpaceMarines(null);
//            spaceMarine.getUser().setId(null);
//            spaceMarine.getUser().setPassword(null);
//        });
        attributeToNull(spaceMarines);
        return ResponseEntity.ok(spaceMarines);
    }

    @GetMapping("byName/{name}/{sort}/{page}")
    public ResponseEntity<?> getPageSpaceMarineByName(@PathVariable String name, @PathVariable String sort,
                                                          @PathVariable Integer page, @RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        if(sort == null || page == null || token == null || token.isEmpty()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(!userService.verifyToken(token)){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        Iterable<SpaceMarine> spaceMarines = spaceService.getPageSpaceMarineByName(sort, page, name);
        attributeToNull(spaceMarines);
        return ResponseEntity.ok(spaceMarines);
    }

    @GetMapping("byCoord/{id}/{sort}/{page}")
    public ResponseEntity<?> getPageSpaceMarineByCoord(@PathVariable Long id, @PathVariable String sort,
                                                           @PathVariable Integer page, @RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        if(sort == null || page == null || token == null || token.isEmpty()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(!userService.verifyToken(token)){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        Coordinates coord = coordinatesService.getCoordById(id);
        Iterable<SpaceMarine> spaceMarines = spaceService.getPageSpaceMarineByCoordinates(sort, page, coord);
        attributeToNull(spaceMarines);
        return ResponseEntity.ok(spaceMarines);
    }

    @GetMapping("byDate/{date}/{sort}/{page}")
    public ResponseEntity<?> getPageSpaceMarineByDate(@PathVariable LocalDateTime date, @PathVariable String sort,
                                                          @PathVariable Integer page, @RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        if(sort == null || page == null || token == null || token.isEmpty()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(!userService.verifyToken(token)){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        Iterable<SpaceMarine> spaceMarines = spaceService.getPageSpaceMarineByCreationDate(sort, page, date);
        attributeToNull(spaceMarines);
        return ResponseEntity.ok(spaceMarines);
    }
    @GetMapping("byChapter/{id}/{sort}/{page}")
    public ResponseEntity<?> getPageSpaceMarineByChapter(@PathVariable Long id, @PathVariable String sort,
                                                             @PathVariable Integer page, @RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        if(sort == null || page == null || token == null || token.isEmpty()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(!userService.verifyToken(token)){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        Chapter chapter = chapterService.getChapterById(id);
        Iterable<SpaceMarine> spaceMarines = spaceService.getPageSpaceMarineByChapter(sort, page, chapter);
        attributeToNull(spaceMarines);
        return ResponseEntity.ok(spaceMarines);
    }

    @GetMapping("byHealth/{health}/{sort}/{page}")
    public ResponseEntity<?> getPageSpaceMarineByHealth(@PathVariable Long health, @PathVariable String sort,
                                                            @PathVariable Integer page, @RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        if(sort == null || page == null || token == null || token.isEmpty()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(!userService.verifyToken(token)){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        Iterable<SpaceMarine> spaceMarines = spaceService.getPageSpaceMarineByHealth(sort, page, health);
        attributeToNull(spaceMarines);
        return ResponseEntity.ok(spaceMarines);
    }

    @GetMapping("byCategory/{category}/{sort}/{page}")
    public ResponseEntity<?> getPageSpaceMarineByName(@PathVariable AstartesCategory category, @PathVariable String sort,
                                                          @PathVariable Integer page, @RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        if(sort == null || page == null || token == null || token.isEmpty()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(!userService.verifyToken(token)){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        Iterable<SpaceMarine> spaceMarines = spaceService.getPageSpaceMarineByCategory(sort, page, category);
        attributeToNull(spaceMarines);
        return ResponseEntity.ok(spaceMarines);
    }

    @GetMapping("byWeapon/{weapon}/{sort}/{page}")
    public ResponseEntity<?> getPageSpaceMarineByName(@PathVariable Weapon weapon, @PathVariable String sort,
                                                          @PathVariable Integer page, @RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        if(sort == null || page == null || token == null || token.isEmpty()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(!userService.verifyToken(token)){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        Iterable<SpaceMarine> spaceMarines = spaceService.getPageSpaceMarineByWeaponType(sort, page, weapon);
        attributeToNull(spaceMarines);
        return ResponseEntity.ok(spaceMarines);
    }

    @GetMapping("byMeleeWeapon/{weapon}/{sort}/{page}")
    public ResponseEntity<?> getPageSpaceMarineByName(@PathVariable MeleeWeapon weapon, @PathVariable String sort,
                                                          @PathVariable Integer page, @RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        if(sort == null || page == null || token == null || token.isEmpty()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(!userService.verifyToken(token)){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        Iterable<SpaceMarine> spaceMarines = spaceService.getPageSpaceMarineByMeleeWeapon(sort, page, weapon);
        attributeToNull(spaceMarines);
        return ResponseEntity.ok(spaceMarines);
    }



    @PostMapping("/update")
    public ResponseEntity<?> updateSpaceMarine(@RequestBody SpaceMarine spaceMarine,
                                               @RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        SpaceMarine spaceMarine1 = spaceService.getSpaceMarine(spaceMarine.getId());
        if (spaceMarine.getName().isEmpty() ||
                spaceMarine.getCoordinates() == null ||
                spaceMarine.getChapter() == null ||
                spaceMarine.getHealth() <= 0 ||
                spaceMarine.getCategory() == null || spaceMarine1 == null ||
                token == null || token.isEmpty()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(!userService.verifyToken(token)){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        try{
            spaceMarine.setCreationDate(LocalDateTime.now());
            User user = userService.findUserByToken(token);
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
    public ResponseEntity<?> deleteSpaceMarine(@PathVariable Long id, @RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        if(token == null || token.isEmpty()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(!userService.verifyToken(token)){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        User user = userService.findUserByToken(token);
        try {
            if (spaceService.deleteSpaceMarine(id, user)) {
                return new ResponseEntity<>(HttpStatus.OK);
            } else
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (ForbiddenException e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    private void attributeToNull(Iterable<SpaceMarine> spaceMarines){
        spaceMarines.forEach(spaceMarine -> {
            spaceMarine.getCoordinates().setSpaceMarines(null);
            spaceMarine.getChapter().setSpaceMarines(null);
            spaceMarine.getUser().setChapters(null);
            spaceMarine.getUser().setCoordinates(null);
            spaceMarine.getUser().setSpaceMarines(null);
            spaceMarine.getUser().setId(null);
            spaceMarine.getUser().setPassword(null);
        });
    }


}
