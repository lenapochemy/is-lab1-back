package org.example.labbb1.services;


import org.example.labbb1.model.Chapter;
import org.example.labbb1.model.MeleeWeapon;
import org.example.labbb1.model.SpaceMarine;
import org.example.labbb1.model.User;
import org.example.labbb1.repositories.ChapterRepository;
import org.example.labbb1.repositories.CoordinatesRepository;
import org.example.labbb1.repositories.SpaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class SpecialService {

    private final SpaceRepository spaceRepository;
    private final ChapterRepository chapterRepository;
    private final CoordinatesRepository coordinatesRepository;

    @Autowired
    public SpecialService(SpaceRepository spaceRepository, ChapterRepository chapterRepository, CoordinatesRepository coordinatesRepository){
        this.spaceRepository = spaceRepository;
        this.chapterRepository = chapterRepository;
        this.coordinatesRepository = coordinatesRepository;
    }

    public SpaceMarine getSpaceMarineWithMinChapter(){
        Long minChapterId = Long.MAX_VALUE;
        SpaceMarine spaceMarine = null;
        Iterable<SpaceMarine> spaceMarineList = spaceRepository.findAll();
        for(SpaceMarine marine : spaceMarineList){
            if(marine.getChapter().getId() < minChapterId) {
                minChapterId = marine.getChapter().getId();
                spaceMarine = marine;
            }
        }
        return spaceMarine;
    }

    public List<SpaceMarine> getSpaceMarinesWithNameStart(String string){
        Iterable<SpaceMarine> spaceMarineList = spaceRepository.findAll();
        List<SpaceMarine> spaceMarines = new ArrayList<>();
        for(SpaceMarine marine : spaceMarineList){
            if(marine.getName().startsWith(string)){
                spaceMarines.add(marine);
            }
        }
        return spaceMarines;
    }

    public List<SpaceMarine> getSpaceMarinesWithGreaterMeleeWeapon(MeleeWeapon meleeWeapon){
        Iterable<SpaceMarine> spaceMarineList = spaceRepository.findAll();
        List<SpaceMarine> spaceMarines = new ArrayList<>();
        for(SpaceMarine marine : spaceMarineList){
            if(marine.getMeleeWeapon() != null) {
                if (marine.getMeleeWeapon().compareTo(meleeWeapon) > 0) {
                    spaceMarines.add(marine);
                }
            }
        }
        return spaceMarines;
    }

    public void saveNewChapter(Chapter chapter, User user){
//        Chapter chapter = new Chapter();
//        chapter.setName(name);
//        chapter.setParentLegion(parentLegion);
        chapter.setUser(user);
        chapterRepository.save(chapter);
    }

    public boolean marineToChapter(SpaceMarine marine, Long chapterId, User user){
        var chapter = chapterRepository.findById(chapterId);
        if(chapter.isPresent()) {
            marine.setChapter(chapter.get());
            marine.setUser(user);
            spaceRepository.save(marine);
            return true;
        } else
            return false;
    }

}
