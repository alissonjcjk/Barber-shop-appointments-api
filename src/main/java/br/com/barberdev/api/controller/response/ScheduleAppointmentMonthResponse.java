package br.com.barberdev.api.controller.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record ScheduleAppointmentMonthResponse(
        @JsonProperty("year")                  int                                    year,
        @JsonProperty("month")                 int                                    month,
        @JsonProperty("scheduledAppointments") List<ClientScheduleAppointmentResponse> scheduledAppointments
) {}
