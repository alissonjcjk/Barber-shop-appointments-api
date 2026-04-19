package br.com.barberdev.api.exceptionhandler;

import br.com.barberdev.api.controller.response.ProblemResponse;
import br.com.barberdev.api.exception.EmailInUseException;
import br.com.barberdev.api.exception.NotFoundException;
import br.com.barberdev.api.exception.PhoneInUseException;
import br.com.barberdev.api.exception.ScheduleConflictException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@Log4j2
@RestControllerAdvice
public class BarberDevExceptionHandler extends ResponseEntityExceptionHandler {

    /** 404 — Recurso não encontrado */
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ProblemResponse> handleNotFound(final NotFoundException ex) {
        log.warn("handleNotFound: {}", ex.getMessage());
        return ResponseEntity
                .status(NOT_FOUND)
                .body(ProblemResponse.of(NOT_FOUND.value(), ex.getMessage()));
    }

    /** 409 — E-mail já em uso */
    @ExceptionHandler(EmailInUseException.class)
    public ResponseEntity<ProblemResponse> handleEmailInUse(final EmailInUseException ex) {
        log.warn("handleEmailInUse: {}", ex.getMessage());
        return ResponseEntity
                .status(CONFLICT)
                .body(ProblemResponse.of(CONFLICT.value(), ex.getMessage()));
    }

    /** 409 — Telefone já em uso */
    @ExceptionHandler(PhoneInUseException.class)
    public ResponseEntity<ProblemResponse> handlePhoneInUse(final PhoneInUseException ex) {
        log.warn("handlePhoneInUse: {}", ex.getMessage());
        return ResponseEntity
                .status(CONFLICT)
                .body(ProblemResponse.of(CONFLICT.value(), ex.getMessage()));
    }

    /** 409 — Conflito de horário no agendamento */
    @ExceptionHandler(ScheduleConflictException.class)
    public ResponseEntity<ProblemResponse> handleScheduleConflict(final ScheduleConflictException ex) {
        log.warn("handleScheduleConflict: {}", ex.getMessage());
        return ResponseEntity
                .status(CONFLICT)
                .body(ProblemResponse.of(CONFLICT.value(), ex.getMessage()));
    }

    /** 422 — Erros de validação de campos (@Valid) com detalhes por campo */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            final MethodArgumentNotValidException ex,
            final HttpHeaders headers,
            final org.springframework.http.HttpStatusCode status,
            final WebRequest request) {

        log.warn("handleMethodArgumentNotValid: {} field errors", ex.getBindingResult().getFieldErrorCount());

        final List<ProblemResponse.FieldError> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .distinct()
                .map(msg -> {
                    final var field = ex.getBindingResult().getFieldErrors().stream()
                            .filter(fe -> fe.getDefaultMessage() != null && fe.getDefaultMessage().equals(msg))
                            .map(FieldError::getField)
                            .findFirst()
                            .orElse("campo desconhecido");
                    return new ProblemResponse.FieldError(field, msg);
                })
                .toList();

        final var body = ProblemResponse.of(
                UNPROCESSABLE_ENTITY.value(),
                "Existem campos com valores inválidos. Verifique os detalhes.",
                fieldErrors
        );

        return ResponseEntity.status(UNPROCESSABLE_ENTITY).body(body);
    }

    /** 500 — Erro genérico não tratado */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemResponse> handleUncaught(final Exception ex, final WebRequest request) {
        log.error("handleUncaught: ", ex);
        return ResponseEntity
                .status(INTERNAL_SERVER_ERROR)
                .body(ProblemResponse.of(
                        INTERNAL_SERVER_ERROR.value(),
                        "Ocorreu um erro interno inesperado. Tente novamente mais tarde."
                ));
    }
}
