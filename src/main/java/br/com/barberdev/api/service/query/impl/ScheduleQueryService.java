package br.com.barberdev.api.service.query.impl;

import br.com.barberdev.api.entity.ScheduleEntity;
import br.com.barberdev.api.exception.NotFoundException;
import br.com.barberdev.api.exception.ScheduleConflictException;
import br.com.barberdev.api.repository.IScheduleRepository;
import br.com.barberdev.api.service.query.IScheduleQueryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * Read-only query service for schedules.
 *
 * All methods run inside a read-only transaction, which tells Hibernate
 * to skip dirty-checking on loaded entities and allows the connection pool
 * to reuse connections more efficiently.
 */
@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class ScheduleQueryService implements IScheduleQueryService {

    private final IScheduleRepository repository;

    @Override
    public ScheduleEntity findById(final long id) {
        return repository.findById(id).orElseThrow(
                () -> new NotFoundException("Agendamento com id " + id + " não encontrado")
        );
    }

    @Override
    public List<ScheduleEntity> findInMonth(final OffsetDateTime startAt, final OffsetDateTime endAt) {
        return repository.findByStartAtGreaterThanEqualAndEndAtLessThanEqualOrderByStartAtAscEndAtAsc(startAt, endAt);
    }

    @Override
    public void verifyIfScheduleAvailable(final OffsetDateTime startAt, final OffsetDateTime endAt) {
        // Overlap condition: existingStart < newEnd  AND  existingEnd > newStart
        if (repository.existsByStartAtLessThanAndEndAtGreaterThan(endAt, startAt)) {
            throw new ScheduleConflictException();
        }
    }
}
