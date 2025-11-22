package org.uex_back.exceptionhandler;

public class EmailAlreadyInUseException extends RuntimeException {
    public EmailAlreadyInUseException(String email) {
        super("E-mail já está em uso: " + email);
    }
}