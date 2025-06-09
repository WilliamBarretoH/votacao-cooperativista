package com.br.votacao.event;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResultadoVotacaoEvent {
    private Long pautaId;
    private String tituloPauta;
    private Long votosSim;
    private Long votosNao;
    private Long totalVotos;
    private String resultado; // APROVADA, REJEITADA, EMPATE
    private LocalDateTime encerradaEm;
}
