package org.uex_back.exceptionhandler;

public class InvalidPasswordException extends RuntimeException {
    public InvalidPasswordException() {
        super("Senha inválida.");
    }
}
