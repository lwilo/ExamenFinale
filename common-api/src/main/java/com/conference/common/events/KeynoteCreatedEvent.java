package com.conference.common.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KeynoteCreatedEvent {
    private String keynoteId;
    private String nom;
    private String prenom;
    private String email;
    private String fonction;
}
