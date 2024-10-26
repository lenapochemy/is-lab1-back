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

    public Iterable<EditCoordinates> getEditCoordByCoords(Coordinates coordinates){
        return editCoordinatesRepository.findAllByCoordinates(coordinates);
    }

    public Iterable<EditSpaceMarine> getEditSpaceMarineBySPaceMarine(SpaceMarine spaceMarine){
        return editSpaceMarineRepository.findAllBySpaceMarine(spaceMarine);
    }



}
