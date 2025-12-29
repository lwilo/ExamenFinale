package com.conference.conference.queryhandler;

import com.conference.common.dto.ConferenceDTO;
import com.conference.common.queries.GetAllConferencesQuery;
import com.conference.common.queries.GetConferenceByIdQuery;
import com.conference.conference.entity.Conference;
import com.conference.conference.repository.ConferenceRepository;
import lombok.RequiredArgsConstructor;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ConferenceQueryHandler {

    private final ConferenceRepository conferenceRepository;

    @QueryHandler
    public List<ConferenceDTO> handle(GetAllConferencesQuery query) {
        return conferenceRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @QueryHandler
    public ConferenceDTO handle(GetConferenceByIdQuery query) {
        Conference conference = conferenceRepository.findById(query.getConferenceId())
                .orElseThrow(() -> new RuntimeException("Conference not found: " + query.getConferenceId()));
        return toDTO(conference);
    }

    private ConferenceDTO toDTO(Conference conference) {
        return new ConferenceDTO(
                conference.getId(),
                conference.getTitre(),
                conference.getType(),
                conference.getDate(),
                conference.getDuree(),
                conference.getNombreInscrits(),
                conference.getScore()
        );
    }
}
