package org.example.labbb1.services;


import org.example.labbb1.dto.ChapterDTO;
import org.example.labbb1.dto.CoordinatesDTO;
import org.example.labbb1.dto.SpaceMarineDTO;
import org.example.labbb1.dto.UserDTO;
import org.example.labbb1.model.Chapter;
import org.example.labbb1.model.MeleeWeapon;
import org.example.labbb1.model.SpaceMarine;
import org.example.labbb1.model.User;
import org.example.labbb1.repositories.ChapterRepository;
import org.example.labbb1.repositories.SpaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public SpaceMarineDTO getSpaceMarineWithMinChapter(){
        Long minChapterId = Long.MAX_VALUE;
        SpaceMarine spaceMarine = null;
        Iterable<SpaceMarine> spaceMarineList = spaceRepository.findAll();
        for(SpaceMarine marine : spaceMarineList){
            if(marine.getChapter().getId() < minChapterId) {
                minChapterId = marine.getChapter().getId();
                spaceMarine = marine;
            }
        }

        return spaceMarineToDTO(spaceMarine);
    }

    public List<SpaceMarineDTO> getSpaceMarinesWithNameStart(String string){
        Iterable<SpaceMarine> spaceMarineList = spaceRepository.findAll();
        List<SpaceMarine> spaceMarines = new ArrayList<>();
        for(SpaceMarine marine : spaceMarineList){
            if(marine.getName().startsWith(string)){
                spaceMarines.add(marine);
            }
        }
        return spaceMarineToDTOs(spaceMarines);
    }

    public List<SpaceMarineDTO> getSpaceMarinesWithGreaterMeleeWeapon(MeleeWeapon meleeWeapon){
        Iterable<SpaceMarine> spaceMarineList = spaceRepository.findAll();
        List<SpaceMarine> spaceMarines = new ArrayList<>();
        for(SpaceMarine marine : spaceMarineList){
            if(marine.getMeleeWeapon() != null) {
                if (marine.getMeleeWeapon().compareTo(meleeWeapon) > 0) {
                    spaceMarines.add(marine);
                }
            }
        }
        return spaceMarineToDTOs(spaceMarines);
    }

    public void saveNewChapter(ChapterDTO chapterDTO, User user){
        Chapter chapter = new Chapter();
        chapter.setName(chapterDTO.getName());
        chapter.setParentLegion(chapterDTO.getParentLegion());
        chapter.setUser(user);
        chapterRepository.save(chapter);
    }

    public boolean marineToChapter(SpaceMarineDTO marine, Long chapterId, User user){
        var chapter = chapterRepository.findById(chapterId);
        if(chapter.isPresent()) {
            var varSpaceMarine = spaceRepository.findById(marine.getId());
            if(varSpaceMarine.isPresent()){
                SpaceMarine spaceMarine = varSpaceMarine.get();
                spaceMarine.setChapter(chapter.get());
                spaceMarine.setUser(user);
                spaceRepository.save(spaceMarine);
            }
            return true;
        } else
            return false;
    }

    private SpaceMarineDTO spaceMarineToDTO(SpaceMarine spaceMarine){
        CoordinatesDTO coordinatesDTO = new CoordinatesDTO(spaceMarine.getCoordinates().getId(),
                spaceMarine.getCoordinates().getX(), spaceMarine.getCoordinates().getY(), null);
        ChapterDTO chapterDTO = new ChapterDTO(spaceMarine.getChapter().getId(),
                spaceMarine.getChapter().getName(), spaceMarine.getChapter().getParentLegion(), null);
        UserDTO userDTO = new UserDTO(spaceMarine.getUser().getId(), spaceMarine.getUser().getLogin());
        return new SpaceMarineDTO(
                spaceMarine.getId(), spaceMarine.getName(), coordinatesDTO, spaceMarine.getCreationDate(),
                chapterDTO, spaceMarine.getHealth(), spaceMarine.getCategory(), spaceMarine.getWeaponType(),
                spaceMarine.getMeleeWeapon(), userDTO
        );
    }

    private List<SpaceMarineDTO> spaceMarineToDTOs(Iterable<SpaceMarine> spaceMarines){
        List<SpaceMarineDTO> spaceMarineDTOS = new ArrayList<>();
        spaceMarines.forEach(marine -> spaceMarineDTOS.add(spaceMarineToDTO(marine)));
        return spaceMarineDTOS;
    }

}
