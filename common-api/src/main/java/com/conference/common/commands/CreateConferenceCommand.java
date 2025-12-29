package com.conference.common.commands;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateConferenceCommand {
    @TargetAggregateIdentifier
    private String conferenceId;
    private String titre;
    private String type;
    private LocalDate date;
    private Integer duree;
    private Integer nombreInscrits;
}
