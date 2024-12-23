package com.kir138.task1.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor

@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherResponse {
    @JsonProperty("DailyForecasts")
    private List<WeatherList> list;

    @ToString
    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class WeatherList {
        @JsonProperty("Date")
        private String dateText;

        @JsonProperty("Temperature")
        private Temperature temperature;

        @JsonProperty("Day")
        private Day day;

        @ToString
        @Getter
        @Setter
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Temperature {
            @JsonProperty("Maximum")
            private Value value;
        }

        @ToString
        @Getter
        @Setter
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Value {
            @JsonProperty("Value")
            private double temp;
        }

        @ToString
        @Getter
        @Setter
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Day {
            @JsonProperty("IconPhrase")
            private String iconPhrase;
        }
    }
}