package com.conference.conference.eventhandler;

import com.conference.common.events.ConferenceCreatedEvent;
import com.conference.common.events.ConferenceUpdatedEvent;
import com.conference.conference.entity.Conference;
import com.conference.conference.repository.ConferenceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ConferenceEventHandler {

    private final ConferenceRepository conferenceRepository;

    @EventHandler
    public void on(ConferenceCreatedEvent event) {
        log.info("Handling ConferenceCreatedEvent for id: {}", event.getConferenceId());
        Conference conference = new Conference(
                event.getConferenceId(),
                event.getTitre(),
                event.getType(),
                event.getDate(),
                event.getDuree(),
                event.getNombreInscrits(),
                event.getScore()
        );
        conferenceRepository.save(conference);
    }

    @EventHandler
    public void on(ConferenceUpdatedEvent event) {
        log.info("Handling ConferenceUpdatedEvent for id: {}", event.getConferenceId());
        conferenceRepository.findById(event.getConferenceId()).ifPresent(conference -> {
            conference.setTitre(event.getTitre());
            conference.setType(event.getType());
            conference.setDate(event.getDate());
            conference.setDuree(event.getDuree());
            conference.setNombreInscrits(event.getNombreInscrits());
            conferenceRepository.save(conference);
        });
    }
}
