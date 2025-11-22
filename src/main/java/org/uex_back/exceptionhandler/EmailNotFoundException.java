package org.uex_back.exceptionhandler;

public class EmailNotFoundException extends RuntimeException {
    public EmailNotFoundException() {
        super("e-mail não encontrado.");
    }
}
