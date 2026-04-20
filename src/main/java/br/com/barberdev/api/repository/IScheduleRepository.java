package br.com.barberdev.api.repository;

import br.com.barberdev.api.entity.ScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;

@Repository
public interface IScheduleRepository extends JpaRepository<ScheduleEntity, Long> {

    List<ScheduleEntity> findByStartAtGreaterThanEqualAndEndAtLessThanEqualOrderByStartAtAscEndAtAsc(
            final OffsetDateTime startAt,
            final OffsetDateTime endAt
    );

    /**
     * Detects whether any existing schedule overlaps the given interval.
     *
     * Two intervals [a, b) and [c, d) overlap when: a < d AND b > c
     * Spring Data JPA naming: startAt < endAt_param AND endAt > startAt_param
     *
     * @param endAt   end of the new interval (exclusive upper bound check)
     * @param startAt start of the new interval (exclusive lower bound check)
     */
    boolean existsByStartAtLessThanAndEndAtGreaterThan(
            final OffsetDateTime endAt,
            final OffsetDateTime startAt
    );
}
