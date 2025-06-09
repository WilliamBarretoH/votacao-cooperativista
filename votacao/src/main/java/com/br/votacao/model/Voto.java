package com.br.votacao.model;

import com.br.votacao.model.enums.TipoVoto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "voto", uniqueConstraints = {
        @UniqueConstraint(name = "unique_voto_por_pauta", columnNames = {"sessao_id", "associado_id"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Voto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sessao_id", nullable = false)
    private SessaoVotacao sessao;

    @ManyToOne
    @JoinColumn(name = "associado_id", nullable = false)
    private Associado associado;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 3)
    private TipoVoto voto;

    @Column(name = "created_at")
    private final LocalDateTime createdAt = LocalDateTime.now();
}
