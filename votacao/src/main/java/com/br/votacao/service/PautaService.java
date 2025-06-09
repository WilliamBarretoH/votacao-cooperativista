package com.br.votacao.service;

import com.br.votacao.api.dto.request.CreatePautaRequest;
import com.br.votacao.api.dto.response.PautaResponse;
import com.br.votacao.model.Pauta;
import com.br.votacao.repository.PautaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PautaService {

    private final PautaRepository pautaRepository;

    public PautaResponse criarPauta(CreatePautaRequest request) {
        // Converte o DTO para entidade
        Pauta pauta = Pauta.builder()
                .titulo(request.getTitulo())
                .descricao(request.getDescricao())
                .build();

        // Salva no banco
        Pauta pautaSalva = pautaRepository.save(pauta);

        // Converte para resposta
        PautaResponse response = new PautaResponse();
        response.setId(pautaSalva.getId());
        response.setTitulo(pautaSalva.getTitulo());
        response.setDescricao(pautaSalva.getDescricao());

        return response;
    }

    public List<PautaResponse> listarPautas() {
        return pautaRepository.findAll()
                .stream()
                .map(p -> {
                    PautaResponse pautaResponse = new PautaResponse();
                    pautaResponse.setId(p.getId());
                    pautaResponse.setTitulo(p.getTitulo());
                    pautaResponse.setDescricao(p.getDescricao());
                    return pautaResponse;
                })
                .toList(); // ou .collect(Collectors.toList()) em versões anteriores ao Java 16
    }
}
