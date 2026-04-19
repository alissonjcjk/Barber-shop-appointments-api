package br.com.barberdev.api.exception;

public class EmailInUseException extends RuntimeException {

    public EmailInUseException(final String email) {
        super("O e-mail '" + email + "' já está em uso por outro cliente.");
    }
}
