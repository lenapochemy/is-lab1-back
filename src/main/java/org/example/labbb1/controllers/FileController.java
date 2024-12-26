package org.example.labbb1.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.labbb1.exceptions.*;
import org.example.labbb1.model.ImportHistory;
import org.example.labbb1.model.User;
import org.example.labbb1.services.FileStorageService;
import org.example.labbb1.services.ImportService;
import org.example.labbb1.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/files")
public class FileController {
    private final FileStorageService fileStorageService;
    private final UserService userService;
    private final ImportService importService;
    @Autowired
    public FileController(FileStorageService fileStorageService, UserService userService, ImportService importService){
        this.fileStorageService = fileStorageService;
        this.userService = userService;
        this.importService = importService;
    }

    @PostMapping
    public ResponseEntity<?> uploadFile(@RequestParam MultipartFile file){
        System.out.println("\u001B[32m" + "запрос на загрузку файла");
        ImportHistory importHistory = new ImportHistory();
        importHistory.setUser(userService.findUserByToken());
        try {
            int count = fileStorageService.parseFile(file, userService.findUserByToken());
            importHistory.setStatus(true);
            importHistory.setCount(count);
//            if(res.equals("Success!")) {
                return new ResponseEntity<>(HttpStatus.OK);
//            } else return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Incorrect value in file");
        } catch (JsonProcessingException e){
            importHistory.setStatus(false);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad file format");
        } catch (IOException e){
            importHistory.setStatus(false);
            System.out.println("problem with parsing");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Problem with reading file");
        } catch (IncorrectValueException e) {
            importHistory.setStatus(false);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Incorrect value in file");
        } catch (ChapterException e) {
            importHistory.setStatus(false);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Incorrect value in file: Parent legion name should be start from letter 'l'");
        } catch (CoordinatesException e) {
            importHistory.setStatus(false);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Incorrect value in file: Coordinates X value should be multiple 5");
        } catch (TooManyMarinesInOneChapterException e) {
            importHistory.setStatus(false);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Incorrect value in file: Too many marines for one chapter,");
        } catch (MarineException e) {
            importHistory.setStatus(false);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Marine name should be unique");

        } finally {
            importHistory.setDateTime(LocalDateTime.now());
            importService.addNewImport(importHistory);
        }
    }

    @GetMapping
    public ResponseEntity<?> getImportsHistory(){
        User user = userService.findUserByToken();
        return ResponseEntity.ok(importService.getImportHistory(user));
    }

}
