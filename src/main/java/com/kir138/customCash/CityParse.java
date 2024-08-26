package com.kir138.customCash;

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
public class CityParse {
    private List<InnerCity> cities;

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class KeyCity {
        @JsonProperty("Key")
        private String key;
    }

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Country {
        @JsonProperty("LocalizedName")
        private String country1;
    }

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class HeaderCountry {
        @JsonProperty("Country")
        private InnerCity country2;
    }

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class InnerCity {
        @JsonProperty("LocalizedName")
        private String city;
    }
}

