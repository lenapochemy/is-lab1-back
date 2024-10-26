package org.example.labbb1.repositories;

import org.example.labbb1.model.Coordinates;
import org.example.labbb1.model.EditCoordinates;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EditCoordinatesRepository extends JpaRepository<EditCoordinates, Long> {

    Iterable<EditCoordinates> findAllByCoordinates(Coordinates coordinates);
}
