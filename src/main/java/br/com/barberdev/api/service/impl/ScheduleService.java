package br.com.barberdev.api.service.impl;

import br.com.barberdev.api.entity.ScheduleEntity;
import br.com.barberdev.api.repository.IScheduleRepository;
import br.com.barberdev.api.service.IScheduleService;
import br.com.barberdev.api.service.query.IScheduleQueryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class ScheduleService implements IScheduleService {

    private final IScheduleRepository repository;
    private final IScheduleQueryService queryService;

    @Override
    @Transactional
    public ScheduleEntity save(final ScheduleEntity entity) {
        queryService.verifyIfScheduleAvailable(entity.getStartAt(), entity.getEndAt());
        return repository.save(entity);
    }

    @Override
    @Transactional
    public void delete(final long id) {
        queryService.findById(id);
        repository.deleteById(id);
    }
}
