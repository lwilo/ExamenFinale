package com.conference.common.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewAddedEvent {
    private String reviewId;
    private String conferenceId;
    private LocalDateTime date;
    private String texte;
    private Integer note;
}
