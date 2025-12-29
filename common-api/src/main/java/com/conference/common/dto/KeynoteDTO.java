package com.conference.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KeynoteDTO {
    private String id;
    private String nom;
    private String prenom;
    private String email;
    private String fonction;
}
