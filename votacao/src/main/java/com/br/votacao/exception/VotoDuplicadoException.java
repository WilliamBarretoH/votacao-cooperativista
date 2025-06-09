package com.br.votacao.exception;

public class VotoDuplicadoException extends RuntimeException {
    public VotoDuplicadoException(String message) {
        super(message);
    }
}
