package org.example.labbb1.controllers;

import org.example.labbb1.exceptions.ForbiddenException;
import org.example.labbb1.model.Coordinates;
import org.example.labbb1.model.User;
import org.example.labbb1.services.CoordinatesService;
import org.example.labbb1.services.UserService;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/space/coord")
public class CoordinatesController {

    private final CoordinatesService coordinatesService;
    private final UserService userService;

    @Autowired
    public CoordinatesController(CoordinatesService coordinatesService, UserService userService) {
        this.coordinatesService = coordinatesService;
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> addNewCoordination(@RequestBody Coordinates coordinates,
                                                @RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        if (coordinates.getX() == null || coordinates.getY() == null || coordinates.getX() <= -147 ||
            token == null || token.isEmpty()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(!userService.verifyToken(token)){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        User user = userService.findUserByToken(token);

        coordinates.setUser(user);
        try {
            coordinatesService.addNewCoordinate(coordinates);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (PSQLException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping()
    public ResponseEntity<?> getAllCoordinatesByUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        if(token == null || token.isEmpty()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(!userService.verifyToken(token)){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        User user = userService.findUserByToken(token);
        Iterable<Coordinates> coords = coordinatesService.getAllCoordinatesByUser(user);
        attributeToNull(coords);
        return ResponseEntity.ok(coords);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllCoordinates(@RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        if(token == null || token.isEmpty()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(!userService.verifyToken(token)){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        User user = userService.findUserByToken(token);
        Iterable<Coordinates> coords = coordinatesService.getAllCoordinates();
        attributeToNull(coords);
        return ResponseEntity.ok(coords);
    }

    @GetMapping("/{sort}/{page}")
    public ResponseEntity<?> getCoordinates(@PathVariable String sort, @PathVariable Integer page,
                                                @RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        if(sort == null || page == null || token == null || token.isEmpty()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(!userService.verifyToken(token)){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
//        User user = userService.filndUserByToken(token);
        Iterable<Coordinates> coords = coordinatesService.getPageCoordinates(sort, page);
        attributeToNull(coords);
        return  ResponseEntity.ok(coords);
    }

    @GetMapping("/byX/{x}/{sort}/{page}")
    public ResponseEntity<?> getCoordinatesByX( @PathVariable Integer x, @PathVariable String sort, @PathVariable Integer page,
                                                    @RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        if(sort == null || page == null || token == null || token.isEmpty()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(!userService.verifyToken(token)){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        Iterable<Coordinates> coords = coordinatesService.getPageCoordinatesByX(sort, page, x);
        attributeToNull(coords);
        return ResponseEntity.ok(coords);
    }

    @GetMapping("/byY/{y}/{sort}/{page}")
    public ResponseEntity<?> getCoordinatesByY(@PathVariable Float y, @PathVariable String sort, @PathVariable Integer page,
                                                   @RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        if(sort == null || page == null || token == null || token.isEmpty()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(!userService.verifyToken(token)){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        Iterable<Coordinates> coords = coordinatesService.getPageCoordinatesByY(sort, page, y);
        attributeToNull(coords);
        return ResponseEntity.ok(coords);
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateCoordination(@RequestBody Coordinates coordinates,
                                                @RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        Coordinates coordinates1 = coordinatesService.getCoordById(coordinates.getId());
        if (coordinates.getX() == null || coordinates.getY() == null || coordinates.getX() <= -147 ||
                coordinates1 == null || token == null || token.isEmpty()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(!userService.verifyToken(token)){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        try {
            User user = userService.findUserByToken(token);
            if(coordinatesService.updateCoordinate(coordinates, user)) {
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } catch (PSQLException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (ForbiddenException e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCoord(@PathVariable Long id, @RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        if(token == null || token.isEmpty()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(!userService.verifyToken(token)){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        User user = userService.findUserByToken(token);
        try{
            if(coordinatesService.deleteCoord(id, user)) {
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } catch (ForbiddenException e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    private void attributeToNull(Iterable<Coordinates> coords){
        coords.forEach(coordinates -> {
            coordinates.setSpaceMarines(null);
            coordinates.getUser().setPassword(null);
            coordinates.getUser().setCoordinates(null);
            coordinates.getUser().setId(null);
            coordinates.getUser().setChapters(null);
            coordinates.getUser().setSpaceMarines(null);
        });
    }

}
