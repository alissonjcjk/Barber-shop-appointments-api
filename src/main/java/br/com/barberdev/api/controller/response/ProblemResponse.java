package br.com.barberdev.api.controller.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * RFC 7807 - Problem Details for HTTP APIs
 * Estrutura padronizada de erro para todas as respostas de erro da API.
 */
public record ProblemResponse(

        @JsonProperty("status")
        Integer status,

        @JsonProperty("timestamp")
        OffsetDateTime timestamp,

        @JsonProperty("message")
        String message,

        @JsonProperty("fields")
        List<FieldError> fields

) {
    public record FieldError(
            @JsonProperty("field")   String field,
            @JsonProperty("message") String message
    ) {}

    public static ProblemResponse of(final int status, final String message) {
        return new ProblemResponse(status, OffsetDateTime.now(), message, List.of());
    }

    public static ProblemResponse of(final int status, final String message, final List<FieldError> fields) {
        return new ProblemResponse(status, OffsetDateTime.now(), message, fields);
    }
}
