package com.br.votacao.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class RegistrarVotoRequest {

    @NotBlank(message = "CPF é obrigatório")
    @Pattern(regexp = "\\d{11}", message = "CPF deve conter exatamente 11 dígitos numéricos")
    private String cpfAssociado;

    @NotBlank(message = "Voto é obrigatório")
    @Pattern(regexp = "SIM|NAO", flags = Pattern.Flag.CASE_INSENSITIVE,
            message = "O voto deve ser 'SIM' ou 'NAO'")
    private String voto;
}
