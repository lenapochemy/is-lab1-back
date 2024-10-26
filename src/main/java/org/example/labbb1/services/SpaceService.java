package org.example.labbb1.services;

import jakarta.transaction.Transactional;
import org.example.labbb1.exceptions.ForbiddenException;
import org.example.labbb1.model.*;
import org.example.labbb1.repositories.ChapterRepository;
import org.example.labbb1.repositories.CoordinatesRepository;
import org.example.labbb1.repositories.EditSpaceMarineRepository;
import org.example.labbb1.repositories.SpaceRepository;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

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
                spaceMarine.setUser(spaceMarine1.getUser());
                spaceRepository.save(spaceMarine);
                EditSpaceMarine editSpaceMarine = new EditSpaceMarine();
                editSpaceMarine.setSpaceMarine(spaceMarine);
                editSpaceMarine.setType(EditType.UPDATE);
                editSpaceMarine.setUser(spaceMarine.getUser());
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


    public Iterable<SpaceMarine> getPageSpaceMarine(String sortParam, int page){
        Sort sort = Sort.by(Sort.Direction.ASC, sortParam);
        Pageable pageable = PageRequest.of(page, 10, sort);
        return spaceRepository.findAll(pageable);
    }

    public Iterable<SpaceMarine> getAllSpaceMarineByUser(User user){
        return spaceRepository.findAllByUser(user);
    }

    public Iterable<SpaceMarine> getAllSpaceMarine(){
        return spaceRepository.findAll();
    }

    public Iterable<SpaceMarine> getPageSpaceMarineByName(String sortParam, int page, String name){
        Sort sort = Sort.by(Sort.Direction.ASC, sortParam);
        Pageable pageable = PageRequest.of(page, 10, sort);
        return spaceRepository.findAllByName(pageable, name);
    }

    public Iterable<SpaceMarine> getPageSpaceMarineByCoordinates(String sortParam, int page, Coordinates coordinates){
        Sort sort = Sort.by(Sort.Direction.ASC, sortParam);
        Pageable pageable = PageRequest.of(page, 10, sort);
        return spaceRepository.findAllByCoordinates(pageable, coordinates);
    }
    public Iterable<SpaceMarine> getPageSpaceMarineByCreationDate(String sortParam, int page, LocalDateTime creationDate){
        Sort sort = Sort.by(Sort.Direction.ASC, sortParam);
        Pageable pageable = PageRequest.of(page, 10, sort);
        return spaceRepository.findAllByCreationDate(pageable, creationDate);
    }

    public Iterable<SpaceMarine> getPageSpaceMarineByChapter(String sortParam, int page, Chapter chapter){
        Sort sort = Sort.by(Sort.Direction.ASC, sortParam);
        Pageable pageable = PageRequest.of(page, 10, sort);
        return spaceRepository.findAllByChapter(pageable, chapter);
    }

    public Iterable<SpaceMarine> getPageSpaceMarineByHealth(String sortParam, int page, Long health){
        Sort sort = Sort.by(Sort.Direction.ASC, sortParam);
        Pageable pageable = PageRequest.of(page, 10, sort);
        return spaceRepository.findAllByHealth(pageable, health);
    }

    public Iterable<SpaceMarine> getPageSpaceMarineByCategory(String sortParam, int page, AstartesCategory category){
        Sort sort = Sort.by(Sort.Direction.ASC, sortParam);
        Pageable pageable = PageRequest.of(page, 10, sort);
        return spaceRepository.findAllByCategory(pageable, category);
    }

    public Iterable<SpaceMarine> getPageSpaceMarineByWeaponType(String sortParam, int page, Weapon weaponType){
        Sort sort = Sort.by(Sort.Direction.ASC, sortParam);
        Pageable pageable = PageRequest.of(page, 10, sort);
        return spaceRepository.findAllByWeaponType(pageable, weaponType);
    }
    public Iterable<SpaceMarine> getPageSpaceMarineByMeleeWeapon(String sortParam, int page, MeleeWeapon meleeWeapon){
        Sort sort = Sort.by(Sort.Direction.ASC, sortParam);
        Pageable pageable = PageRequest.of(page, 10, sort);
        return spaceRepository.findAllByMeleeWeapon(pageable, meleeWeapon);
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
