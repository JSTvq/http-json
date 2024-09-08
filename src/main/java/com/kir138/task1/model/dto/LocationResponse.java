package com.kir138.task1.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
public class LocationResponse {

    @JsonProperty("Key")
    private String key;
    @JsonProperty("LocalizedName")
    private String localizedName;
    @JsonProperty("Country")
    private Country country;

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Getter
    public static class Country {
        @JsonProperty("LocalizedName")
        private String localizedName;
    }
}


