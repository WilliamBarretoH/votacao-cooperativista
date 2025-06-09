package com.br.votacao.api.dto.response;

import com.br.votacao.api.dto.request.RegistrarVotoRequest;
import com.br.votacao.service.VotoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/pautas")
@RequiredArgsConstructor
public class VotoResource {

    private final VotoService votoService;

    @PostMapping("/{pautaId}/votos")
    public ResponseEntity<String> registrarVoto(
            @PathVariable Long pautaId,
            @Valid @RequestBody RegistrarVotoRequest request
    ) {
        votoService.registrarVoto(pautaId, request);
        return ResponseEntity.status(201).body("Voto registrado com sucesso!");
    }
}
