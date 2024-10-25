package org.example.labbb1.controllers;

import org.example.labbb1.exceptions.ForbiddenException;
import org.example.labbb1.model.Chapter;
import org.example.labbb1.model.User;
import org.example.labbb1.services.ChapterService;
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
    @Autowired
    public ChapterController(ChapterService chapterService, UserService userService) {
        this.chapterService = chapterService;
        this.userService = userService;
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

    @GetMapping()
    public ResponseEntity<?> getAllChapters(@RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        if(token == null || token.isEmpty()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(!userService.verifyToken(token)){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        Iterable<Chapter> chapters = chapterService.getAllChapters();
        attributeToNull(chapters);
        return ResponseEntity.ok(chapters);
    }

    @GetMapping("/{sort}/{page}")
    public ResponseEntity<?> getChapters(@PathVariable String sort, @PathVariable Integer page,
                                         @RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        if(sort == null || page == null || token == null || token.isEmpty()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(!userService.verifyToken(token)){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        Iterable<Chapter> chapters = chapterService.getPageChapters(sort, page);
        attributeToNull(chapters);
        return ResponseEntity.ok(chapters);
    }

    @GetMapping("/byName/{name}/{sort}/{page}")
    public ResponseEntity<?> getChaptersByName(@PathVariable String name, @PathVariable String sort,
                                               @PathVariable Integer page, @RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        if(sort == null || page == null || token == null || token.isEmpty()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(!userService.verifyToken(token)){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        Iterable<Chapter> chapters = chapterService.getPageChapterByName(sort, page, name);
        attributeToNull(chapters);
        return ResponseEntity.ok(chapters);
    }

    @GetMapping("/byParentLegion/{parentLegion}/{sort}/{page}")
    public ResponseEntity<?> getChaptersByparentlegion(@PathVariable String parentLegion, @PathVariable String sort,
                                                       @PathVariable Integer page, @RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        if(sort == null || page == null || token == null || token.isEmpty()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(!userService.verifyToken(token)){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        Iterable<Chapter> chapters = chapterService.getPageChapterByParentLegion(sort, page, parentLegion);
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

    private void attributeToNull(Iterable<Chapter> chapters){
        chapters.forEach(chapter -> {
            chapter.setSpaceMarines(null);
            chapter.getUser().setPassword(null);
            chapter.getUser().setId(null);
            chapter.getUser().setCoordinates(null);
            chapter.getUser().setChapters(null);
            chapter.getUser().setSpaceMarines(null);
        });
    }
}
