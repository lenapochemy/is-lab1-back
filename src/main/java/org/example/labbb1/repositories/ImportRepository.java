package org.example.labbb1.repositories;

import org.example.labbb1.model.ImportHistory;
import org.example.labbb1.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImportRepository extends JpaRepository<ImportHistory, Long> {

    List<ImportHistory> findAllByUser(User user);
}
