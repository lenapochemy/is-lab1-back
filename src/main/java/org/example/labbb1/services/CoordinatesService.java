package org.example.labbb1.services;

import org.example.labbb1.exceptions.ForbiddenException;
import org.example.labbb1.model.Coordinates;
import org.example.labbb1.model.User;
import org.example.labbb1.model.UserRole;
import org.example.labbb1.repositories.CoordinatesRepository;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class CoordinatesService {

    private final CoordinatesRepository coordinatesRepository;

    @Autowired
    public CoordinatesService(CoordinatesRepository coordinatesRepository){
        this.coordinatesRepository = coordinatesRepository;
    }

    public Coordinates getCoordById(long id){
        return coordinatesRepository.findById(id);
    }

    public void addNewCoordinate(Coordinates coordinates) throws PSQLException {
        coordinatesRepository.save(coordinates);
    }

    public boolean updateCoordinate(Coordinates coordinates, User user) throws PSQLException, ForbiddenException{
        var coord = coordinatesRepository.findById(coordinates.getId());
        if(coord.isPresent()) {
            Coordinates coordinates1 = coord.get();
            if(user.getRole().equals(UserRole.APPROVED_ADMIN) ||
                    coordinates1.getUser().getId().equals(user.getId())) {
                coordinatesRepository.save(coordinates);
                return true;
            } else throw new ForbiddenException();
        } else return false;
    }


    public Iterable<Coordinates> getAllCoordinates(){
        return coordinatesRepository.findAll();
    }

    public Iterable<Coordinates> getPageCoordinates(String sortParam, int page){
        Sort sort = Sort.by(Sort.Direction.ASC, sortParam);
        Pageable pageable = PageRequest.of(page, 10, sort);
        return coordinatesRepository.findAll(pageable);
    }

    public Iterable<Coordinates> getPageCoordinatesByX(String sortParam, int page, Integer x){
        Sort sort = Sort.by(Sort.Direction.ASC, sortParam);
        Pageable pageable = PageRequest.of(page, 10, sort);
        return coordinatesRepository.findAllByX(pageable, x);
    }

    public Iterable<Coordinates> getPageCoordinatesByY(String sortParam, int page, Float y){
        Sort sort = Sort.by(Sort.Direction.ASC, sortParam);
        Pageable pageable = PageRequest.of(page, 10, sort);
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
