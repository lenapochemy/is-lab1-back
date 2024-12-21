package org.example.labbb1.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.eclipse.persistence.annotations.CascadeOnDelete;

import java.util.List;

@Entity
@Table(name = "chapter")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Chapter {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String parentLegion;

    @CascadeOnDelete
    @OneToMany(mappedBy = "chapter", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<SpaceMarine> spaceMarines;

    @ManyToOne (fetch = FetchType.EAGER)
    private User user;

    @OneToMany(mappedBy = "chapter", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<EditChapter> editChapters;


    @Override
    public String toString(){
        return "chapter: id=" + this.id + " name=" + this.name + " marines count=" + this.spaceMarines.size();
    }
}
