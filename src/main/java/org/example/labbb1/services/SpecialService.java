package org.example.labbb1.services;


import org.example.labbb1.model.Chapter;
import org.example.labbb1.model.MeleeWeapon;
import org.example.labbb1.model.SpaceMarine;
import org.example.labbb1.model.User;
import org.example.labbb1.repositories.ChapterRepository;
import org.example.labbb1.repositories.SpaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class SpecialService {

    private final SpaceRepository spaceRepository;
    private final ChapterRepository chapterRepository;


    @Autowired
    public SpecialService(SpaceRepository spaceRepository, ChapterRepository chapterRepository){
        this.spaceRepository = spaceRepository;
        this.chapterRepository = chapterRepository;
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
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

    @Transactional(isolation = Isolation.READ_COMMITTED)
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

    @Transactional(isolation = Isolation.READ_COMMITTED)
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
