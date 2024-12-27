package org.example.labbb1.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.labbb1.exceptions.*;
import org.example.labbb1.model.ImportHistory;
import org.example.labbb1.model.SpaceMarine;
import org.example.labbb1.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLTransientConnectionException;
import java.util.List;

@Service
public class FileService {

    private final SpaceService spaceService;
    private final ImportService importService;
    private final MinIOService minIOService;

    @Autowired
    public FileService(SpaceService spaceService, ImportService importService, MinIOService minIOService) {
        this.spaceService = spaceService;
        this.importService = importService;
        this.minIOService = minIOService;
    }


    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = { JsonProcessingException.class,
            IncorrectValueException.class, ChapterException.class, CoordinatesException.class, TooManyMarinesInOneChapterException.class,
            MarineException.class, MinIOException.class})
    public int uploadFile(MultipartFile file, User user, Long historyID) throws JsonProcessingException, IOException,
            IncorrectValueException, ChapterException, CoordinatesException, TooManyMarinesInOneChapterException,
            MarineException, MinIOException, SQLTransientConnectionException {
        String filename = historyID.toString();
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                try {
                    minIOService.unlockFile(filename);
                } catch (MinIOException e) {
                    //
                }
            }

            @Override
            public void afterCompletion(int status) {
                if (status == STATUS_ROLLED_BACK) {
                    try {
                        minIOService.unlockFile(filename);
                        minIOService.deleteFile(filename);
                    } catch (Exception e) {
                        //
                    }
                }
            }
        });

        String content = new String(file.getBytes());
        ObjectMapper objectMapper = new ObjectMapper();
        List<SpaceMarine> spaceMarines = objectMapper.readValue(content, new TypeReference<>() {
        });

        spaceService.addListOfNewSpaceMarines(spaceMarines);
//        int a = 1 / 0;
        minIOService.uploadAndLockFile(file.getInputStream(), file.getSize(), filename);
        return spaceMarines.size();
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = {MinIOException.class, BadRequestException.class})
    public InputStream getFile(Long historyId) throws MinIOException, BadRequestException{
        ImportHistory importHistory = importService.findById(historyId);
        if(importHistory != null && importHistory.isStatus() ) {
            return minIOService.downloadFile(historyId.toString());
        } else throw new BadRequestException();
    }

}
