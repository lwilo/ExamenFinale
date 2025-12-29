package com.conference.conference.aggregate;

import com.conference.common.commands.CreateConferenceCommand;
import com.conference.common.commands.UpdateConferenceCommand;
import com.conference.common.events.ConferenceCreatedEvent;
import com.conference.common.events.ConferenceUpdatedEvent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import java.time.LocalDate;

@Aggregate
@NoArgsConstructor
@Getter
public class ConferenceAggregate {

    @AggregateIdentifier
    private String conferenceId;
    private String titre;
    private String type;
    private LocalDate date;
    private Integer duree;
    private Integer nombreInscrits;
    private Double score;

    @CommandHandler
    public ConferenceAggregate(CreateConferenceCommand command) {
        // Validation
        if (command.getTitre() == null || command.getTitre().isEmpty()) {
            throw new IllegalArgumentException("Le titre ne peut pas être vide");
        }
        if (command.getDuree() == null || command.getDuree() <= 0) {
            throw new IllegalArgumentException("La durée doit être positive");
        }

        // Emit event
        ConferenceCreatedEvent event = new ConferenceCreatedEvent(
                command.getConferenceId(),
                command.getTitre(),
                command.getType(),
                command.getDate(),
                command.getDuree(),
                command.getNombreInscrits(),
                0.0 // Initial score
        );
        AggregateLifecycle.apply(event);
    }

    @CommandHandler
    public void handle(UpdateConferenceCommand command) {
        // Validation
        if (command.getTitre() == null || command.getTitre().isEmpty()) {
            throw new IllegalArgumentException("Le titre ne peut pas être vide");
        }

        // Emit event
        ConferenceUpdatedEvent event = new ConferenceUpdatedEvent(
                command.getConferenceId(),
                command.getTitre(),
                command.getType(),
                command.getDate(),
                command.getDuree(),
                command.getNombreInscrits()
        );
        AggregateLifecycle.apply(event);
    }

    @EventSourcingHandler
    public void on(ConferenceCreatedEvent event) {
        this.conferenceId = event.getConferenceId();
        this.titre = event.getTitre();
        this.type = event.getType();
        this.date = event.getDate();
        this.duree = event.getDuree();
        this.nombreInscrits = event.getNombreInscrits();
        this.score = event.getScore();
    }

    @EventSourcingHandler
    public void on(ConferenceUpdatedEvent event) {
        this.titre = event.getTitre();
        this.type = event.getType();
        this.date = event.getDate();
        this.duree = event.getDuree();
        this.nombreInscrits = event.getNombreInscrits();
    }
}
