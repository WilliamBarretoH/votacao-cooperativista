package com.br.votacao.repository;

import com.br.votacao.model.Associado;
import com.br.votacao.model.SessaoVotacao;
import com.br.votacao.model.Voto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VotoRepository extends JpaRepository<Voto, Long> {
    boolean existsBySessaoAndAssociado(SessaoVotacao sessao, Associado associado);
    Optional<Voto> findBySessaoAndAssociado(SessaoVotacao sessao, Associado associado);
    List<Voto> findAllBySessao(SessaoVotacao sessao);
}
