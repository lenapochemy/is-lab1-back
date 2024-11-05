package org.example.labbb1.services;

import org.example.labbb1.model.*;
import org.example.labbb1.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class EditService {

    private final EditChapterRepository editChapterRepository;
    private final EditCoordinatesRepository editCoordinatesRepository;
    private final EditSpaceMarineRepository editSpaceMarineRepository;

    @Autowired
    public EditService(EditChapterRepository editChapterRepository, EditCoordinatesRepository editCoordinatesRepository,
                       EditSpaceMarineRepository editSpaceMarineRepository){
        this.editChapterRepository = editChapterRepository;
        this.editCoordinatesRepository = editCoordinatesRepository;
        this.editSpaceMarineRepository = editSpaceMarineRepository;

    }

    public Iterable<EditChapter> getEditChapterByChapter(Chapter chapter){
        return editChapterRepository.findAllByChapter(chapter);
    }

    public void addNewEditChapter(Chapter chapter, User user, EditType editType){
        EditChapter editChapter = new EditChapter();
        editChapter.setChapter(chapter);
        editChapter.setType(editType);
        editChapter.setUser(user);
        editChapter.setDate(LocalDateTime.now());
        editChapterRepository.save(editChapter);
    }

    public void addNewEditCoord(Coordinates coord, User user, EditType editType){
        EditCoordinates editCoordinates = new EditCoordinates();
        editCoordinates.setCoordinates(coord);
        editCoordinates.setType(editType);
        editCoordinates.setUser(user);
        editCoordinates.setDate(LocalDateTime.now());

    }

    public void addNewEditSpaceMarine(SpaceMarine spaceMarine , User user, EditType editType){
        EditSpaceMarine editSpaceMarine = new EditSpaceMarine();
        editSpaceMarine.setSpaceMarine(spaceMarine);
        editSpaceMarine.setType(editType);
        editSpaceMarine.setUser(user);
        editSpaceMarine.setDate(LocalDateTime.now());
    }


    public Iterable<EditCoordinates> getEditCoordByCoords(Coordinates coordinates){
        return editCoordinatesRepository.findAllByCoordinates(coordinates);
    }


    public Iterable<EditSpaceMarine> getEditSpaceMarineBySPaceMarine(SpaceMarine spaceMarine){
        return editSpaceMarineRepository.findAllBySpaceMarine(spaceMarine);
    }



}
