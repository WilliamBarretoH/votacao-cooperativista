package com.br.votacao.repository;

import com.br.votacao.model.ResultadoVotacao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResultadoVotacaoRepository extends JpaRepository<ResultadoVotacao, Long> {
}
