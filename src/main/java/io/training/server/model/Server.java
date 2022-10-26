package io.training.server.model;

import io.training.server.enumeration.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;


@Entity // On définit la table
@Data  // Lombok - genere les getter et setter - évite code boilerplate
@NoArgsConstructor
@AllArgsConstructor
public class Server {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true) // pas de duplicate dans la db
    @NotEmpty(message = "ipAdress cannot be null or empty") // validator
    private String ipAddress;

    private String name;
    private String memory;
    private String type;
    private String imageUrl;
    private Status status;
}
