package com.br.votacao.api.dto.request;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class AbrirSessaoRequest {

    @Min(value = 1, message = "A duração mínima é de 1 minuto")
    private Integer duracao; // duração em minutos
}
