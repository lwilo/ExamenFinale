package com.conference.keynote.aggregate;

import com.conference.common.commands.CreateKeynoteCommand;
import com.conference.common.commands.UpdateKeynoteCommand;
import com.conference.common.commands.DeleteKeynoteCommand;
import com.conference.common.events.KeynoteCreatedEvent;
import com.conference.common.events.KeynoteUpdatedEvent;
import com.conference.common.events.KeynoteDeletedEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

@Aggregate
public class KeynoteAggregate {

    @AggregateIdentifier
    private String keynoteId;
    private String nom;
    private String prenom;
    private String email;
    private String fonction;

    public KeynoteAggregate() {
    }

    @CommandHandler
    public KeynoteAggregate(CreateKeynoteCommand command) {
        AggregateLifecycle.apply(new KeynoteCreatedEvent(
            command.getKeynoteId(),
            command.getNom(),
            command.getPrenom(),
            command.getEmail(),
            command.getFonction()
        ));
    }

    @CommandHandler
    public void handle(UpdateKeynoteCommand command) {
        AggregateLifecycle.apply(new KeynoteUpdatedEvent(
            command.getKeynoteId(),
            command.getNom(),
            command.getPrenom(),
            command.getEmail(),
            command.getFonction()
        ));
    }

    @CommandHandler
    public void handle(DeleteKeynoteCommand command) {
        AggregateLifecycle.apply(new KeynoteDeletedEvent(command.getKeynoteId()));
    }

    @EventSourcingHandler
    public void on(KeynoteCreatedEvent event) {
        this.keynoteId = event.getKeynoteId();
        this.nom = event.getNom();
        this.prenom = event.getPrenom();
        this.email = event.getEmail();
        this.fonction = event.getFonction();
    }

    @EventSourcingHandler
    public void on(KeynoteUpdatedEvent event) {
        this.nom = event.getNom();
        this.prenom = event.getPrenom();
        this.email = event.getEmail();
        this.fonction = event.getFonction();
    }

    @EventSourcingHandler
    public void on(KeynoteDeletedEvent event) {
        // Mark for deletion
    }
}
