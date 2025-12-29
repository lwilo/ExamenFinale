package com.conference.keynote.controller;

import com.conference.common.dto.KeynoteDTO;
import com.conference.common.queries.GetAllKeynotesQuery;
import com.conference.common.queries.GetKeynoteByIdQuery;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/keynotes")
@CrossOrigin("*")
public class KeynoteQueryController {

    private final QueryGateway queryGateway;

    public KeynoteQueryController(QueryGateway queryGateway) {
        this.queryGateway = queryGateway;
    }

    @GetMapping("/all")
    public CompletableFuture<List<KeynoteDTO>> getAllKeynotes() {
        return queryGateway.query(
            new GetAllKeynotesQuery(),
            ResponseTypes.multipleInstancesOf(KeynoteDTO.class)
        );
    }

    @GetMapping("/{id}")
    public CompletableFuture<ResponseEntity<KeynoteDTO>> getKeynoteById(@PathVariable String id) {
        return queryGateway.query(
            new GetKeynoteByIdQuery(id),
            ResponseTypes.instanceOf(KeynoteDTO.class)
        ).thenApply(keynote -> 
            keynote != null ? ResponseEntity.ok(keynote) : ResponseEntity.notFound().build()
        );
    }
}
