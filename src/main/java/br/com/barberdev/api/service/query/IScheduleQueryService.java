package br.com.barberdev.api.service.query;

import br.com.barberdev.api.entity.ScheduleEntity;

import java.time.OffsetDateTime;
import java.util.List;

public interface IScheduleQueryService {
    ScheduleEntity findById(long id);
    List<ScheduleEntity> findInMonth(OffsetDateTime startAt, OffsetDateTime endAt);
    void verifyIfScheduleAvailable(OffsetDateTime startAt, OffsetDateTime endAt);
}
