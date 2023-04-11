package ru.practicum.explore_with_me.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore_with_me.WebClientService;
import ru.practicum.explore_with_me.dto.StatsDtoForView;
import ru.practicum.explore_with_me.handler.exceptions.StatsException;
import ru.practicum.explore_with_me.model.Event;
import ru.practicum.explore_with_me.model.ParticipationRequest;
import ru.practicum.explore_with_me.repository.ParticipationRequestRepository;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Import({WebClientService.class})
public class UtilService {
    private final ParticipationRequestRepository requestRepository;
    private final WebClientService webClientService;

    /**
     * Получить просмотры списка событий.
     * @param events список событий.
     * @return список событий с заполненными полями количества просмотров.
     */
    public List<StatsDtoForView> getViews(List<Event> events) {
        if (events == null || events.isEmpty()) {
            return Collections.emptyList();
        }
        List<String> uris = new ArrayList<>();
        LocalDateTime start = null;
        for (Event event : events) {
            uris.add("/events/" + event.getId());
            if (start == null) {
                start = event.getCreatedOn();
            } else if (start.isBefore(event.getCreatedOn())) {
                start = event.getCreatedOn();
            }
        }
        List<StatsDtoForView> stats = new ArrayList<>();
        try {
            log.info("Запрашиваем статистику от сервера статистики.");
            stats = webClientService.getStats(start, LocalDateTime.now(), uris, true);
        } catch (StatsException e) {
            log.error(String.format("Ошибка сервиса статистики при получении информации об %s:\t\t%s", uris,
                    e.getMessage()));
        }
        return stats;
    }

    /**
     * Заполнить просмотры списка событий.
     * @param events события без заполненных полей о просмотрах
     * @param stats  список статистики.
     * @return список событий с заполненными полями количества просмотров.
     */
    public List<Event> fillViews(List<Event> events, List<StatsDtoForView> stats) {
        //Если статистика есть, то заполняем поля.
        List<Event> result = new ArrayList<>();
        if (stats != null && !stats.isEmpty()) {
            for (Event ev : events) {

                for (StatsDtoForView statsDtoForView : stats) {
                    String[] statsFields = statsDtoForView.getUri().split("/");
                    if (Integer.parseInt(statsFields[2]) == ev.getId()) {
                        ev.setViews(statsDtoForView.getHits());
                        result.add(ev);
                    }
                }
            }
        } else {
            for (Event event : events) {
                event.setViews(0);
                result.add(event);
            }
        }

        return result;
    }

    /**
     * Найти список подтверждённых запросов для списка событий.
     * @param events список событий, для которых надо найти подтв. запросы.
     * @return карта (событие -> список запросов).
     */
    public Map<Event, List<ParticipationRequest>> prepareConfirmedRequest(List<Event> events) {
        //Получаем список подтверждённых запросов для всех событий.
        log.info("Получаем список подтверждённых запросов для всех событий.");
        List<Long> list1 = new ArrayList<>();
        for (Event event1 : events) {
            list1.add(event1.getId());
        }
        List<ParticipationRequest> confirmedRequests = requestRepository.findConfirmedRequests(list1);
        //Теперь их надо "раскидать" в Map.
        Map<Event, List<ParticipationRequest>> result = new HashMap<>();
        //Заполняем списки карты.
        for (ParticipationRequest request : confirmedRequests) {
            Event event = request.getEvent();
            List<ParticipationRequest> list = result.get(event);
            if (list == null) {
                list = new ArrayList<>();
            }
            list.add(request);
            result.put(event, list);
        }
        return result;
    }

    /**
     * Заполняем просмотры списка событий.
     */
    public List<Event> fillConfirmedRequests(List<Event> events, Map<Event, List<ParticipationRequest>> confirmedRequests) {
        if (confirmedRequests == null || confirmedRequests.isEmpty()) {
            log.info("Список событий пуст или равен null. Вот он: {}.", confirmedRequests);
            for (Event event : events) {
                event.setConfirmedRequests(0);
            }
            return events;
        }

        for (Event event : events) {
            if (confirmedRequests.get(event).isEmpty()) {
                event.setConfirmedRequests(0);
            } else {
                event.setConfirmedRequests(confirmedRequests.get(event).size());
            }
        }
        return events;
    }
}
