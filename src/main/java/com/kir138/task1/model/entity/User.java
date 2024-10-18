package com.kir138.task1.model.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Builder
@Getter
@Setter
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name; //здесь скорее всего id

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserCityFavorites> favorites;
}
