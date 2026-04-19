package br.com.barberdev.api.mapper;

import br.com.barberdev.api.controller.request.SaveScheduleRequest;
import br.com.barberdev.api.controller.response.ClientScheduleAppointmentResponse;
import br.com.barberdev.api.controller.response.SaveScheduleResponse;
import br.com.barberdev.api.controller.response.ScheduleAppointmentMonthResponse;
import br.com.barberdev.api.entity.ScheduleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface IScheduleMapper {

    @Mapping(target = "id",        ignore = true)
    @Mapping(target = "client.id", source = "clientId")
    ScheduleEntity toEntity(final SaveScheduleRequest request);

    @Mapping(target = "clientId",   source = "client.id")
    @Mapping(target = "clientName", source = "client.name")
    SaveScheduleResponse toSaveResponse(final ScheduleEntity entity);

    @Mapping(target = "scheduledAppointments", expression = "java(toClientMonthResponse(entities))")
    ScheduleAppointmentMonthResponse toMonthResponse(final int year, final int month, final List<ScheduleEntity> entities);

    List<ClientScheduleAppointmentResponse> toClientMonthResponse(final List<ScheduleEntity> entities);

    @Mapping(target = "clientId",   source = "client.id")
    @Mapping(target = "clientName", source = "client.name")
    @Mapping(target = "day",        expression = "java(entity.getStartAt().getDayOfMonth())")
    ClientScheduleAppointmentResponse toClientMonthResponse(final ScheduleEntity entity);
}
