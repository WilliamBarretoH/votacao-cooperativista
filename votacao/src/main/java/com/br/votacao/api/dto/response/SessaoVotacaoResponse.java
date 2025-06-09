package com.br.votacao.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SessaoVotacaoResponse {

    private Long id;
    private Long pautaId;
    private LocalDateTime dataHoraInicio;
    private LocalDateTime dataHoraFim;
}