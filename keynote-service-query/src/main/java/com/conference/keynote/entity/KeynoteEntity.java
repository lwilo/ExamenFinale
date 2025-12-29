package com.conference.keynote.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "keynotes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KeynoteEntity {
    @Id
    private String id;
    private String nom;
    private String prenom;
    private String email;
    private String fonction;
}
