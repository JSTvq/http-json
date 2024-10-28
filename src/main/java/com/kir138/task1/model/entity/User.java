package com.kir138.task1.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Builder
@Getter
@Setter
@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id", nullable = false, foreignKey = @ForeignKey(name = "fk_user_city"))
    private City city;

    //@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    //private Set<UserCityFavorites> favorites;
}
