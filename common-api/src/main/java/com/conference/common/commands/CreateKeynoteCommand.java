package com.conference.common.commands;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateKeynoteCommand {
    @TargetAggregateIdentifier
    private String keynoteId;
    private String nom;
    private String prenom;
    private String email;
    private String fonction;
}
