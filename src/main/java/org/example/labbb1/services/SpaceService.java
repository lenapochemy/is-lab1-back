package org.example.labbb1.services;

import org.example.labbb1.exceptions.ForbiddenException;
import org.example.labbb1.model.*;
import org.example.labbb1.repositories.EditSpaceMarineRepository;
import org.example.labbb1.repositories.SpaceRepository;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SpaceService {

    private final SpaceRepository spaceRepository;
    private final EditSpaceMarineRepository editSpaceMarineRepository;


    @Autowired
    public SpaceService(SpaceRepository spaceRepository, EditSpaceMarineRepository editSpaceMarineRepository){
        this.spaceRepository = spaceRepository;
        this.editSpaceMarineRepository = editSpaceMarineRepository;
    }


    public void addNewSpaceMarine(SpaceMarine spaceMarine) throws PSQLException{
        spaceRepository.save(spaceMarine);
        EditSpaceMarine editSpaceMarine = new EditSpaceMarine();
        editSpaceMarine.setSpaceMarine(spaceMarine);
        editSpaceMarine.setType(EditType.CREATE);
        editSpaceMarine.setUser(spaceMarine.getUser());
        editSpaceMarine.setDate(LocalDateTime.now());
        editSpaceMarineRepository.save(editSpaceMarine);
    }

    public boolean updateSpaceMarine(SpaceMarine spaceMarine, User user) throws PSQLException, ForbiddenException{
        var marine = spaceRepository.findById(spaceMarine.getId());
        if(marine.isPresent()){
            SpaceMarine spaceMarine1 = marine.get();
            if(user.getRole().equals(UserRole.APPROVED_ADMIN) ||
                    spaceMarine1.getUser().getId().equals(user.getId())){

                spaceMarine1.setName(spaceMarine.getName());
                spaceMarine1.setCoordinates(spaceMarine.getCoordinates());
                spaceMarine1.setChapter(spaceMarine.getChapter());
                spaceMarine1.setHealth(spaceMarine.getHealth());
                spaceMarine1.setCategory(spaceMarine.getCategory());
                spaceMarine1.setWeaponType(spaceMarine.getWeaponType());
                spaceMarine1.setMeleeWeapon(spaceMarine.getMeleeWeapon());

                spaceRepository.save(spaceMarine1);
                EditSpaceMarine editSpaceMarine = new EditSpaceMarine();
                editSpaceMarine.setSpaceMarine(spaceMarine1);
                editSpaceMarine.setType(EditType.UPDATE);
                editSpaceMarine.setUser(user);
                editSpaceMarine.setDate(LocalDateTime.now());
                editSpaceMarineRepository.save(editSpaceMarine);
                return true;
            } else throw new ForbiddenException();
        } else return false;
    }


    public SpaceMarine getSpaceMarine(Long id){
        var marine = spaceRepository.findById(id);
        SpaceMarine spaceMarine = null;
        if(marine.isPresent()){
            spaceMarine = marine.get();
        }
        return spaceMarine;
    }


    public Iterable<SpaceMarine> getPageSpaceMarine(String sortParam, int page, int size){
        Sort sort = Sort.by(Sort.Direction.ASC, sortParam);
        Pageable pageable = PageRequest.of(page, size, sort);
        return spaceRepository.findAll(pageable);
    }

    public Iterable<SpaceMarine> getAllSpaceMarineByUser(User user){
        if(user.getRole().equals(UserRole.APPROVED_ADMIN)){
            return getAllSpaceMarine();
        }
        return spaceRepository.findAllByUser(user);
    }

    public Iterable<SpaceMarine> getAllSpaceMarine(){
        return spaceRepository.findAll();
    }

    public Iterable<SpaceMarine> getPageSpaceMarineByName(String sortParam, int page, int size, String name){
        Sort sort = Sort.by(Sort.Direction.ASC, sortParam);
        Pageable pageable = PageRequest.of(page, size, sort);
        return spaceRepository.findAllByName(pageable, name);
    }

    public Iterable<SpaceMarine> getPageSpaceMarineByCoordinates(String sortParam, int page, int size, Coordinates coordinates){
        Sort sort = Sort.by(Sort.Direction.ASC, sortParam);
        Pageable pageable = PageRequest.of(page, size, sort);
        return spaceRepository.findAllByCoordinates(pageable, coordinates);
    }

    public Iterable<SpaceMarine> getPageSpaceMarineByChapter(String sortParam, int page, int size, Chapter chapter){
        Sort sort = Sort.by(Sort.Direction.ASC, sortParam);
        Pageable pageable = PageRequest.of(page, size, sort);
        return spaceRepository.findAllByChapter(pageable, chapter);
    }

    public Iterable<SpaceMarine> getPageSpaceMarineByHealth(String sortParam, int page, int size, Long health){
        Sort sort = Sort.by(Sort.Direction.ASC, sortParam);
        Pageable pageable = PageRequest.of(page, size, sort);
        return spaceRepository.findAllByHealth(pageable, health);
    }

    public boolean deleteSpaceMarine(Long id, User user) throws ForbiddenException{
        var marine = spaceRepository.findById(id);
        if(marine.isPresent()){
            SpaceMarine spaceMarine1 = marine.get();
            if(user.getRole().equals(UserRole.APPROVED_ADMIN) ||
                    spaceMarine1.getUser().getId().equals(user.getId())){
                spaceRepository.deleteById(id);
                return true;
            } else throw new ForbiddenException();
        } else return false;
    }



}
