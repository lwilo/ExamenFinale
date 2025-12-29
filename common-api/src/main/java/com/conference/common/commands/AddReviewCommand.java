package com.conference.common.commands;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddReviewCommand {
    private String reviewId;
    @TargetAggregateIdentifier
    private String conferenceId;
    private String texte;
    private Integer note;
}
