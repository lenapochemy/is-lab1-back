package org.example.labbb1.services;

import org.example.labbb1.model.Chapter;
import org.example.labbb1.repositories.ChapterRepository;
import org.example.labbb1.repositories.CoordinatesRepository;
import org.example.labbb1.repositories.SpaceRepository;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ChapterService {

    private final ChapterRepository chapterRepository;

    @Autowired
    public ChapterService(ChapterRepository chapterRepository){
        this.chapterRepository = chapterRepository;
    }


    public Chapter getChapterById(Long id){
        var chapt =  chapterRepository.findById(id);
        Chapter chapter = null;
        if(chapt.isPresent()){
            chapter = chapt.get();
        }
        return chapter;
    }

    public void addNewChapter(Chapter chapter) throws PSQLException {
        chapterRepository.save(chapter);
    }
    public void updateChapter(Chapter chapter) throws PSQLException{
        chapterRepository.save(chapter);
    }

    public Iterable<Chapter> getPageChapters(String sortParam, int page){
        Sort sort = Sort.by(Sort.Direction.ASC, sortParam);
        Pageable pageable = PageRequest.of(page, 10, sort);
        return chapterRepository.findAll(pageable);
    }

    public Iterable<Chapter> getPageChapterByName(String sortParam, int page, String name){
        Sort sort = Sort.by(Sort.Direction.ASC, sortParam);
        Pageable pageable = PageRequest.of(page, 10, sort);
        return chapterRepository.findAllByName(pageable, name);
    }

    public Iterable<Chapter> getPageChapterByParentLegion(String sortParam, int page, String parentLegion){
        Sort sort = Sort.by(Sort.Direction.ASC, sortParam);
        Pageable pageable = PageRequest.of(page, 10, sort);
        return chapterRepository.findAllByParentLegion(pageable, parentLegion);
    }


    public void deleteChapter(Long id){
//        Chapter chapter = new Chapter();
//        chapter.setId(id);
        chapterRepository.deleteById(id);
    }

}
