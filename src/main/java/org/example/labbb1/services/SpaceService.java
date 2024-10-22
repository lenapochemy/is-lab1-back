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

    public void addNewSpaceMarine(SpaceMarine spaceMarine) throws PSQLException{
        spaceRepository.save(spaceMarine);
    }

    public void updateSpaceMarine(SpaceMarine spaceMarine) throws PSQLException{
        spaceRepository.save(spaceMarine);
    }
    public Coordinates getCoordById(long id){
        return coordinatesRepository.findById(id);
    }

    public Chapter getChapterById(Long id){
        var chapt =  chapterRepository.findById(id);
        Chapter chapter = null;
        if(chapt.isPresent()){
            chapter = chapt.get();
        }
        return chapter;
    }

    public SpaceMarine getSpaceMarine(Long id){
        var marine = spaceRepository.findById(id);
        SpaceMarine spaceMarine = null;
        if(marine.isPresent()){
            spaceMarine = marine.get();
        }
        return spaceMarine;
    }
    public Iterable<SpaceMarine> getAllSpaceMarine(){
        return spaceRepository.findAll();
    }

    public Iterable<Chapter> getAllChapters(){
        return chapterRepository.findAll();
    }

    public void addNewChapter(Chapter chapter) throws PSQLException{
        chapterRepository.save(chapter);
    }
    public void updateChapter(Chapter chapter) throws PSQLException{
        chapterRepository.save(chapter);
    }


    public void addNewCoordinate(Coordinates coordinates) throws PSQLException {
        coordinatesRepository.save(coordinates);
    }

    public void updateCoordinate(Coordinates coordinates) throws PSQLException{
//        Coordinates oldCoord = coordinatesRepository.findById(coordinates.getId());
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


//    @Transactional
    public void deleteCoord(Long id){
        coordinatesRepository.deleteById(id);
    }


    public void deleteSpaceMarine(Long id){
        spaceRepository.deleteById(id);
    }



}
