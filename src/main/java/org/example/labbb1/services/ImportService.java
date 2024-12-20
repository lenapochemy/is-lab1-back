package org.example.labbb1.services;

import org.example.labbb1.model.ImportHistory;
import org.example.labbb1.model.User;
import org.example.labbb1.model.UserRole;
import org.example.labbb1.repositories.ImportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImportService {

    private final ImportRepository importRepository;

    @Autowired
    public ImportService(ImportRepository importRepository){
        this.importRepository = importRepository;
    }

    public void addNewImport(ImportHistory importHistory){
        importRepository.save(importHistory);
    }

    public List<ImportHistory> getImportHistory(User user){
        if(user.getRole().equals(UserRole.APPROVED_ADMIN)){
            return attributesToNull(importRepository.findAll());
        } else {
            return attributesToNull(importRepository.findAllByUser(user));
        }
    }

    private List<ImportHistory> attributesToNull(List<ImportHistory> list){
        list.forEach(importHistory -> {
            importHistory.getUser().setSpaceMarines(null);
            importHistory.getUser().setCoordinates(null);
            importHistory.getUser().setChapters(null);
            importHistory.getUser().setPassword(null);
            importHistory.getUser().setId(null);
            importHistory.getUser().setImports(null);
        });
        return list;
    }
}
