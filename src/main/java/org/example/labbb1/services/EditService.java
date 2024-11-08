package org.example.labbb1.services;

import org.example.labbb1.dto.*;
import org.example.labbb1.model.*;
import org.example.labbb1.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

    public Iterable<EditChapterDTO> getEditChapterByChapter(Chapter chapter){
        Iterable<EditChapter> chapters = editChapterRepository.findAllByChapter(chapter);
        List<EditChapterDTO> chapterDTOS = new ArrayList<>();
        chapters.forEach(editChapter -> {
            ChapterDTO chapterDTO = new ChapterDTO(editChapter.getChapter().getId(), editChapter.getChapter().getName(),
                    editChapter.getChapter().getParentLegion(), null);
            UserDTO userDTO = new UserDTO(editChapter.getUser().getId(), editChapter.getUser().getLogin());
            chapterDTOS.add(new EditChapterDTO(editChapter.getId(), chapterDTO, userDTO, editChapter.getType(), editChapter.getDate()));
        });
        return chapterDTOS;
    }

    public Iterable<EditCoordinatesDTO> getEditCoordByCoords(Coordinates coordinates){
        Iterable<EditCoordinates> coords = editCoordinatesRepository.findAllByCoordinates(coordinates);
        List<EditCoordinatesDTO> coordinatesDTOS = new ArrayList<>();
        coords.forEach(editCoordinates -> {
            CoordinatesDTO coordinatesDTO = new CoordinatesDTO(editCoordinates.getCoordinates().getId(), editCoordinates.getCoordinates().getX(),
                    editCoordinates.getCoordinates().getY(), null);
            UserDTO userDTO = new UserDTO(editCoordinates.getUser().getId(), editCoordinates.getUser().getLogin());
            coordinatesDTOS.add(new EditCoordinatesDTO(editCoordinates.getId(), coordinatesDTO, userDTO, editCoordinates.getType(), editCoordinates.getDate()));
        });
        return coordinatesDTOS;
    }

    public Iterable<EditSpaceMarineDTO> getEditSpaceMarineBySPaceMarine(SpaceMarine spaceMarine){
        Iterable<EditSpaceMarine> spaceMarines = editSpaceMarineRepository.findAllBySpaceMarine(spaceMarine);
        List<EditSpaceMarineDTO> editSpaceMarineDTOS = new ArrayList<>();
        spaceMarines.forEach(editSpaceMarine -> {
            CoordinatesDTO coordinatesDTO = new CoordinatesDTO(
                    editSpaceMarine.getSpaceMarine().getCoordinates().getId(), editSpaceMarine.getSpaceMarine().getCoordinates().getX(),
                    editSpaceMarine.getSpaceMarine().getCoordinates().getY(), null);
            ChapterDTO chapterDTO = new ChapterDTO(
                    editSpaceMarine.getSpaceMarine().getChapter().getId(), editSpaceMarine.getSpaceMarine().getChapter().getName(),
                    editSpaceMarine.getSpaceMarine().getChapter().getParentLegion(), null);
            SpaceMarineDTO spaceMarineDTO = new SpaceMarineDTO(
                    editSpaceMarine.getSpaceMarine().getId(), editSpaceMarine.getSpaceMarine().getName(),
                    coordinatesDTO, editSpaceMarine.getSpaceMarine().getCreationDate(), chapterDTO,
                    editSpaceMarine.getSpaceMarine().getHealth(), editSpaceMarine.getSpaceMarine().getCategory(),
                    editSpaceMarine.getSpaceMarine().getWeaponType(), editSpaceMarine.getSpaceMarine().getMeleeWeapon(), null);
            UserDTO userDTO = new UserDTO(editSpaceMarine.getUser().getId(), editSpaceMarine.getUser().getLogin());
            editSpaceMarineDTOS.add(new EditSpaceMarineDTO(editSpaceMarine.getId(), spaceMarineDTO, userDTO,
                    editSpaceMarine.getType(), editSpaceMarine.getDate()));
        });
        return editSpaceMarineDTOS;
    }



}
