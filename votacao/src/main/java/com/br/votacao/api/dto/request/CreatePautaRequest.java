package com.br.votacao.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreatePautaRequest {

    @NotBlank(message = "Título da pauta é obrigatório")
    private String titulo;

    private String descricao;
}