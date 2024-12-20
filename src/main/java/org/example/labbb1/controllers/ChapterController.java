package org.example.labbb1.controllers;

import org.example.labbb1.exceptions.ChapterException;
import org.example.labbb1.exceptions.ForbiddenException;
import org.example.labbb1.model.Chapter;
import org.example.labbb1.model.User;
import org.example.labbb1.services.ChapterService;
import org.example.labbb1.services.UserService;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/space/chapter")
public class ChapterController {

    private final ChapterService chapterService;
    private final UserService userService;

    @Autowired
    public ChapterController(ChapterService chapterService, UserService userService) {
        this.chapterService = chapterService;
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> addNewChapter(@RequestBody Chapter chapter) {
        if (chapter.getName() == null || chapter.getName().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        User user = userService.findUserByToken();
        chapter.setUser(user);
        try {
            chapterService.addNewChapter(chapter);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (ChapterException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Parent legion name should be start from letter 'l'");
        }
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateChapter(@RequestBody Chapter chapter) {
        Chapter chapter1 = chapterService.getChapterById(chapter.getId());
        if (chapter.getName() == null || chapter.getName().isEmpty() || chapter1 == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            User user = userService.findUserByToken();
            if (chapterService.updateChapter(chapter, user)) {
                return new ResponseEntity<>(HttpStatus.OK);
            } else
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        } catch (PSQLException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (ForbiddenException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping
//    @GetMapping(value =  {"/{filter_param}/{filter_value}/{sort_param}/{page}/{size}", "{filter_param}", "/{filter_param}/{filter_value}"})
    public ResponseEntity<?> getChaptersByParentlegion(String filter_param,
                                                       String filter_value,
                                                       String sort_param,
                                                       Integer page,
                                                       Integer size) {
        Iterable<Chapter> chapters;
        switch (filter_param) {
            case "name": {
                chapters = chapterService.getPageChapterByName(sort_param, page, size, filter_value);
                break;
            }
            case "parentLegion": {
                chapters = chapterService.getPageChapterByParentLegion(sort_param, page, size, filter_value);
                break;
            }
            case "user": {
                User user = userService.findUserByToken();
                chapters = chapterService.getAllChaptersByUser(user);
                if (filter_value != null) {
                    List<Long> chaptersId = new ArrayList<>();
                    chapters.forEach(chapter -> chaptersId.add(chapter.getId()));
                    return ResponseEntity.ok(chaptersId);
                }
                break;
            }
            case "all": {
                if (size == null) {
                    chapters = chapterService.getAllChapters();
                } else
                    chapters = chapterService.getPageChapters(sort_param, page, size);
                break;
            }
            default:
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        attributeToNull(chapters);
        return ResponseEntity.ok(chapters);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteChapter(@PathVariable Long id) {

        User user = userService.findUserByToken();
        try {
            if (chapterService.deleteChapter(id, user)) {
                return new ResponseEntity<>(HttpStatus.OK);
            } else
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (ForbiddenException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    private void attributeToNull(Iterable<Chapter> chapters) {
        if (chapters != null) {
            chapters.forEach(chapter -> {
                chapter.setSpaceMarines(null);
                chapter.getUser().setPassword(null);
                chapter.getUser().setId(null);
                chapter.getUser().setCoordinates(null);
                chapter.getUser().setChapters(null);
                chapter.getUser().setImports(null);
                chapter.getUser().setSpaceMarines(null);
                chapter.setEditChapters(null);
            });
        }
    }
}
