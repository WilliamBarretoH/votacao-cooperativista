package com.br.votacao.service;

import com.br.votacao.api.dto.request.AbrirSessaoRequest;
import com.br.votacao.api.dto.response.SessaoVotacaoResponse;
import com.br.votacao.exception.RecursoNaoEncontradoException;
import com.br.votacao.exception.SessaoParaPautaJaExisteException;
import com.br.votacao.model.Pauta;
import com.br.votacao.model.SessaoVotacao;
import com.br.votacao.repository.PautaRepository;
import com.br.votacao.repository.SessaoVotacaoRepository;
import com.br.votacao.service.QuartzSchedulerService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@RequiredArgsConstructor
@Service
public class SessaoVotacaoService {

    private final SessaoVotacaoRepository sessaoVotacaoRepository;
    private final PautaRepository pautaRepository;
    private final QuartzSchedulerService quartzSchedulerService;

    @Transactional
    public SessaoVotacaoResponse abrirSessao(Long pautaId, AbrirSessaoRequest request) {
        Pauta pauta = pautaRepository.findById(pautaId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Pauta não encontrada"));

        if (sessaoVotacaoRepository.existsByPauta(pauta)) {
            throw new SessaoParaPautaJaExisteException("Já existe uma sessão aberta para essa pauta");
        }

        LocalDateTime inicio = LocalDateTime.now();
        LocalDateTime fim = inicio.plusMinutes(request.getDuracao() != null ? request.getDuracao() : 1);

        SessaoVotacao sessao = SessaoVotacao.builder()
                .pauta(pauta)
                .dataHoraInicio(inicio)
                .dataHoraFim(fim)
                .encerrada(false)
                .build();

        SessaoVotacao sessaoVotacao = sessaoVotacaoRepository.save(sessao);

        // Agenda o encerramento via Quartz
        quartzSchedulerService.agendarEncerramentoSessao(
                sessaoVotacao.getId(),
                Date.from(fim.atZone(ZoneId.systemDefault()).toInstant())
        );

        return new SessaoVotacaoResponse(sessaoVotacao.getId(), pautaId, sessaoVotacao.getDataHoraInicio(), sessaoVotacao.getDataHoraFim());
    }
}