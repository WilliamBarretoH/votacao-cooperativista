package com.br.votacao.repository;

import com.br.votacao.model.Pauta;
import com.br.votacao.model.SessaoVotacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SessaoVotacaoRepository extends JpaRepository<SessaoVotacao, Long> {
    Optional<SessaoVotacao> findByPauta(Pauta pauta);
    boolean existsByPauta(Pauta pauta);
}