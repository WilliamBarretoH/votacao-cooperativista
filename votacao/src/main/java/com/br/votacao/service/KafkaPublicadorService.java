package com.br.votacao.service;

import com.br.votacao.event.ResultadoVotacaoEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaPublicadorService {

    private static final String TOPICO = "resultado-votacao";

    private final KafkaTemplate<String, ResultadoVotacaoEvent> kafkaTemplate;

    public void publicarResultado(ResultadoVotacaoEvent event) {
        kafkaTemplate.send(TOPICO, event.getPautaId().toString(), event);
        log.info("Resultado da pauta {} enviado ao tópico Kafka: {}", event.getPautaId(), TOPICO);
    }
}