package com.conference.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConferenceDTO {
    private String id;
    private String titre;
    private String type;
    private LocalDate date;
    private Integer duree;
    private Integer nombreInscrits;
    private Double score;
}
