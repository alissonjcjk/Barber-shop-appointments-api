package br.com.barberdev.api.service;

import br.com.barberdev.api.entity.ScheduleEntity;

public interface IScheduleService {
    ScheduleEntity save(ScheduleEntity entity);
    void delete(long id);
}
