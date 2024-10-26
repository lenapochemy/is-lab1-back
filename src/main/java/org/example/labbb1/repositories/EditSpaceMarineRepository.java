package org.example.labbb1.repositories;

import org.example.labbb1.model.EditSpaceMarine;
import org.example.labbb1.model.SpaceMarine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EditSpaceMarineRepository extends JpaRepository<EditSpaceMarine, Long> {

    Iterable<EditSpaceMarine> findAllBySpaceMarine(SpaceMarine spaceMarine);
}
