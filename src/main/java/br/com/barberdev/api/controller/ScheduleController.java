package br.com.barberdev.api.controller;

import br.com.barberdev.api.controller.request.SaveScheduleRequest;
import br.com.barberdev.api.controller.response.SaveScheduleResponse;
import br.com.barberdev.api.controller.response.ScheduleAppointmentMonthResponse;
import br.com.barberdev.api.mapper.IScheduleMapper;
import br.com.barberdev.api.service.IScheduleService;
import br.com.barberdev.api.service.query.IScheduleQueryService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.YearMonth;
import java.time.ZoneId;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("schedules")
@AllArgsConstructor
public class ScheduleController {

    private final IScheduleService service;
    private final IScheduleQueryService queryService;
    private final IScheduleMapper mapper;

    /**
     * Timezone used to compute month boundaries for the listing query.
     * Defaults to America/Sao_Paulo (BRT, UTC-3) — configurable via {@code app.timezone}.
     *
     * Using the local timezone is important: an appointment at 22:30 BRT on April 30
     * is stored as 01:30 UTC on May 1. With UTC boundaries, it would wrongly appear
     * in May instead of April.
     */
    @Value("${app.timezone:America/Sao_Paulo}")
    private String timezone;

    @PostMapping
    @ResponseStatus(CREATED)
    SaveScheduleResponse save(@RequestBody @Valid final SaveScheduleRequest request) {
        final var entity = mapper.toEntity(request);
        service.save(entity);
        return mapper.toSaveResponse(entity);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(NO_CONTENT)
    void delete(@PathVariable final long id) {
        service.delete(id);
    }

    @GetMapping("{year}/{month}")
    ScheduleAppointmentMonthResponse listMonth(
            @PathVariable final int year,
            @PathVariable final int month) {

        final var zone      = ZoneId.of(timezone);
        final var yearMonth = YearMonth.of(year, month);

        // Build boundaries in the local timezone so that appointments late at night
        // (e.g., 22:30 BRT = 01:30 UTC next day) are still included in the correct month.
        final var startAt = yearMonth.atDay(1)
                .atTime(0, 0, 0, 0)
                .atZone(zone)
                .toOffsetDateTime();
        final var endAt = yearMonth.atEndOfMonth()
                .atTime(23, 59, 59, 999_999_999)
                .atZone(zone)
                .toOffsetDateTime();

        final var entities = queryService.findInMonth(startAt, endAt);
        return mapper.toMonthResponse(year, month, entities);
    }
}
