package org.example.labbb1.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "space_marine")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Cacheable(value = false)
public class SpaceMarine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne //если убрать каскаде тайп, то можно будет удалять марину, без удаления координат
    private Coordinates coordinates;

    @Column(nullable = false)
    private LocalDateTime creationDate;

    @ManyToOne //если убрать каскаде тайп, то можно будет удалять марину, без удаления части
    private Chapter chapter;

    private Long health;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AstartesCategory category;

    @Enumerated(EnumType.STRING)
    private Weapon weaponType;

    @Enumerated(EnumType.STRING)
    private MeleeWeapon meleeWeapon;

    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "spaceMarine", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<EditSpaceMarine> editSpaceMarines;

}
