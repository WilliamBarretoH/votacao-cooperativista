package com.br.votacao.model;

import com.br.votacao.model.enums.ResultadoStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "resultado_votacao")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultadoVotacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Pauta pauta;

    private Long totalVotos;
    private Long votosSim;
    private Long votosNao;

    @Enumerated(EnumType.STRING)
    private ResultadoStatus resultado; // APROVADA, REJEITADA, EMPATE

    private LocalDateTime encerradaEm;
}
