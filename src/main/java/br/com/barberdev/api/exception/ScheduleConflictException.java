package br.com.barberdev.api.exception;

public class ScheduleConflictException extends RuntimeException {

    public ScheduleConflictException() {
        super("Já existe um agendamento no horário solicitado. Escolha outro horário.");
    }
}
