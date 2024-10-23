package org.example.labbb1.services;

import jakarta.transaction.Transactional;
import org.example.labbb1.model.*;
import org.example.labbb1.repositories.ChapterRepository;
import org.example.labbb1.repositories.CoordinatesRepository;
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
//    private final ChapterRepository chapterRepository;
//    private final CoordinatesRepository coordinatesRepository;

    @Autowired
    public SpaceService(SpaceRepository spaceRepository, ChapterRepository chapterRepository, CoordinatesRepository coordinatesRepository){
        this.spaceRepository = spaceRepository;
//        this.chapterRepository = chapterRepository;
//        this.coordinatesRepository = coordinatesRepository;
    }


    public void addNewSpaceMarine(SpaceMarine spaceMarine) throws PSQLException{
        spaceRepository.save(spaceMarine);
    }

    public void updateSpaceMarine(SpaceMarine spaceMarine) throws PSQLException{
        spaceRepository.save(spaceMarine);
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

//    public Iterable<Coordinates> getPageCoordinates(String sortParam, int page){
//        Sort sort = Sort.by(Sort.Direction.ASC, sortParam);
//        Pageable pageable = PageRequest.of(page, 10, sort);
//        return coordinatesRepository.findAll(pageable);
//    }
//
//    public Iterable<Coordinates> getPageCoordinatesByX(String sortParam, int page, Integer x){
//        Sort sort = Sort.by(Sort.Direction.ASC, sortParam);
//        Pageable pageable = PageRequest.of(page, 10, sort);
//        return coordinatesRepository.findAllByX(pageable, x);
//    }
//
//    public Iterable<Coordinates> getPageCoordinatesByY(String sortParam, int page, Float y){
//        Sort sort = Sort.by(Sort.Direction.ASC, sortParam);
//        Pageable pageable = PageRequest.of(page, 10, sort);
//        return coordinatesRepository.findAllByY(pageable, y);
//    }
//    @Transactional
//    public void deleteCoord(Long id){
//        coordinatesRepository.deleteById(id);
//    }


    public void deleteSpaceMarine(Long id){
        spaceRepository.deleteById(id);
    }



}
