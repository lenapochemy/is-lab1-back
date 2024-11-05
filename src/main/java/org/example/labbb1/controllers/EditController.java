package org.example.labbb1.controllers;

import org.example.labbb1.model.*;
import org.example.labbb1.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/edit")
public class EditController {

    private final EditService editService;
    private final ChapterService chapterService;
    private final CoordinatesService coordinatesService;
    private final SpaceService spaceService;

    @Autowired
    public EditController(EditService editService, ChapterService chapterService,
                          CoordinatesService coordinatesService, SpaceService spaceService){
        this.editService = editService;
        this.chapterService = chapterService;
        this.coordinatesService = coordinatesService;
        this.spaceService = spaceService;
    }

    @GetMapping("/chapter/{id}")
    public ResponseEntity<?> getEditChapters(@PathVariable Long id){
        Chapter chapter = chapterService.getChapterById(id);
        Iterable<EditChapter> chapters = editService.getEditChapterByChapter(chapter);
        chapters.forEach(editChapter -> {
            editChapter.getUser().setSpaceMarines(null);
            editChapter.getUser().setCoordinates(null);
            editChapter.getUser().setChapters(null);
            editChapter.getUser().setPassword(null);
            editChapter.getUser().setId(null);
            editChapter.getChapter().setUser(null);
            editChapter.getChapter().setSpaceMarines(null);
            editChapter.getChapter().setEditChapters(null);
        });
        return ResponseEntity.ok(chapters);
    }

    @GetMapping("/coord/{id}")
    public ResponseEntity<?> getEditCoords(@PathVariable Long id){
        Coordinates coordinates = coordinatesService.getCoordById(id);
        Iterable<EditCoordinates> coords = editService.getEditCoordByCoords(coordinates);
        coords.forEach(editCoordinates -> {
            editCoordinates.getUser().setSpaceMarines(null);
            editCoordinates.getUser().setCoordinates(null);
            editCoordinates.getUser().setChapters(null);
            editCoordinates.getUser().setPassword(null);
            editCoordinates.getUser().setId(null);
            editCoordinates.getCoordinates().setUser(null);
            editCoordinates.getCoordinates().setSpaceMarines(null);
            editCoordinates.getCoordinates().setEditCoordinates(null);
        });
        return ResponseEntity.ok(coords);
    }

    @GetMapping("/spaceMarine/{id}")
    public ResponseEntity<?> getEditSpaceMarine(
                                             @PathVariable Long id){
        SpaceMarine spaceMarine = spaceService.getSpaceMarine(id);
        Iterable<EditSpaceMarine> spaceMarines = editService.getEditSpaceMarineBySPaceMarine(spaceMarine);
        spaceMarines.forEach(editSpaceMarine -> {
            editSpaceMarine.getUser().setSpaceMarines(null);
            editSpaceMarine.getUser().setCoordinates(null);
            editSpaceMarine.getUser().setChapters(null);
            editSpaceMarine.getUser().setPassword(null);
            editSpaceMarine.getUser().setId(null);
            editSpaceMarine.getSpaceMarine().setUser(null);
            editSpaceMarine.getSpaceMarine().getCoordinates().setUser(null);
            editSpaceMarine.getSpaceMarine().getCoordinates().setSpaceMarines(null);
            editSpaceMarine.getSpaceMarine().getChapter().setSpaceMarines(null);
            editSpaceMarine.getSpaceMarine().getChapter().setUser(null);
            editSpaceMarine.getSpaceMarine().setEditSpaceMarines(null);
            editSpaceMarine.getSpaceMarine().getChapter().setEditChapters(null);
            editSpaceMarine.getSpaceMarine().getCoordinates().setEditCoordinates(null);
        });

        return ResponseEntity.ok(spaceMarines);
    }


}
