package org.example.labbb1.repositories;

import org.example.labbb1.model.SpaceMarine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

@SuppressWarnings("ALL")
@Repository
public interface SpaceRepository extends CrudRepository<SpaceMarine, Long>, PagingAndSortingRepository<SpaceMarine, Long> {

    Page<SpaceMarine> findAll (Pageable pageable);
//    SpaceMarine findById(Long id);

}
