package org.example.labbb1.services;

import org.example.labbb1.exceptions.*;
import org.example.labbb1.model.*;
import org.example.labbb1.repositories.EditSpaceMarineRepository;
import org.example.labbb1.repositories.SpaceRepository;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SpaceService {

    private final SpaceRepository spaceRepository;
    private final EditSpaceMarineRepository editSpaceMarineRepository;
    private final UserService userService;
    private final CoordinatesService coordinatesService;
    private final ChapterService chapterService;

    @Autowired
    public SpaceService(SpaceRepository spaceRepository, EditSpaceMarineRepository editSpaceMarineRepository,
                        UserService userService, ChapterService chapterService, CoordinatesService coordinatesService) {
        this.spaceRepository = spaceRepository;
        this.editSpaceMarineRepository = editSpaceMarineRepository;
        this.userService = userService;
        this.coordinatesService = coordinatesService;
        this.chapterService = chapterService;
    }


    @Transactional(rollbackFor = {ChapterException.class, TooManyMarinesInOneChapterException.class, MarineException.class},
            propagation = Propagation.REQUIRED, isolation = Isolation.SERIALIZABLE)
    public void addNewSpaceMarine(SpaceMarine spaceMarine) throws ChapterException, TooManyMarinesInOneChapterException, MarineException {

        SpaceMarine marine = spaceRepository.findByName(spaceMarine.getName());
        if (marine != null){
            throw new MarineException();
        }

        Chapter chapter = chapterService.getChapterById(spaceMarine.getChapter().getId());
        if (chapter == null) {
            throw new ChapterException();
        }
        System.out.println(chapter.toString());
        System.out.println(chapter.getSpaceMarines().size());
        spaceRepository.findAllByChapter(chapter).forEach((x) -> System.out.println(x.toString()));
        if (chapter.getSpaceMarines().size() >= 3) {
            System.out.println("-------------");
            throw new TooManyMarinesInOneChapterException();
        }

        spaceRepository.save(spaceMarine);
        EditSpaceMarine editSpaceMarine = new EditSpaceMarine();
        editSpaceMarine.setSpaceMarine(spaceMarine);
        editSpaceMarine.setType(EditType.CREATE);
        editSpaceMarine.setUser(spaceMarine.getUser());
        editSpaceMarine.setDate(LocalDateTime.now());
        editSpaceMarineRepository.save(editSpaceMarine);
    }

    @Transactional(rollbackFor = {IncorrectValueException.class, ChapterException.class, CoordinatesException.class,
            TooManyMarinesInOneChapterException.class, MarineException.class}, isolation = Isolation.SERIALIZABLE)
    public void addListOfNewSpaceMarines(List<SpaceMarine> spaceMarineList) throws IncorrectValueException,
            CoordinatesException, ChapterException, TooManyMarinesInOneChapterException, MarineException {
        for (SpaceMarine spaceMarine : spaceMarineList) {
            if (spaceMarine.getName() == null || spaceMarine.getName().isEmpty() || spaceMarine.getCoordinates() == null ||
                    spaceMarine.getCoordinates().getX() == null || spaceMarine.getCoordinates().getX() <= -147 ||
                    spaceMarine.getCoordinates().getY() == null || spaceMarine.getChapter() == null ||
                    spaceMarine.getChapter().getName() == null || spaceMarine.getChapter().getName().isEmpty() ||
                    spaceMarine.getHealth() <= 0 || spaceMarine.getCategory() == null) {
//                    return "Incorrect value";
                throw new IncorrectValueException();
            }
            User user = userService.findUserByToken();

            Coordinates coordinates = spaceMarine.getCoordinates();
            coordinates.setUser(user);
            coordinatesService.addNewCoordinate(coordinates);

            Chapter chapter = spaceMarine.getChapter();
            chapter.setUser(user);
            chapterService.addNewChapter(chapter);

            spaceMarine.setCreationDate(LocalDateTime.now());
            spaceMarine.setUser(user);
            addNewSpaceMarine(spaceMarine);
        }

    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public boolean updateSpaceMarine(SpaceMarine spaceMarine, User user) throws PSQLException, ForbiddenException {
        var marine = spaceRepository.findById(spaceMarine.getId());
        if (marine.isPresent()) {
            SpaceMarine spaceMarine1 = marine.get();
            if (user.getRole().equals(UserRole.APPROVED_ADMIN) ||
                    spaceMarine1.getUser().getId().equals(user.getId())) {

                spaceMarine1.setName(spaceMarine.getName());
                spaceMarine1.setCoordinates(spaceMarine.getCoordinates());
                spaceMarine1.setChapter(spaceMarine.getChapter());
                spaceMarine1.setHealth(spaceMarine.getHealth());
                spaceMarine1.setCategory(spaceMarine.getCategory());
                spaceMarine1.setWeaponType(spaceMarine.getWeaponType());
                spaceMarine1.setMeleeWeapon(spaceMarine.getMeleeWeapon());

                spaceRepository.save(spaceMarine1);
                EditSpaceMarine editSpaceMarine = new EditSpaceMarine();
                editSpaceMarine.setSpaceMarine(spaceMarine1);
                editSpaceMarine.setType(EditType.UPDATE);
                editSpaceMarine.setUser(user);
                editSpaceMarine.setDate(LocalDateTime.now());
                editSpaceMarineRepository.save(editSpaceMarine);
                return true;
            } else throw new ForbiddenException();
        } else return false;
    }


    @Transactional(isolation = Isolation.READ_COMMITTED)
    public SpaceMarine getSpaceMarine(Long id) {
        var marine = spaceRepository.findById(id);
        SpaceMarine spaceMarine = null;
        if (marine.isPresent()) {
            spaceMarine = marine.get();
        }
        return spaceMarine;
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Iterable<SpaceMarine> getPageSpaceMarine(String sortParam, int page, int size) {
        Sort sort = Sort.by(Sort.Direction.ASC, sortParam);
        Pageable pageable = PageRequest.of(page, size, sort);
        return spaceRepository.findAll(pageable);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Iterable<SpaceMarine> getAllSpaceMarineByUser(User user) {
        if (user.getRole().equals(UserRole.APPROVED_ADMIN)) {
            return getAllSpaceMarine();
        }
        return spaceRepository.findAllByUser(user);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Iterable<SpaceMarine> getAllSpaceMarine() {
        return spaceRepository.findAll();
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Iterable<SpaceMarine> getPageSpaceMarineByName(String sortParam, int page, int size, String name) {
        Sort sort = Sort.by(Sort.Direction.ASC, sortParam);
        Pageable pageable = PageRequest.of(page, size, sort);
        return spaceRepository.findAllByName(pageable, name);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Iterable<SpaceMarine> getPageSpaceMarineByCoordinates(String sortParam, int page, int size, Coordinates coordinates) {
        Sort sort = Sort.by(Sort.Direction.ASC, sortParam);
        Pageable pageable = PageRequest.of(page, size, sort);
        return spaceRepository.findAllByCoordinates(pageable, coordinates);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Iterable<SpaceMarine> getPageSpaceMarineByChapter(String sortParam, int page, int size, Chapter chapter) {
        Sort sort = Sort.by(Sort.Direction.ASC, sortParam);
        Pageable pageable = PageRequest.of(page, size, sort);
        return spaceRepository.findAllByChapter(pageable, chapter);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Iterable<SpaceMarine> getPageSpaceMarineByHealth(String sortParam, int page, int size, Long health) {
        Sort sort = Sort.by(Sort.Direction.ASC, sortParam);
        Pageable pageable = PageRequest.of(page, size, sort);
        return spaceRepository.findAllByHealth(pageable, health);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public boolean deleteSpaceMarine(Long id, User user) throws ForbiddenException {
        var marine = spaceRepository.findById(id);
        if (marine.isPresent()) {
            SpaceMarine spaceMarine1 = marine.get();
            if (user.getRole().equals(UserRole.APPROVED_ADMIN) ||
                    spaceMarine1.getUser().getId().equals(user.getId())) {
                spaceRepository.deleteById(id);
                return true;
            } else throw new ForbiddenException();
        } else return false;
    }


}
