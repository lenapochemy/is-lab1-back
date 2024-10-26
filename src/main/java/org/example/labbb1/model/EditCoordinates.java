package org.example.labbb1.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "edit_coordinates")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EditCoordinates {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JoinColumn(nullable = false)
    @ManyToOne
    private Coordinates coordinates;

    @JoinColumn(nullable = false)
    @ManyToOne
    private User user;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EditType type;

    @Column(nullable = false)
    private LocalDateTime date;

}
