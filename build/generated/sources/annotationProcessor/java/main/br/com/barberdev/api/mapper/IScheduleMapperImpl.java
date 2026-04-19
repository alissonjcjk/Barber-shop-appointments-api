package br.com.barberdev.api.mapper;

import br.com.barberdev.api.controller.request.SaveScheduleRequest;
import br.com.barberdev.api.controller.response.ClientScheduleAppointmentResponse;
import br.com.barberdev.api.controller.response.SaveScheduleResponse;
import br.com.barberdev.api.controller.response.ScheduleAppointmentMonthResponse;
import br.com.barberdev.api.entity.ClientEntity;
import br.com.barberdev.api.entity.ScheduleEntity;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-18T16:16:12-0300",
    comments = "version: 1.6.3, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.12.1.jar, environment: Java 21.0.9 (Eclipse Adoptium)"
)
@Component
public class IScheduleMapperImpl implements IScheduleMapper {

    @Override
    public ScheduleEntity toEntity(SaveScheduleRequest request) {
        if ( request == null ) {
            return null;
        }

        ScheduleEntity scheduleEntity = new ScheduleEntity();

        scheduleEntity.setClient( saveScheduleRequestToClientEntity( request ) );
        scheduleEntity.setStartAt( request.startAt() );
        scheduleEntity.setEndAt( request.endAt() );

        return scheduleEntity;
    }

    @Override
    public SaveScheduleResponse toSaveResponse(ScheduleEntity entity) {
        if ( entity == null ) {
            return null;
        }

        Long clientId = null;
        String clientName = null;
        Long id = null;
        OffsetDateTime startAt = null;
        OffsetDateTime endAt = null;

        clientId = entityClientId( entity );
        clientName = entityClientName( entity );
        id = entity.getId();
        startAt = entity.getStartAt();
        endAt = entity.getEndAt();

        SaveScheduleResponse saveScheduleResponse = new SaveScheduleResponse( id, startAt, endAt, clientId, clientName );

        return saveScheduleResponse;
    }

    @Override
    public ScheduleAppointmentMonthResponse toMonthResponse(int year, int month, List<ScheduleEntity> entities) {
        if ( entities == null ) {
            return null;
        }

        int year1 = 0;
        year1 = year;
        int month1 = 0;
        month1 = month;

        List<ClientScheduleAppointmentResponse> scheduledAppointments = toClientMonthResponse(entities);

        ScheduleAppointmentMonthResponse scheduleAppointmentMonthResponse = new ScheduleAppointmentMonthResponse( year1, month1, scheduledAppointments );

        return scheduleAppointmentMonthResponse;
    }

    @Override
    public List<ClientScheduleAppointmentResponse> toClientMonthResponse(List<ScheduleEntity> entities) {
        if ( entities == null ) {
            return null;
        }

        List<ClientScheduleAppointmentResponse> list = new ArrayList<ClientScheduleAppointmentResponse>( entities.size() );
        for ( ScheduleEntity scheduleEntity : entities ) {
            list.add( toClientMonthResponse( scheduleEntity ) );
        }

        return list;
    }

    @Override
    public ClientScheduleAppointmentResponse toClientMonthResponse(ScheduleEntity entity) {
        if ( entity == null ) {
            return null;
        }

        Long clientId = null;
        String clientName = null;
        Long id = null;
        OffsetDateTime startAt = null;
        OffsetDateTime endAt = null;

        clientId = entityClientId( entity );
        clientName = entityClientName( entity );
        id = entity.getId();
        startAt = entity.getStartAt();
        endAt = entity.getEndAt();

        Integer day = entity.getStartAt().getDayOfMonth();

        ClientScheduleAppointmentResponse clientScheduleAppointmentResponse = new ClientScheduleAppointmentResponse( id, day, startAt, endAt, clientId, clientName );

        return clientScheduleAppointmentResponse;
    }

    protected ClientEntity saveScheduleRequestToClientEntity(SaveScheduleRequest saveScheduleRequest) {
        if ( saveScheduleRequest == null ) {
            return null;
        }

        ClientEntity clientEntity = new ClientEntity();

        clientEntity.setId( saveScheduleRequest.clientId() );

        return clientEntity;
    }

    private Long entityClientId(ScheduleEntity scheduleEntity) {
        ClientEntity client = scheduleEntity.getClient();
        if ( client == null ) {
            return null;
        }
        return client.getId();
    }

    private String entityClientName(ScheduleEntity scheduleEntity) {
        ClientEntity client = scheduleEntity.getClient();
        if ( client == null ) {
            return null;
        }
        return client.getName();
    }
}
