package com.example.backendspring.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "characters")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Character {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false, length = 2000)
    private String description;

    @Column(name = "date_added", nullable = false)
    @CreationTimestamp
    private Date dateAdded;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "gender", nullable = false)
    private String gender;

    @Column(name = "age")
    private Integer age;

    @Column(name = "nickname")
    private String nickname;

    @Lob
    @Column(name = "picture", columnDefinition = "LONGBLOB")
    private byte[] imageBytes;

    @Column(name = "approved", nullable = false)
    private boolean approved;

    @ManyToOne
    @JoinColumn(name = "id_actor")
    private Actor actor;

    @ManyToOne
    @JoinColumn(name = "id_dabber")
    private Actor dabber;

    @ManyToOne
    @JoinColumn(name = "added_by")
    private User addedBy;

    @ManyToOne
    @JoinColumn(name = "approved_by")
    private User approvedBy;

    @ManyToMany
    @JoinTable(
            name = "characters_movies",
            joinColumns = @JoinColumn(name = "id_character"),
            inverseJoinColumns = @JoinColumn(name = "id_movie")
    )

    private List<Movie> movies = new ArrayList<>();

}
