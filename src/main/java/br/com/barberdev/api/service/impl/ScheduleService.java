package br.com.barberdev.api.service.impl;

import br.com.barberdev.api.entity.ScheduleEntity;
import br.com.barberdev.api.repository.IScheduleRepository;
import br.com.barberdev.api.service.IScheduleService;
import br.com.barberdev.api.service.query.IClientQueryService;
import br.com.barberdev.api.service.query.IScheduleQueryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class ScheduleService implements IScheduleService {

    private final IScheduleRepository repository;
    private final IScheduleQueryService queryService;
    private final IClientQueryService clientQueryService;

    @Override
    @Transactional
    public ScheduleEntity save(final ScheduleEntity entity) {
        // Load the full client entity so that client.name is available
        // in the response mapper (client is LAZY — only id was set by MapStruct)
        final var client = clientQueryService.findById(entity.getClient().getId());
        entity.setClient(client);

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
