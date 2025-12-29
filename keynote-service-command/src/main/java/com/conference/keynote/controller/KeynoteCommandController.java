package com.conference.keynote.controller;

import com.conference.common.commands.CreateKeynoteCommand;
import com.conference.common.commands.UpdateKeynoteCommand;
import com.conference.common.commands.DeleteKeynoteCommand;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/keynotes")
@CrossOrigin("*")
public class KeynoteCommandController {

    private final CommandGateway commandGateway;

    public KeynoteCommandController(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    @PostMapping
    public CompletableFuture<ResponseEntity<String>> createKeynote(@RequestBody CreateKeynoteRequest request) {
        String id = UUID.randomUUID().toString();
        CreateKeynoteCommand command = new CreateKeynoteCommand(
            id,
            request.getNom(),
            request.getPrenom(),
            request.getEmail(),
            request.getFonction()
        );
        
        return commandGateway.send(command)
            .thenApply(result -> ResponseEntity.status(HttpStatus.CREATED).body(id));
    }

    @PutMapping("/{id}")
    public CompletableFuture<ResponseEntity<String>> updateKeynote(
            @PathVariable String id,
            @RequestBody UpdateKeynoteRequest request) {
        UpdateKeynoteCommand command = new UpdateKeynoteCommand(
            id,
            request.getNom(),
            request.getPrenom(),
            request.getEmail(),
            request.getFonction()
        );
        
        return commandGateway.send(command)
            .thenApply(result -> ResponseEntity.ok(id));
    }

    @DeleteMapping("/{id}")
    public CompletableFuture<ResponseEntity<Void>> deleteKeynote(@PathVariable String id) {
        DeleteKeynoteCommand command = new DeleteKeynoteCommand(id);
        
        return commandGateway.send(command)
            .thenApply(result -> ResponseEntity.noContent().<Void>build());
    }
}

// DTOs for requests
class CreateKeynoteRequest {
    private String nom;
    private String prenom;
    private String email;
    private String fonction;

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getFonction() { return fonction; }
    public void setFonction(String fonction) { this.fonction = fonction; }
}

class UpdateKeynoteRequest {
    private String nom;
    private String prenom;
    private String email;
    private String fonction;

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getFonction() { return fonction; }
    public void setFonction(String fonction) { this.fonction = fonction; }
}
