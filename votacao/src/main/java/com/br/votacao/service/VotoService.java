package com.br.votacao.service;

import com.br.votacao.api.dto.request.RegistrarVotoRequest;
import com.br.votacao.client.CpfValidationClient;
import com.br.votacao.client.dto.CpfValidationResponse;
import com.br.votacao.exception.RecursoNaoEncontradoException;
import com.br.votacao.exception.SessaoEncerradaException;
import com.br.votacao.exception.VotoDuplicadoException;
import com.br.votacao.exception.AssociadoNaoEncontradoException;
import com.br.votacao.model.Associado;
import com.br.votacao.model.Pauta;
import com.br.votacao.model.SessaoVotacao;
import com.br.votacao.model.Voto;
import com.br.votacao.model.enums.TipoVoto;
import com.br.votacao.repository.AssociadoRepository;
import com.br.votacao.repository.PautaRepository;
import com.br.votacao.repository.SessaoVotacaoRepository;
import com.br.votacao.repository.VotoRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class VotoService {

    private final PautaRepository pautaRepository;
    private final SessaoVotacaoRepository sessaoVotacaoRepository;
    private final AssociadoRepository associadoRepository;
    private final VotoRepository votoRepository;
    private final CpfValidationClient cpfValidationClient;

    //Porque usar @Transactional
    //Cria uma única transação de banco de dados englobando todas essas operações
    //Garante que tudo aconteça ou nada aconteça (ACID)
    //Garante que o banco só libere os Locks no final do commit
    //Colabora com a constraint UNIQUE(sessao_id, associado_id) para garantir atomicidade
    @Transactional
    public void registrarVoto(Long pautaId, RegistrarVotoRequest request) {
        Pauta pauta = pautaRepository.findById(pautaId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Pauta não encontrada"));

        SessaoVotacao sessao = sessaoVotacaoRepository.findByPauta(pauta)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Sessão de votação não encontrada para essa pauta"));

        String cpf = request.getCpfAssociado().replaceAll("[^\\d]", "");
        Associado associado = associadoRepository.findByCpf(cpf).orElseThrow(() -> new AssociadoNaoEncontradoException("Associado não encontrado"));

        //Salvando qualquer cpf no momento do voto para fins de teste de carga
        //Associado associado = associadoRepository.findByCpf(cpf).orElseGet(() -> associadoRepository.save(Associado.builder().cpf(cpf).build()));

        LocalDateTime agora = LocalDateTime.now();
        if (agora.isBefore(sessao.getDataHoraInicio()) || agora.isAfter(sessao.getDataHoraFim())) {
            throw new SessaoEncerradaException("A sessão de votação está encerrada para esta pauta");
        }

        if (votoRepository.existsBySessaoAndAssociado(sessao, associado)) {
            throw new VotoDuplicadoException("Esse associado já votou nessa pauta");
        }

        if (!isCpfHabilitado(cpf)) {
            throw new IllegalArgumentException("CPF inválido ou não autorizado a votar");
        }

        TipoVoto tipoVoto = TipoVoto.valueOf(request.getVoto().toUpperCase());

        Voto voto = Voto.builder()
                .associado(associado)
                .sessao(sessao)
                .voto(tipoVoto)
                .build();

        try {
            votoRepository.save(voto);
        } catch (DataIntegrityViolationException e) {
            throw new VotoDuplicadoException("Esse associado já votou nessa pauta");
        }
    }

    private Boolean isCpfHabilitado(String cpf) {
        try {
            //Exemplo da chamada ao feign client
            //ResponseEntity<CpfValidationResponse> response = cpfValidationClient.validarCpf(cpf);
            //return response.getBody().getStatus().equals("ABLE_TO_VOTE");
            return Boolean.TRUE;
        } catch (Exception e) {
            return Boolean.FALSE;
        }
    }
}
