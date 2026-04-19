package br.com.barberdev.api.exception;

public class PhoneInUseException extends RuntimeException {

    public PhoneInUseException(final String phone) {
        super("O telefone '" + phone + "' já está em uso por outro cliente.");
    }
}
