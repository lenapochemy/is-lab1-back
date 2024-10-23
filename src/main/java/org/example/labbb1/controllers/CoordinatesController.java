package org.example.labbb1.controllers;

import org.example.labbb1.model.Coordinates;
import org.example.labbb1.services.ChapterService;
import org.example.labbb1.services.CoordinatesService;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/space/coord")
public class CoordinatesController {

    private final CoordinatesService coordinatesService;

    @Autowired
    public CoordinatesController(CoordinatesService coordinatesService) {
        this.coordinatesService = coordinatesService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> addNewCoordination(@RequestBody Coordinates coordinates){
        if (coordinates.getX() == null || coordinates.getY() == null || coordinates.getX() <= -147){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            coordinatesService.addNewCoordinate(coordinates);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (PSQLException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping()
    public Iterable<Coordinates> getAllCoordinates(){
        Iterable<Coordinates> coords = coordinatesService.getAllCoordinates();
        coords.forEach(coordinates -> {
            coordinates.setSpaceMarines(null);
        });
        return coords;
    }

    @GetMapping("/{sort}/{page}")
    public Iterable<Coordinates> getCoordinates(@PathVariable String sort, @PathVariable int page){
        Iterable<Coordinates> coords = coordinatesService.getPageCoordinates(sort, page);
        coords.forEach(coordinates -> {
            coordinates.setSpaceMarines(null);
        });
        return coords;
    }

    @GetMapping("/byX/{x}/{sort}/{page}")
    public Iterable<Coordinates> getCoordinatesByX( @PathVariable Integer x, @PathVariable String sort, @PathVariable int page){
        Iterable<Coordinates> coords = coordinatesService.getPageCoordinatesByX(sort, page, x);
        coords.forEach(coordinates -> {
            coordinates.setSpaceMarines(null);
        });
        return coords;
    }

    @GetMapping("/byY/{y}/{sort}/{page}")
    public Iterable<Coordinates> getCoordinatesByY(@PathVariable Float y, @PathVariable String sort, @PathVariable int page){
        Iterable<Coordinates> coords = coordinatesService.getPageCoordinatesByY(sort, page, y);
        coords.forEach(coordinates -> {
            coordinates.setSpaceMarines(null);
        });
        return coords;
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateCoordination(@RequestBody Coordinates coordinates){
        Coordinates coordinates1 = coordinatesService.getCoordById(coordinates.getId());
        if (coordinates.getX() == null || coordinates.getY() == null || coordinates.getX() <= -147 || coordinates1 == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            coordinatesService.updateCoordinate(coordinates);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (PSQLException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCoord(@PathVariable Long id){
        coordinatesService.deleteCoord(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
