
CREATE TABLE resultado_votacao (
    id SERIAL PRIMARY KEY,
    pauta_id BIGINT NOT NULL UNIQUE,
    total_votos BIGINT,
    votos_sim BIGINT,
    votos_nao BIGINT,
    resultado VARCHAR(20),
    encerrada_em TIMESTAMP,

    CONSTRAINT fk_pauta_resultado FOREIGN KEY (pauta_id) REFERENCES pauta(id)
);
