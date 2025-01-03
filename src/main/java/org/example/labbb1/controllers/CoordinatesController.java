package org.example.labbb1.controllers;

import org.example.labbb1.exceptions.CoordinatesException;
import org.example.labbb1.exceptions.ForbiddenException;
import org.example.labbb1.model.Coordinates;
import org.example.labbb1.model.User;
import org.example.labbb1.services.CoordinatesService;
import org.example.labbb1.services.UserService;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
    public ResponseEntity<?> addNewCoordination(@RequestBody Coordinates coordinates) {
        if (coordinates.getX() == null || coordinates.getY() == null || coordinates.getX() <= -147) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        User user = userService.findUserByToken();

        coordinates.setUser(user);
        try {
            coordinatesService.addNewCoordinate(coordinates);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (CoordinatesException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Coordinates X value should be multiple 5");
        }
    }


    //    @GetMapping(value =  {"/{filter_param}/{filter_value}/{sort_param}/{page}/{size}", "/{filter_param}", "/{filter_param}/{filter_value}"})
//    public ResponseEntity<?> getCoordinatesByY(@PathVariable String filter_param,
//                                               @PathVariable(required = false) String filter_value,
//                                               @PathVariable(required = false) String sort_param,
//                                               @PathVariable(required = false) Integer page,
//                                               @PathVariable(required = false) Integer size){
    @GetMapping
    public ResponseEntity<?> getCoordinatesByY(String filter_param, String filter_value, String sort_param,
                                               Integer page, Integer size) {
        Iterable<Coordinates> coords;
        try {
            switch (filter_param) {
                case "x": {
                    Integer x = Integer.parseInt(filter_value);
                    coords = coordinatesService.getPageCoordinatesByX(sort_param, page, size, x);
                    break;
                }
                case "y": {
                    Float y = Float.parseFloat(filter_value);
                    coords = coordinatesService.getPageCoordinatesByY(sort_param, page, size, y);
                    break;
                }
                case "all": {
                    if (size == null) {
                        coords = coordinatesService.getAllCoordinates();
                    } else
                        coords = coordinatesService.getPageCoordinates(sort_param, page, size);
                    break;
                }
                case "user": {
                    User user = userService.findUserByToken();
                    coords = coordinatesService.getAllCoordinatesByUser(user);
                    if (filter_value != null) {
                        List<Long> coordsId = new ArrayList<>();
                        coords.forEach(coordinates -> coordsId.add(coordinates.getId()));
                        return ResponseEntity.ok(coordsId);
                    }
                    break;
                }
                default: {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
            }
        } catch (NumberFormatException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        attributeToNull(coords);
        return ResponseEntity.ok(coords);
    }


    @PostMapping("/update")
    public ResponseEntity<?> updateCoordination(@RequestBody Coordinates coordinates) {
        if (coordinates.getX() == null || coordinates.getY() == null || coordinates.getX() <= -147) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            User user = userService.findUserByToken();
            if (coordinatesService.updateCoordinate(coordinates, user)) {
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } catch (PSQLException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (ForbiddenException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCoord(@PathVariable Long id) {
        User user = userService.findUserByToken();
        try {
            if (coordinatesService.deleteCoord(id, user)) {
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } catch (ForbiddenException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    private void attributeToNull(Iterable<Coordinates> coords) {
        if (coords != null) {
            coords.forEach(coordinates -> {
                coordinates.setSpaceMarines(null);
                System.out.println(coordinates.getUser().toString());
                coordinates.getUser().setPassword(null);
                coordinates.getUser().setCoordinates(null);
//                coordinates.getUser().setId(null);
                coordinates.getUser().setChapters(null);
                coordinates.getUser().setSpaceMarines(null);
                coordinates.getUser().setImports(null);
                coordinates.setEditCoordinates(null);
            });
        }
    }
}
