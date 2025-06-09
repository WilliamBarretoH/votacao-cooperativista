package com.br.votacao.repository;

import com.br.votacao.model.Associado;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Repository
public interface AssociadoRepository extends JpaRepository<Associado, Long> {

    //Mesmo não sendo obrigatório o uso de um Lock, pois o banco possui constrainsts,
    //achei interessante mostrar sua implementação para evitar Race condition
    //No caso 2 requisiçoes simultaneas olhando para o mesmo objeto não consguem votar
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Associado> findByCpf(String cpf);

    boolean existsByCpf(String cpf);
}
