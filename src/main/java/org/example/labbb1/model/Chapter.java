package org.example.labbb1.model;

import jakarta.persistence.*;
import lombok.*;
import org.eclipse.persistence.annotations.CascadeOnDelete;

import java.util.List;

@Entity
@Table(name = "chapter")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Cacheable(value = false)
public class Chapter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String parentLegion;

    @CascadeOnDelete
    @OneToMany(mappedBy = "chapter", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<SpaceMarine> spaceMarines;

    @ManyToOne (fetch = FetchType.EAGER)
    private User user;

    @OneToMany(mappedBy = "chapter", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<EditChapter> editChapters;


//    @Override
//    public String toString(){
//        return "chapter: id=" + this.id + " name=" + this.name + " marines count=" + this.spaceMarines.size();
//    }
}
