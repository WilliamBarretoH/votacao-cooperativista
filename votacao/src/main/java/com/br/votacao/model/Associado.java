package com.br.votacao.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "associado")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Associado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 11, nullable = false, unique = true)
    private String cpf;

    @Column(name = "created_at")
    private final LocalDateTime createdAt = LocalDateTime.now();
}
