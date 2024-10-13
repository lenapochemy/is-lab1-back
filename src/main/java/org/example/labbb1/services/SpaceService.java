package org.example.labbb1.services;

import jakarta.transaction.Transactional;
import org.example.labbb1.model.Chapter;
import org.example.labbb1.model.Coordinates;
import org.example.labbb1.model.SpaceMarine;
import org.example.labbb1.model.User;
import org.example.labbb1.repositories.ChapterRepository;
import org.example.labbb1.repositories.CoordinatesRepository;
import org.example.labbb1.repositories.SpaceRepository;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
public class SpaceService {

    private final SpaceRepository spaceRepository;
    private final ChapterRepository chapterRepository;
    private final CoordinatesRepository coordinatesRepository;

    @Autowired
    public SpaceService(SpaceRepository spaceRepository, ChapterRepository chapterRepository, CoordinatesRepository coordinatesRepository){
        this.spaceRepository = spaceRepository;
        this.chapterRepository = chapterRepository;
        this.coordinatesRepository = coordinatesRepository;
    }

//    public Chapter findOrAddNewChapter(Chapter chapter){
//        Chapter chapter1 = chapterRepository.
//    }

    public void addNewSpaceMarine(SpaceMarine spaceMarine){
        spaceRepository.save(spaceMarine);
    }

    public Iterable<SpaceMarine> getAllSpaceMarine(){
        return spaceRepository.findAll();
    }

    public Iterable<Chapter> getAllChapters(){
        return chapterRepository.findAll();
    }

    public void addNewChapter(Chapter chapter){
        chapterRepository.save(chapter);
    }

    public void addNewCoordination(Coordinates coordinates) throws PSQLException {
        coordinatesRepository.save(coordinates);
    }

    public Iterable<Coordinates> getAllCoordinates(){
        return coordinatesRepository.findAll();
    }

    public void deleteChapter(Long id){
//        Chapter chapter = new Chapter();
//        chapter.setId(id);
        chapterRepository.deleteById(id);
    }


    @Transactional
    public void deleteCoord(Long id){
//        Optional<Coordinates> coord = coordinatesRepository.findById(id);
////        Coordinates coord = (Coordinates) coordinates;
//        List<SpaceMarine> spaceMarines = coord.getSpaceMarines();
//        spaceRepository.deleteAll(spaceMarines);
//        for(SpaceMarine marine : spaceMarines){
//            coord.removeSpaceMarine(marine);
//        }
//        Coordinates coordinates = coordinatesRepository.findById(id);
//
//        coordinates.getSpaceMarines().forEach(spaceMarine -> {
//            spaceMarine.getCoordinates().remove(coordinates);
//        });
        coordinatesRepository.deleteById(id);
    }

}
