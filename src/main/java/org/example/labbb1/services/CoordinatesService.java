package org.example.labbb1.services;

import org.example.labbb1.exceptions.CoordinatesException;
import org.example.labbb1.exceptions.ForbiddenException;
import org.example.labbb1.model.*;
import org.example.labbb1.repositories.CoordinatesRepository;
import org.example.labbb1.repositories.EditCoordinatesRepository;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class CoordinatesService {

    private final CoordinatesRepository coordinatesRepository;
    private final EditCoordinatesRepository editCoordinatesRepository;

    @Autowired
    public CoordinatesService(CoordinatesRepository coordinatesRepository, EditCoordinatesRepository editCoordinatesRepository){
        this.coordinatesRepository = coordinatesRepository;
        this.editCoordinatesRepository = editCoordinatesRepository;
    }

    public Coordinates getCoordById(long id){
        return coordinatesRepository.findById(id);
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ)
    public void addNewCoordinate(Coordinates coordinates) throws CoordinatesException{
        if(coordinates.getX() % 5 != 0) {
            throw new CoordinatesException();
        }
        coordinatesRepository.save(coordinates);
        EditCoordinates editCoordinates = new EditCoordinates();
        editCoordinates.setCoordinates(coordinates);
        editCoordinates.setType(EditType.CREATE);
        editCoordinates.setUser(coordinates.getUser());
        editCoordinates.setDate(LocalDateTime.now());
        editCoordinatesRepository.save(editCoordinates);
    }

    public boolean updateCoordinate(Coordinates coordinates, User user) throws PSQLException, ForbiddenException{
        var coord = coordinatesRepository.findById(coordinates.getId());
        if(coord.isPresent()) {
            Coordinates coordinates1 = coord.get();
            if(user.getRole().equals(UserRole.APPROVED_ADMIN) ||
                    coordinates1.getUser().getId().equals(user.getId())) {
                coordinates1.setX(coordinates.getX());
                coordinates1.setY(coordinates.getY());
                coordinatesRepository.save(coordinates1);
                EditCoordinates editCoordinates = new EditCoordinates();
                editCoordinates.setCoordinates(coordinates1);
                editCoordinates.setType(EditType.UPDATE);
                editCoordinates.setUser(user);
                editCoordinates.setDate(LocalDateTime.now());
                editCoordinatesRepository.save(editCoordinates);
                return true;
            } else throw new ForbiddenException();
        } else return false;
    }


    public Iterable<Coordinates> getAllCoordinatesByUser(User user){
        if(user.getRole().equals(UserRole.APPROVED_ADMIN)){
            return getAllCoordinates();
        }
        return coordinatesRepository.findAllByUser(user);
    }

    public Iterable<Coordinates> getAllCoordinates(){
        return coordinatesRepository.findAll();
    }

    public Iterable<Coordinates> getPageCoordinates(String sortParam, int page, int size){
        Sort sort = Sort.by(Sort.Direction.ASC, sortParam);
        Pageable pageable = PageRequest.of(page, size, sort);
        return coordinatesRepository.findAll(pageable);
    }

    public Iterable<Coordinates> getPageCoordinatesByX(String sortParam, int page, int size, Integer x){
        Sort sort = Sort.by(Sort.Direction.ASC, sortParam);
        Pageable pageable = PageRequest.of(page, size, sort);
        return coordinatesRepository.findAllByX(pageable, x);
    }

    public Iterable<Coordinates> getPageCoordinatesByY(String sortParam, int page, int size, Float y){
        Sort sort = Sort.by(Sort.Direction.ASC, sortParam);
        Pageable pageable = PageRequest.of(page, size, sort);
        return coordinatesRepository.findAllByY(pageable, y);
    }

    public boolean deleteCoord(Long id, User user) throws ForbiddenException{
        var coord = coordinatesRepository.findById(id);
        if(coord.isPresent()) {
            Coordinates coordinates = coord.get();
            if(user.getRole().equals(UserRole.APPROVED_ADMIN) ||
                    coordinates.getUser().getId().equals(user.getId())) {
                coordinatesRepository.deleteById(id);
                return true;
            } else throw new ForbiddenException();
        } else return false;
    }

}
