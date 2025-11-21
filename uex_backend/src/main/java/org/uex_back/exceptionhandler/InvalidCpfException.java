package org.uex_back.exceptionhandler;

public class InvalidCpfException extends RuntimeException {
    public InvalidCpfException() {
        super("CPF inválido.");
    }
}
