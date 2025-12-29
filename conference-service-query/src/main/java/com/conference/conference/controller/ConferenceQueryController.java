package com.conference.conference.controller;

import com.conference.common.dto.ConferenceDTO;
import com.conference.common.queries.GetAllConferencesQuery;
import com.conference.common.queries.GetConferenceByIdQuery;
import lombok.RequiredArgsConstructor;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/conferences/queries")
@RequiredArgsConstructor
public class ConferenceQueryController {

    private final QueryGateway queryGateway;

    @GetMapping
    public CompletableFuture<ResponseEntity<List<ConferenceDTO>>> getAllConferences() {
        return queryGateway.query(
                new GetAllConferencesQuery(),
                ResponseTypes.multipleInstancesOf(ConferenceDTO.class)
        ).thenApply(ResponseEntity::ok);
    }

    @GetMapping("/{id}")
    public CompletableFuture<ResponseEntity<ConferenceDTO>> getConferenceById(@PathVariable String id) {
        return queryGateway.query(
                new GetConferenceByIdQuery(id),
                ResponseTypes.instanceOf(ConferenceDTO.class)
        ).thenApply(ResponseEntity::ok)
         .exceptionally(ex -> ResponseEntity.notFound().build());
    }
}
