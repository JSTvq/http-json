package com.kir138.task1.weatherInfo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor

@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherResponse {

    @JsonProperty("DailyForecasts")
    private List<WeatherList> list;

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class WeatherList {

        @JsonProperty("Date")
        private String dateText;

        @JsonProperty("Temperature")
        private Temperature temperature;

        @JsonProperty("Day")
        private Day day;

        @Getter
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Temperature {

            @JsonProperty("Maximum")
            private Value value;
        }

        @Getter
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Value {

            @JsonProperty("Value")
            private double temp;
        }

        @Getter
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Day {

            @JsonProperty("IconPhrase")
            private String iconPhrase;
        }
    }
}