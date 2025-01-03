package org.example.labbb1.repositories;

import org.example.labbb1.model.Coordinates;
import org.example.labbb1.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CoordinatesRepository extends PagingAndSortingRepository<Coordinates, Long>, CrudRepository<Coordinates, Long> {
    Page<Coordinates> findAll (Pageable pageable);
    Iterable<Coordinates> findAll(Sort sort);

    Coordinates findById (long id);
    Coordinates findByX (Integer x);


    Page<Coordinates> findAllByX(Pageable pageable, Integer x);
    Page<Coordinates> findAllByY(Pageable pageable, Float y);
    List<Coordinates> findAllByUser(User user);
}
