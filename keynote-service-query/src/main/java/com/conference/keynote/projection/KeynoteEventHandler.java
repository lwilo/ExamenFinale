package com.conference.keynote.projection;

import com.conference.common.events.KeynoteCreatedEvent;
import com.conference.common.events.KeynoteUpdatedEvent;
import com.conference.common.events.KeynoteDeletedEvent;
import com.conference.keynote.entity.KeynoteEntity;
import com.conference.keynote.repository.KeynoteRepository;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KeynoteEventHandler {

    private final KeynoteRepository keynoteRepository;

    public KeynoteEventHandler(KeynoteRepository keynoteRepository) {
        this.keynoteRepository = keynoteRepository;
    }

    @EventHandler
    public void on(KeynoteCreatedEvent event) {
        log.info("Handling KeynoteCreatedEvent for id: {}", event.getKeynoteId());
        KeynoteEntity keynote = new KeynoteEntity(
            event.getKeynoteId(),
            event.getNom(),
            event.getPrenom(),
            event.getEmail(),
            event.getFonction()
        );
        keynoteRepository.save(keynote);
    }

    @EventHandler
    public void on(KeynoteUpdatedEvent event) {
        log.info("Handling KeynoteUpdatedEvent for id: {}", event.getKeynoteId());
        keynoteRepository.findById(event.getKeynoteId()).ifPresent(keynote -> {
            keynote.setNom(event.getNom());
            keynote.setPrenom(event.getPrenom());
            keynote.setEmail(event.getEmail());
            keynote.setFonction(event.getFonction());
            keynoteRepository.save(keynote);
        });
    }

    @EventHandler
    public void on(KeynoteDeletedEvent event) {
        log.info("Handling KeynoteDeletedEvent for id: {}", event.getKeynoteId());
        keynoteRepository.deleteById(event.getKeynoteId());
    }
}
