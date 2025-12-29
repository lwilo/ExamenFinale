package com.conference.keynote.query;

import com.conference.common.dto.KeynoteDTO;
import com.conference.common.queries.GetAllKeynotesQuery;
import com.conference.common.queries.GetKeynoteByIdQuery;
import com.conference.keynote.entity.KeynoteEntity;
import com.conference.keynote.repository.KeynoteRepository;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class KeynoteQueryHandler {

    private final KeynoteRepository keynoteRepository;

    public KeynoteQueryHandler(KeynoteRepository keynoteRepository) {
        this.keynoteRepository = keynoteRepository;
    }

    @QueryHandler
    public List<KeynoteDTO> handle(GetAllKeynotesQuery query) {
        log.info("Handling GetAllKeynotesQuery");
        return keynoteRepository.findAll().stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

    @QueryHandler
    public KeynoteDTO handle(GetKeynoteByIdQuery query) {
        log.info("Handling GetKeynoteByIdQuery for id: {}", query.getKeynoteId());
        return keynoteRepository.findById(query.getKeynoteId())
            .map(this::toDTO)
            .orElse(null);
    }

    private KeynoteDTO toDTO(KeynoteEntity entity) {
        return new KeynoteDTO(
            entity.getId(),
            entity.getNom(),
            entity.getPrenom(),
            entity.getEmail(),
            entity.getFonction()
        );
    }
}
