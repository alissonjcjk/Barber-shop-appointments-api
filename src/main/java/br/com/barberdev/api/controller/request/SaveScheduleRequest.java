package br.com.barberdev.api.controller.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;

public record SaveScheduleRequest(

        @NotNull(message = "O horário de início é obrigatório")
        @Future(message = "O horário de início deve ser uma data futura")
        @JsonProperty("startAt")
        OffsetDateTime startAt,

        @NotNull(message = "O horário de término é obrigatório")
        @Future(message = "O horário de término deve ser uma data futura")
        @JsonProperty("endAt")
        OffsetDateTime endAt,

        @NotNull(message = "O cliente é obrigatório")
        @JsonProperty("clientId")
        Long clientId

) {}
