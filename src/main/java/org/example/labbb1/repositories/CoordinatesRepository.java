package org.example.labbb1.repositories;

import org.example.labbb1.model.Coordinates;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@SuppressWarnings("ALL")
@Repository
public interface CoordinatesRepository extends PagingAndSortingRepository<Coordinates, Long>, CrudRepository<Coordinates, Long> {
    Page<Coordinates> findAll (Pageable pageable);

    Coordinates findById (long id);

}
