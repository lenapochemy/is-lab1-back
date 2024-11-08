package org.example.labbb1.services;

import com.fasterxml.jackson.databind.util.ArrayIterator;
import org.example.labbb1.dto.ChapterDTO;
import org.example.labbb1.dto.UserDTO;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ChapterService {

    private final ChapterRepository chapterRepository;
    private final EditChapterRepository editChapterRepository;

    @Autowired
    public ChapterService(ChapterRepository chapterRepository, EditChapterRepository editChapterRepository){
        this.chapterRepository = chapterRepository;
        this.editChapterRepository = editChapterRepository;
    }


    public Chapter getChapterById(Long id){
        var chapt =  chapterRepository.findById(id);
        Chapter chapter = null;
        if(chapt.isPresent()){
            chapter = chapt.get();
        }
        return chapter;
    }

    public void addNewChapter(ChapterDTO chapterDTO, User user) throws PSQLException {
        Chapter chapter = new Chapter();
        chapter.setName(chapterDTO.getName());
        chapter.setParentLegion(chapterDTO.getParentLegion());
        chapter.setUser(user);

        chapterRepository.save(chapter);

        EditChapter editChapter = new EditChapter();
        editChapter.setChapter(chapter);
        editChapter.setType(EditType.CREATE);
        editChapter.setUser(chapter.getUser());
        editChapter.setDate(LocalDateTime.now());
        editChapterRepository.save(editChapter);
    }

    public boolean updateChapter(ChapterDTO chapterDTO, User user) throws PSQLException, ForbiddenException{
        var chap = chapterRepository.findById(chapterDTO.getId());
        if(chap.isPresent()) {
            Chapter chapter1 = chap.get();
            if(user.getRole().equals(UserRole.APPROVED_ADMIN) ||
                    chapter1.getUser().getId().equals(user.getId())) {
                chapter1.setName(chapterDTO.getName());
                chapter1.setParentLegion(chapterDTO.getParentLegion());
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

    public Iterable<ChapterDTO> getAllChaptersByUser(User user){
        if(user.getRole().equals(UserRole.APPROVED_ADMIN)){
            return getAllChapters();
        }
        Iterable<Chapter> chapters = chapterRepository.findAllByUser(user);
        return chaptersToDTOs(chapters);
    }

    public Iterable<ChapterDTO> getAllChapters(){
        Iterable<Chapter> chapters = chapterRepository.findAll();
        return chaptersToDTOs(chapters);
    }
    public Iterable<Chapter> getPageChapters(String sortParam, int page, int size){
        Sort sort = Sort.by(Sort.Direction.ASC, sortParam);
        Pageable pageable = PageRequest.of(page, size, sort);
        return chapterRepository.findAll(pageable);
    }

    public Iterable<Chapter> getPageChapterByName(String sortParam, int page, int size, String name){
        Sort sort = Sort.by(Sort.Direction.ASC, sortParam);
        Pageable pageable = PageRequest.of(page, size, sort);
        return chapterRepository.findAllByName(pageable, name);
    }

    public Iterable<Chapter> getPageChapterByParentLegion(String sortParam, int page, int size, String parentLegion){
        Sort sort = Sort.by(Sort.Direction.ASC, sortParam);
        Pageable pageable = PageRequest.of(page, size, sort);
        return chapterRepository.findAllByParentLegion(pageable, parentLegion);
    }


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


    private Iterable<ChapterDTO> chaptersToDTOs(Iterable<Chapter> chapters){
        List<ChapterDTO> dtos = new ArrayList<>();
        chapters.forEach(chap -> dtos.add(new ChapterDTO(chap.getId(), chap.getName(), chap.getParentLegion(),
                new UserDTO(chap.getUser().getId(), chap.getUser().getLogin()))));
        return dtos;
    }
}
