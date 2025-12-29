package com.conference.common.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConferenceUpdatedEvent {
    private String conferenceId;
    private String titre;
    private String type;
    private LocalDate date;
    private Integer duree;
    private Integer nombreInscrits;
}
