package org.example.labbb1.services;

import org.example.labbb1.exceptions.ForbiddenException;
import org.example.labbb1.model.Chapter;
import org.example.labbb1.model.User;
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
    public boolean updateChapter(Chapter chapter, User user) throws PSQLException, ForbiddenException{
        var chap = chapterRepository.findById(chapter.getId());
        if(chap.isPresent()) {
            Chapter chapter1 = chap.get();
            if(chapter1.getUser().getId().equals(user.getId())) {
                chapterRepository.save(chapter);
                return true;
            } else throw new ForbiddenException();
        } else return false;
    }

    public Iterable<Chapter> getAllChapters(){
        return chapterRepository.findAll();
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


    public boolean deleteChapter(Long id, User user) throws ForbiddenException{
        var chap = chapterRepository.findById(id);
        if(chap.isPresent()) {
            Chapter chapter = chap.get();
            if(chapter.getUser().getId().equals(user.getId())) {
                chapterRepository.deleteById(id);
                return true;
            } else throw new ForbiddenException();
        } else return false;
    }

}
