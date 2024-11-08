package org.example.labbb1.services;

import org.example.labbb1.dto.CoordinatesDTO;
import org.example.labbb1.dto.UserDTO;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    public void addNewCoordinate(CoordinatesDTO coordinatesDTO, User user) throws PSQLException {
        Coordinates coordinates = new Coordinates();
        coordinates.setX(coordinatesDTO.getX());
        coordinates.setY(coordinatesDTO.getY());
        coordinates.setUser(user);

        coordinatesRepository.save(coordinates);

        EditCoordinates editCoordinates = new EditCoordinates();
        editCoordinates.setCoordinates(coordinates);
        editCoordinates.setType(EditType.CREATE);
        editCoordinates.setUser(coordinates.getUser());
        editCoordinates.setDate(LocalDateTime.now());
        editCoordinatesRepository.save(editCoordinates);
    }

    public boolean updateCoordinate(CoordinatesDTO coordinatesDTO, User user) throws PSQLException, ForbiddenException{
        var coord = coordinatesRepository.findById(coordinatesDTO.getId());
        if(coord.isPresent()) {
            Coordinates coordinates1 = coord.get();
            if(user.getRole().equals(UserRole.APPROVED_ADMIN) ||
                    coordinates1.getUser().getId().equals(user.getId())) {
                coordinates1.setX(coordinatesDTO.getX());
                coordinates1.setY(coordinatesDTO.getY());
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


    public Iterable<CoordinatesDTO> getAllCoordinatesByUser(User user){
        if(user.getRole().equals(UserRole.APPROVED_ADMIN)){
            return getAllCoordinates();
        }
        Iterable<Coordinates> coords = coordinatesRepository.findAllByUser(user);
        return coordsToDTOs(coords);
    }

    public Iterable<CoordinatesDTO> getAllCoordinates(){
        Iterable<Coordinates> coords = coordinatesRepository.findAll();
        return coordsToDTOs(coords);
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

    private Iterable<CoordinatesDTO> coordsToDTOs(Iterable<Coordinates> coordinates){
        List<CoordinatesDTO> dtos = new ArrayList<>();
        coordinates.forEach(coord -> dtos.add(new CoordinatesDTO(coord.getId(), coord.getX(), coord.getY(),
                new UserDTO(coord.getUser().getId(), coord.getUser().getLogin()))));
        return dtos;
    }
}
