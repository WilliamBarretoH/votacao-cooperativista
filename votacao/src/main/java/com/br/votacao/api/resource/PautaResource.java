package com.br.votacao.api.resource;

import com.br.votacao.api.dto.request.CreatePautaRequest;
import com.br.votacao.api.dto.response.PautaResponse;
import com.br.votacao.service.PautaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pautas")
@RequiredArgsConstructor
public class PautaResource {

    private final PautaService pautaService;

    @PostMapping
    public ResponseEntity<PautaResponse> criar(@Valid @RequestBody CreatePautaRequest request) {
        PautaResponse response = pautaService.criarPauta(request);
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping
    public ResponseEntity<List<PautaResponse>> listar() {
        return ResponseEntity.ok(pautaService.listarPautas());
    }
}
