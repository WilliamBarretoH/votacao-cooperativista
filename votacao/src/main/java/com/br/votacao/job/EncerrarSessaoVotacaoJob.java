package com.br.votacao.job;

import com.br.votacao.event.ResultadoVotacaoEvent;
import com.br.votacao.model.ResultadoVotacao;
import com.br.votacao.model.SessaoVotacao;
import com.br.votacao.model.Voto;
import com.br.votacao.model.enums.ResultadoStatus;
import com.br.votacao.model.enums.TipoVoto;
import com.br.votacao.repository.ResultadoVotacaoRepository;
import com.br.votacao.repository.SessaoVotacaoRepository;
//import com.br.votacao.service.KafkaPublicadorService;
import com.br.votacao.repository.VotoRepository;
import com.br.votacao.service.KafkaPublicadorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class EncerrarSessaoVotacaoJob implements Job {

    private final SessaoVotacaoRepository sessaoVotacaoRepository;
    private final VotoRepository votoRepository;
    private final ResultadoVotacaoRepository resultadoVotacaoRepository;
    private final KafkaPublicadorService kafkaPublicadorService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        Long sessaoId = context.getJobDetail().getJobDataMap().getLong("sessaoId");
        log.info("Executando job para encerrar sessão de votação com ID: {}", sessaoId);

        Optional<SessaoVotacao> optionalSessao = sessaoVotacaoRepository.findById(sessaoId);

        if (optionalSessao.isPresent()) {
            SessaoVotacao sessao = optionalSessao.get();

            if (sessao.getEncerrada()) {
                log.info("Sessão de ID {} já foi encerrada anteriormente", sessaoId);
                return;
            }

            List<Voto> votos = votoRepository.findAllBySessao(sessao);
            long votosSim = votos.stream().filter(v -> v.getVoto() == TipoVoto.SIM).count();
            long votosNao = votos.stream().filter(v -> v.getVoto() == TipoVoto.NAO).count();
            long total = votos.size();

            ResultadoStatus status;
            if (votosSim > votosNao) {
                status = ResultadoStatus.APROVADA;
            } else if (votosNao > votosSim) {
                status = ResultadoStatus.REJEITADA;
            } else {
                status = ResultadoStatus.EMPATE;
            }

            ResultadoVotacao resultadoVotacao = buildResultadoVotacao(sessao, status, votosNao, votosSim, total);

            resultadoVotacaoRepository.save(resultadoVotacao);

            // Encerrar a sessão
            sessao.setEncerrada(true);
            sessaoVotacaoRepository.save(sessao);

            ResultadoVotacaoEvent resultadoVotacaoEvent = buildResultadoVotacaoEvent(sessao, votosSim, votosNao, total, status);
            // Publicar resultado no Kafka
            kafkaPublicadorService.publicarResultado(resultadoVotacaoEvent);
            log.info("Sessão encerrada e resultado publicado com sucesso.");
        } else {
            log.warn("Sessão de votação com ID {} não encontrada", sessaoId);
        }
    }

    private static ResultadoVotacaoEvent buildResultadoVotacaoEvent(SessaoVotacao sessao, long votosSim, long votosNao, long total, ResultadoStatus status) {
        ResultadoVotacaoEvent resultadoVotacaoEvent = ResultadoVotacaoEvent.builder()
                .pautaId(sessao.getPauta().getId())
                .tituloPauta(sessao.getPauta().getTitulo())
                .votosSim(votosSim)
                .votosNao(votosNao)
                .totalVotos(total)
                .resultado(status.name())
                .encerradaEm(sessao.getDataHoraFim())
                .build();
        return resultadoVotacaoEvent;
    }

    private static ResultadoVotacao buildResultadoVotacao(SessaoVotacao sessao, ResultadoStatus status, long votosNao, long votosSim, long total) {
        ResultadoVotacao resultadoVotacao = new ResultadoVotacao();
        resultadoVotacao.setPauta(sessao.getPauta());
        resultadoVotacao.setResultado(status);
        resultadoVotacao.setEncerradaEm(sessao.getDataHoraFim());
        resultadoVotacao.setVotosNao(votosNao);
        resultadoVotacao.setVotosSim(votosSim);
        resultadoVotacao.setTotalVotos(total);
        return resultadoVotacao;
    }
}
