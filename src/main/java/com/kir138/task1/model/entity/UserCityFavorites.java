package com.kir138.task1.model.entity;

import com.kir138.task1.model.dto.CityDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@Table(name = "userCityFavorites")
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class UserCityFavorites {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "id"), nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "city_name", foreignKey = @ForeignKey(name = "city_name_history"), nullable = false)
    private WeatherHistory weatherHistory;
}