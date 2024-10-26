package org.example.labbb1.repositories;

import jakarta.annotation.Nullable;
import org.example.labbb1.model.Chapter;
import org.example.labbb1.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChapterRepository extends JpaRepository<Chapter, Long> {

    Page<Chapter> findAll (Pageable pageable);
    void deleteById(Long id);
    Page<Chapter> findAllByName (Pageable pageable, String name);
    Page<Chapter> findAllByParentLegion (Pageable pageable, String parentLegion);
    List<Chapter> findAllByUser(User user);
    Chapter findByName(String name);

//    Chapter findById(Long id);

}
