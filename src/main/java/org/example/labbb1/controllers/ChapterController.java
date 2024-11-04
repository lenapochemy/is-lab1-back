package org.example.labbb1.controllers;

import org.example.labbb1.exceptions.ForbiddenException;
import org.example.labbb1.model.Chapter;
import org.example.labbb1.model.User;
import org.example.labbb1.services.ChapterService;
import org.example.labbb1.services.EditService;
import org.example.labbb1.services.UserService;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/space/chapter")
public class ChapterController {

    private final ChapterService chapterService;
    private final UserService userService;
    private final EditService editService;
    @Autowired
    public ChapterController(ChapterService chapterService, UserService userService,
                             EditService editService) {
        this.chapterService = chapterService;
        this.userService = userService;
        this.editService = editService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> addNewChapter(@RequestBody Chapter chapter,
                                           @RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        if(chapter.getName() == null ||chapter.getName().isEmpty() || token == null || token.isEmpty()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(!userService.verifyToken(token)){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        User user = userService.findUserByToken(token);
        chapter.setUser(user);
        try {
            chapterService.addNewChapter(chapter);
//            editService.addNewEditChapterOnCreate(chapter.getName());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (PSQLException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateChapter(@RequestBody Chapter chapter,
                                           @RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        Chapter chapter1 = chapterService.getChapterById(chapter.getId());
        if(chapter.getName() == null ||chapter.getName().isEmpty() || chapter1 == null ||
                token == null || token.isEmpty()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(!userService.verifyToken(token)){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        try {
            User user = userService.findUserByToken(token);
            if(chapterService.updateChapter(chapter, user)){
                return new ResponseEntity<>(HttpStatus.OK);
            } else
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        } catch (PSQLException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (ForbiddenException e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping(value =  {"/{filter_param}/{filter_value}/{sort_param}/{page}/{size}", "{filter_param}"})
    public ResponseEntity<?> getChaptersByparentlegion(@PathVariable String filter_param,
                                                       @PathVariable(required = false) String filter_value,
                                                       @PathVariable(required = false) String sort_param,
                                                       @PathVariable(required = false) Integer page,
                                                       @PathVariable(required = false) Integer size,
                                                       @RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        if(token == null || token.isEmpty()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(!userService.verifyToken(token)){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Iterable<Chapter> chapters;
        switch (filter_param){
            case "name":{
                chapters = chapterService.getPageChapterByName(sort_param, page, size, filter_value);
                break;
            }
            case "parentLegion": {
                chapters = chapterService.getPageChapterByParentLegion(sort_param, page, size, filter_value);
                break;
            }
            case "user": {
                User user = userService.findUserByToken(token);
                chapters = chapterService.getAllChaptersByUser(user);
                break;
            }
            case "all": {
                if(size == null){
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
    public ResponseEntity<?> deleteChapter(@PathVariable Long id, @RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        if(token == null || token.isEmpty()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(!userService.verifyToken(token)){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        User user = userService.findUserByToken(token);
        try {
            if (chapterService.deleteChapter(id, user)) {
                return new ResponseEntity<>(HttpStatus.OK);
            } else
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (ForbiddenException e){
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
                chapter.getUser().setSpaceMarines(null);
                chapter.setEditChapters(null);
            });
        }
    }
}
