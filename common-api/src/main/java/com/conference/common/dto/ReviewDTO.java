package com.conference.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO {
    private String id;
    private String conferenceId;
    private LocalDateTime date;
    private String texte;
    private Integer note;
}
