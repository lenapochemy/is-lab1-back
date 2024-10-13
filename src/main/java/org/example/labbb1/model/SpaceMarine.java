package org.example.labbb1.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "space_marine")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SpaceMarine {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false)
    private String name;
    @ManyToOne(cascade = CascadeType.REMOVE)
//    @Column(nullable = false)
    private Coordinates coordinates;
    @Column(nullable = false)
    private LocalDateTime creationDate;
    @ManyToOne
//    @Column(nullable = false)
    private Chapter chapter;
    private Long health;

    @Enumerated(EnumType.STRING)
    private AstartesCategory category;

    @Enumerated(EnumType.STRING)
    private Weapon weaponType;

    @Enumerated(EnumType.STRING)
    private MeleeWeapon meleeWeapon;

//    @ManyToOne
//    @JoinColumn(name = "user_id", referencedColumnName = "id")
//    private User user;

}
