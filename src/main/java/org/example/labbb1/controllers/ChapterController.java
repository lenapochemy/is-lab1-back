package org.example.labbb1.controllers;

import org.example.labbb1.model.Chapter;
import org.example.labbb1.services.ChapterService;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/space/chapter")
public class ChapterController {

    private final ChapterService chapterService;

    @Autowired
    public ChapterController(ChapterService chapterService) {
        this.chapterService = chapterService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> addNewChapter(@RequestBody Chapter chapter){
        if(chapter.getName() == null ||chapter.getName().isEmpty()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            chapterService.addNewChapter(chapter);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (PSQLException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateChapter(@RequestBody Chapter chapter){
        Chapter chapter1 = chapterService.getChapterById(chapter.getId());
        if(chapter.getName() == null ||chapter.getName().isEmpty() || chapter1 == null
        ){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            chapterService.updateChapter(chapter);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (PSQLException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping()
    public Iterable<Chapter> getAllChapters(){
        Iterable<Chapter> chapters = chapterService.getAllChapters();
        chapters.forEach(chapter -> {
            chapter.setSpaceMarines(null);
        });
        return chapters;
    }

    @GetMapping("/{sort}/{page}")
    public Iterable<Chapter> getChapters(@PathVariable String sort, @PathVariable int page){
        Iterable<Chapter> chapters = chapterService.getPageChapters(sort, page);
        chapters.forEach(chapter -> {
            chapter.setSpaceMarines(null);
        });
        return chapters;
    }

    @GetMapping("/byName/{name}/{sort}/{page}")
    public Iterable<Chapter> getChaptersByName(@PathVariable String name, @PathVariable String sort, @PathVariable int page){
        Iterable<Chapter> chapters = chapterService.getPageChapterByName(sort, page, name);
        chapters.forEach(chapter -> {
            chapter.setSpaceMarines(null);
        });
        return chapters;
    }

    @GetMapping("/byParentLegion/{parentLegion}/{sort}/{page}")
    public Iterable<Chapter> getChaptersByparentlegion(@PathVariable String parentLegion, @PathVariable String sort, @PathVariable int page){
        Iterable<Chapter> chapters = chapterService.getPageChapterByParentLegion(sort, page, parentLegion);
        chapters.forEach(chapter -> {
            chapter.setSpaceMarines(null);
        });
        return chapters;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteChapter(@PathVariable Long id){
        chapterService.deleteChapter(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
