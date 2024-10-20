package org.example.labbb1.repositories;

import org.example.labbb1.model.Coordinates;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoordinatesRepository extends CrudRepository<Coordinates, Long> {


}
