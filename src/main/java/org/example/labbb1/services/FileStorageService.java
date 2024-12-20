package org.example.labbb1.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.labbb1.exceptions.ChapterException;
import org.example.labbb1.exceptions.CoordinatesException;
import org.example.labbb1.exceptions.IncorrectValueException;
import org.example.labbb1.exceptions.TooManyMarinesInOneChapterException;
import org.example.labbb1.model.ImportHistory;
import org.example.labbb1.model.SpaceMarine;
import org.example.labbb1.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class FileStorageService {

    private final SpaceService spaceService;
    private final ImportService importService;

    @Autowired
    public FileStorageService(SpaceService spaceService, ImportService importService) {
        this.spaceService = spaceService;
        this.importService = importService;
    }


    public int parseFile(MultipartFile file, User user) throws JsonProcessingException, IOException,
            IncorrectValueException, ChapterException, CoordinatesException{
        String content = new String(file.getBytes());
        ObjectMapper objectMapper = new ObjectMapper();
        List<SpaceMarine> spaceMarines = objectMapper.readValue(content, new TypeReference<>() {
        });
//        ImportHistory importHistory = new ImportHistory();
//        importHistory.setUser(user);
        spaceService.addListOfNewSpaceMarines(spaceMarines);
//        if(spaceService.addListOfNewSpaceMarines(spaceMarines)) {
//            importHistory.setStatus(true);
//            importHistory.setCount(spaceMarines.size());
//        } else {
//            importHistory.setStatus(false);
//        }
//        importService.addNewImport(importHistory);
        return spaceMarines.size();
    }

}
