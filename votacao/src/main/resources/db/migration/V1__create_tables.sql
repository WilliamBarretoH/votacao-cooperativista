-- Criação da tabela de associados
CREATE TABLE associado (
    id BIGSERIAL PRIMARY KEY,
    cpf VARCHAR(11) NOT NULL UNIQUE, -- CPF sem pontos/hífen
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT now()
);

COMMENT ON TABLE associado IS 'Associados que podem votar nas pautas';
COMMENT ON COLUMN associado.cpf IS 'CPF único de identificação do associado';

-- Criação da tabela de pautas
CREATE TABLE pauta (
    id BIGSERIAL PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    descricao TEXT,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT now()
);

COMMENT ON TABLE pauta IS 'Pautas que serão votadas';
COMMENT ON COLUMN pauta.titulo IS 'Título ou tema da pauta';

-- Criação da tabela de sessões de votação
CREATE TABLE sessao_votacao (
    id BIGSERIAL PRIMARY KEY,
    pauta_id BIGINT NOT NULL UNIQUE, -- cada pauta tem uma única sessão ativa
    data_hora_inicio TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    data_hora_fim TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT now(),

    CONSTRAINT fk_sessao_pauta FOREIGN KEY (pauta_id) REFERENCES pauta(id) ON DELETE CASCADE
);

COMMENT ON TABLE sessao_votacao IS 'Sessões de votação para cada pauta';
COMMENT ON COLUMN sessao_votacao.pauta_id IS 'Chave estrangeira para a pauta';
COMMENT ON COLUMN sessao_votacao.data_hora_fim IS 'Determina o tempo limite da votação';

CREATE TABLE voto (
    id BIGSERIAL PRIMARY KEY,
    sessao_id BIGINT NOT NULL,
    associado_id BIGINT NOT NULL,
    voto VARCHAR(3) NOT NULL CHECK (voto IN ('SIM', 'NAO')),
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT now(),

    CONSTRAINT fk_voto_sessao FOREIGN KEY (sessao_id) REFERENCES sessao_votacao(id) ON DELETE CASCADE,
    CONSTRAINT fk_voto_associado FOREIGN KEY (associado_id) REFERENCES associado(id) ON DELETE CASCADE,
    CONSTRAINT unique_voto_por_pauta UNIQUE (sessao_id, associado_id)
);

COMMENT ON TABLE voto IS 'Votos dos associados em cada sessão';
COMMENT ON COLUMN voto.voto IS 'Voto do associado: SIM ou NAO';
-- Comentário lógico: unique_voto_por_pauta garante 1 voto por associado por sessão

CREATE INDEX idx_voto_sessao_id ON voto(sessao_id);
CREATE INDEX idx_voto_associado_id ON voto(associado_id);

