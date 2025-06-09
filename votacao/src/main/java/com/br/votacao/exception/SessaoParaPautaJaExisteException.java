package com.br.votacao.exception;

public class SessaoParaPautaJaExisteException extends RuntimeException {
    public SessaoParaPautaJaExisteException(String message) {
        super(message);
    }
}
