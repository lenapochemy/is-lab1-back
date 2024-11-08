package org.example.labbb1.services;

import org.example.labbb1.dto.ChapterDTO;
import org.example.labbb1.dto.CoordinatesDTO;
import org.example.labbb1.dto.SpaceMarineDTO;
import org.example.labbb1.dto.UserDTO;
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
import java.util.ArrayList;
import java.util.List;

@Service
public class SpaceService {

    private final SpaceRepository spaceRepository;
    private final EditSpaceMarineRepository editSpaceMarineRepository;


    @Autowired
    public SpaceService(SpaceRepository spaceRepository, EditSpaceMarineRepository editSpaceMarineRepository){
        this.spaceRepository = spaceRepository;
        this.editSpaceMarineRepository = editSpaceMarineRepository;
    }


    public void addNewSpaceMarine(SpaceMarineDTO spaceMarineDTO, User user) throws PSQLException{
//        Coordinates coordinates = new Coordinates();
//        coordinates.setX(spaceMarineDTO.getCoordinatesDTO().getX());
//        coordinates.setY(spaceMarineDTO.getCoordinatesDTO().getY());
//
//        Chapter chapter = new Chapter();
//        chapter.setName(spaceMarineDTO.getChapterDTO().getName());
//        chapter.setParentLegion(spaceMarineDTO.getChapterDTO().getParentLegion());
//
//        SpaceMarine spaceMarine = new SpaceMarine();
//        spaceMarine.setName(spaceMarineDTO.getName());
//        spaceMarine.setCoordinates(coordinates);
//        spaceMarine.setChapter(chapter);
//        spaceMarine.setHealth(spaceMarineDTO.getHealth());
//        spaceMarine.setCategory(spaceMarineDTO.getCategory());
//        spaceMarine.setWeaponType(spaceMarineDTO.getWeaponType());
//        spaceMarine.setMeleeWeapon(spaceMarineDTO.getMeleeWeapon());
//        spaceMarine.setUser(user);
        SpaceMarine spaceMarine = spaceMarineFromDTO(spaceMarineDTO, user);
        spaceMarine.setCreationDate(LocalDateTime.now());
        spaceRepository.save(spaceMarine);
        EditSpaceMarine editSpaceMarine = new EditSpaceMarine();
        editSpaceMarine.setSpaceMarine(spaceMarine);
        editSpaceMarine.setType(EditType.CREATE);
        editSpaceMarine.setUser(spaceMarine.getUser());
        editSpaceMarine.setDate(LocalDateTime.now());
        editSpaceMarineRepository.save(editSpaceMarine);
    }

    public boolean updateSpaceMarine(SpaceMarineDTO spaceMarineDTO, User user) throws PSQLException, ForbiddenException{
        var marine = spaceRepository.findById(spaceMarineDTO.getId());
        if(marine.isPresent()){
            SpaceMarine spaceMarine1 = marine.get();
            if(user.getRole().equals(UserRole.APPROVED_ADMIN) ||
                    spaceMarine1.getUser().getId().equals(user.getId())){

                Coordinates coordinates = new Coordinates();
                coordinates.setX(spaceMarineDTO.getCoordinatesDTO().getX());
                coordinates.setY(spaceMarineDTO.getCoordinatesDTO().getY());
                coordinates.setId(spaceMarineDTO.getCoordinatesDTO().getId());

                Chapter chapter = new Chapter();
                chapter.setId(spaceMarineDTO.getChapterDTO().getId());
                chapter.setName(spaceMarineDTO.getChapterDTO().getName());
                chapter.setParentLegion(spaceMarineDTO.getChapterDTO().getParentLegion());

                spaceMarine1.setName(spaceMarineDTO.getName());
                spaceMarine1.setCoordinates(coordinates);
                spaceMarine1.setChapter(chapter);
                spaceMarine1.setHealth(spaceMarineDTO.getHealth());
                spaceMarine1.setCategory(spaceMarineDTO.getCategory());
                spaceMarine1.setWeaponType(spaceMarineDTO.getWeaponType());
                spaceMarine1.setMeleeWeapon(spaceMarineDTO.getMeleeWeapon());

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

    public Iterable<SpaceMarineDTO> getAllSpaceMarineByUser(User user){
        if(user.getRole().equals(UserRole.APPROVED_ADMIN)){
            return getAllSpaceMarine();
        }
        Iterable<SpaceMarine> spaceMarines = spaceRepository.findAllByUser(user);
        return spaceMarinesToDTOs(spaceMarines);
    }

    public Iterable<SpaceMarineDTO> getAllSpaceMarine(){
        Iterable<SpaceMarine> spaceMarines = spaceRepository.findAll();
        return spaceMarinesToDTOs(spaceMarines);
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


    private SpaceMarine spaceMarineFromDTO(SpaceMarineDTO spaceMarineDTO, User user){
        Coordinates coordinates = new Coordinates();
        coordinates.setId(spaceMarineDTO.getCoordinatesDTO().getId());

        Chapter chapter = new Chapter();
        chapter.setId(spaceMarineDTO.getChapterDTO().getId());

        SpaceMarine spaceMarine = new SpaceMarine();
        spaceMarine.setName(spaceMarineDTO.getName());
        spaceMarine.setCoordinates(coordinates);
        spaceMarine.setChapter(chapter);
        spaceMarine.setHealth(spaceMarineDTO.getHealth());
        spaceMarine.setCategory(spaceMarineDTO.getCategory());
        spaceMarine.setWeaponType(spaceMarineDTO.getWeaponType());
        spaceMarine.setMeleeWeapon(spaceMarineDTO.getMeleeWeapon());
        spaceMarine.setUser(user);

        return spaceMarine;
    }

    private Iterable<SpaceMarineDTO> spaceMarinesToDTOs(Iterable<SpaceMarine> spaceMarines){
        List<SpaceMarineDTO> dtos = new ArrayList<>();
        spaceMarines.forEach(marine -> {
            ChapterDTO chapterDTO = new ChapterDTO(marine.getChapter().getId(), marine.getChapter().getName(),
                    marine.getChapter().getParentLegion(), null);
            CoordinatesDTO coordinatesDTO = new CoordinatesDTO(marine.getCoordinates().getId(), marine.getCoordinates().getX(),
                    marine.getCoordinates().getY(), null);
            UserDTO userDTO = new UserDTO(marine.getUser().getId(), marine.getUser().getLogin());
            dtos.add(new SpaceMarineDTO(marine.getId(), marine.getName(), coordinatesDTO, marine.getCreationDate(),
                    chapterDTO, marine.getHealth(), marine.getCategory(), marine.getWeaponType(), marine.getMeleeWeapon(),
                    userDTO));
        });
        return dtos;
    }


}
