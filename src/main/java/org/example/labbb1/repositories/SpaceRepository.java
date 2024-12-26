package org.example.labbb1.repositories;

import org.example.labbb1.model.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SpaceRepository extends CrudRepository<SpaceMarine, Long>, PagingAndSortingRepository<SpaceMarine, Long> {

    Page<SpaceMarine> findAll (Pageable pageable);
//    SpaceMarine findById(Long id);
    Iterable<SpaceMarine> findAllByUser(User user);
    Page<SpaceMarine> findAllByName(Pageable pageable, String name);
    Page<SpaceMarine> findAllByCoordinates(Pageable pageable, Coordinates coordinates);
    Page<SpaceMarine> findAllByCreationDate(Pageable pageable, LocalDateTime creationDate);
    Page<SpaceMarine> findAllByChapter(Pageable pageable, Chapter chapter);

    List<SpaceMarine> findAllByChapter(Chapter chapter);
    Page<SpaceMarine> findAllByHealth(Pageable pageable, Long health);
    Page<SpaceMarine> findAllByCategory(Pageable pageable, AstartesCategory category);
    Page<SpaceMarine> findAllByWeaponType(Pageable pageable, Weapon weaponType);
    Page<SpaceMarine> findAllByMeleeWeapon(Pageable pageable, MeleeWeapon meleeWeapon);

    SpaceMarine findByName(String name);


}
