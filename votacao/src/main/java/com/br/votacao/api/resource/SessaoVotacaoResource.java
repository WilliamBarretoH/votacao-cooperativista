package com.br.votacao.api.resource;

import com.br.votacao.api.dto.request.AbrirSessaoRequest;
import com.br.votacao.api.dto.response.SessaoVotacaoResponse;
import com.br.votacao.service.SessaoVotacaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/sessoes")
@RequiredArgsConstructor
public class SessaoVotacaoResource{

    private final SessaoVotacaoService sessaoVotacaoService;

    @PostMapping("/pauta/{pautaId}")
    public ResponseEntity<SessaoVotacaoResponse> abrirSessao(
            @PathVariable Long pautaId,
            @Valid @RequestBody(required = false) AbrirSessaoRequest request
    ) {
        if (request == null) {
            request = new AbrirSessaoRequest(); // usa duração padrão
        }

        SessaoVotacaoResponse response = sessaoVotacaoService.abrirSessao(pautaId, request);
        return ResponseEntity.status(201).body(response);
    }
}
