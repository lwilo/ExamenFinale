package com.conference.conference.controller;

import com.conference.common.commands.CreateConferenceCommand;
import com.conference.common.commands.UpdateConferenceCommand;
import com.conference.common.dto.ConferenceDTO;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/conferences/commands")
@RequiredArgsConstructor
public class ConferenceCommandController {

    private final CommandGateway commandGateway;

    @PostMapping
    public CompletableFuture<ResponseEntity<String>> createConference(@RequestBody ConferenceDTO conferenceDTO) {
        String conferenceId = UUID.randomUUID().toString();
        CreateConferenceCommand command = new CreateConferenceCommand(
                conferenceId,
                conferenceDTO.getTitre(),
                conferenceDTO.getType(),
                conferenceDTO.getDate(),
                conferenceDTO.getDuree(),
                conferenceDTO.getNombreInscrits()
        );
        
        return commandGateway.send(command)
                .thenApply(result -> ResponseEntity.status(HttpStatus.CREATED).body(conferenceId))
                .exceptionally(ex -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Error creating conference: " + ex.getMessage()));
    }

    @PutMapping("/{id}")
    public CompletableFuture<ResponseEntity<String>> updateConference(
            @PathVariable String id,
            @RequestBody ConferenceDTO conferenceDTO) {
        UpdateConferenceCommand command = new UpdateConferenceCommand(
                id,
                conferenceDTO.getTitre(),
                conferenceDTO.getType(),
                conferenceDTO.getDate(),
                conferenceDTO.getDuree(),
                conferenceDTO.getNombreInscrits()
        );
        
        return commandGateway.send(command)
                .thenApply(result -> ResponseEntity.ok("Conference updated successfully"))
                .exceptionally(ex -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Error updating conference: " + ex.getMessage()));
    }
}
