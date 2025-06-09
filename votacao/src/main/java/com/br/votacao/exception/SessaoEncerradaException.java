package com.br.votacao.exception;

public class SessaoEncerradaException extends RuntimeException {
    public SessaoEncerradaException(String message) {
        super(message);
    }
}
