package org.example.labbb1.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "space_users")
@Getter
@Setter
@NoArgsConstructor
@Cacheable(value = false)
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String login;

    @Column(unique = true, nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @OneToMany(mappedBy = "user")
    private List<Coordinates> coordinates;

    @OneToMany(mappedBy = "user")
    private List<Chapter> chapters;

    @OneToMany(mappedBy = "user")
    private List<SpaceMarine> spaceMarines;

    @OneToMany(mappedBy = "user")
    private List<ImportHistory> imports;

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities(){
        return List.of(new SimpleGrantedAuthority(this.role.name()));
    }

    @Override
    public String getUsername(){
        return getLogin();
    }


    @Override
    public String toString(){
        return "user: id=" + this.id + " login=" + this.login + " password=" + this.password + " role=" + this.role;
    }
}
