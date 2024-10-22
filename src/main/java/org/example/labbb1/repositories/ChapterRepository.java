package org.example.labbb1.repositories;

import jakarta.annotation.Nullable;
import org.example.labbb1.model.Chapter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChapterRepository extends JpaRepository<Chapter, Long> {

    Page<Chapter> findAll (Pageable pageable);
    void deleteById(Long id);

//    Chapter findById(Long id);

}
