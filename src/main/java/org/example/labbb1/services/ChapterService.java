package org.example.labbb1.services;

import org.example.labbb1.exceptions.ChapterException;
import org.example.labbb1.exceptions.ForbiddenException;
import org.example.labbb1.model.*;
import org.example.labbb1.repositories.ChapterRepository;
import org.example.labbb1.repositories.EditChapterRepository;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class ChapterService {

    private final ChapterRepository chapterRepository;
    private final EditChapterRepository editChapterRepository;

    @Autowired
    public ChapterService(ChapterRepository chapterRepository, EditChapterRepository editChapterRepository){
        this.chapterRepository = chapterRepository;
        this.editChapterRepository = editChapterRepository;
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Chapter getChapterById(Long id){
        var chapt =  chapterRepository.findById(id);
        Chapter chapter = null;
        if(chapt.isPresent()){
            chapter = chapt.get();
        } else {
            System.out.println("don't have chapter in db");
        }
        return chapter;
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ)
    public void addNewChapter(Chapter chapter) throws ChapterException {
        if(chapter.getParentLegion() != null && !chapter.getParentLegion().startsWith("l")){
            throw new ChapterException();
        } else {
            chapterRepository.save(chapter);
            EditChapter editChapter = new EditChapter();
            editChapter.setChapter(chapter);
            editChapter.setType(EditType.CREATE);
            editChapter.setUser(chapter.getUser());
            editChapter.setDate(LocalDateTime.now());
            editChapterRepository.save(editChapter);
        }
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public boolean updateChapter(Chapter chapter, User user) throws PSQLException, ForbiddenException{
        var chap = chapterRepository.findById(chapter.getId());
        if(chap.isPresent()) {
            Chapter chapter1 = chap.get();
            if(user.getRole().equals(UserRole.APPROVED_ADMIN) ||
                    chapter1.getUser().getId().equals(user.getId())) {
                chapter1.setName(chapter.getName());
                chapter1.setParentLegion(chapter.getParentLegion());
                chapterRepository.save(chapter1);


                EditChapter editChapter = new EditChapter();
                editChapter.setChapter(chapter1);
                editChapter.setType(EditType.UPDATE);
                editChapter.setUser(user);
                editChapter.setDate(LocalDateTime.now());
                editChapterRepository.save(editChapter);
                return true;
            } else throw new ForbiddenException();
        } else return false;
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Iterable<Chapter> getAllChaptersByUser(User user){
        if(user.getRole().equals(UserRole.APPROVED_ADMIN)){
            return getAllChapters();
        }
        return chapterRepository.findAllByUser(user);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Iterable<Chapter> getAllChapters(){
        return chapterRepository.findAll();
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Iterable<Chapter> getPageChapters(String sortParam, int page, int size){
        Sort sort = Sort.by(Sort.Direction.ASC, sortParam);
        Pageable pageable = PageRequest.of(page, size, sort);
        return chapterRepository.findAll(pageable);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Iterable<Chapter> getPageChapterByName(String sortParam, int page, int size, String name){
        Sort sort = Sort.by(Sort.Direction.ASC, sortParam);
        Pageable pageable = PageRequest.of(page, size, sort);
        return chapterRepository.findAllByName(pageable, name);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Iterable<Chapter> getPageChapterByParentLegion(String sortParam, int page, int size, String parentLegion){
        Sort sort = Sort.by(Sort.Direction.ASC, sortParam);
        Pageable pageable = PageRequest.of(page, size, sort);
        return chapterRepository.findAllByParentLegion(pageable, parentLegion);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public boolean deleteChapter(Long id, User user) throws ForbiddenException{
        var chap = chapterRepository.findById(id);
        if(chap.isPresent()) {
            Chapter chapter = chap.get();
            if(user.getRole().equals(UserRole.APPROVED_ADMIN) ||
                    chapter.getUser().getId().equals(user.getId())) {
                chapterRepository.deleteById(id);
                return true;
            } else throw new ForbiddenException();
        } else return false;
    }

}
