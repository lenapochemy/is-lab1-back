package org.example.labbb1.repositories;

import org.example.labbb1.model.Chapter;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChapterRepository extends CrudRepository<Chapter, Long> {

    void deleteById(Long id);

}
