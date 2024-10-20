package org.example.labbb1.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.eclipse.persistence.annotations.CascadeOnDelete;

import java.util.List;

@Entity
@Table(name = "coordinates")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Coordinates {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false)
    private Integer x;
    @Column(nullable = false)
    private Float y;

    @CascadeOnDelete
    @OneToMany(mappedBy = "coordinates", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SpaceMarine> spaceMarines;

//    public void removeSpaceMarine(SpaceMarine spaceMarine){
//        spaceMarines.remove(spaceMarine);
//        spaceMarine.setCoordinates(null);
//    }

}
