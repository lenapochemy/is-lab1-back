package org.example.labbb1.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.labbb1.model.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SpaceMarineDTO {
    private Long id;
    private String name;
    private CoordinatesDTO coordinatesDTO;
    private LocalDateTime creationDate;
    private ChapterDTO chapterDTO;
    private Long health;
    private AstartesCategory category;
    private Weapon weaponType;
    private MeleeWeapon meleeWeapon;
    private UserDTO userDTO;
}
