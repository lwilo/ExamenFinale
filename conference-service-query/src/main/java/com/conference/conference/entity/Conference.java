package com.conference.conference.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Conference {
    @Id
    private String id;
    private String titre;
    private String type;
    private LocalDate date;
    private Integer duree;
    private Integer nombreInscrits;
    private Double score;
}
