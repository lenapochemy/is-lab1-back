package org.example.labbb1.repositories;

import org.example.labbb1.model.Chapter;
import org.example.labbb1.model.EditChapter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EditChapterRepository extends JpaRepository<EditChapter, Long> {

    Iterable<EditChapter> findAllByChapter(Chapter chapter);

}
