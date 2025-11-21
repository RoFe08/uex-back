package org.uex_back.exceptionhandler;

public class CpfAlreadyInUseException extends RuntimeException {
    public CpfAlreadyInUseException(String cpf) {
        super("CPF já está em uso: " + cpf);
    }
}