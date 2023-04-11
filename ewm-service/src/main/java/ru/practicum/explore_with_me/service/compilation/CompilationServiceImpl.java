package ru.practicum.explore_with_me.service.compilation;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore_with_me.dto.StatsDtoForView;
import ru.practicum.explore_with_me.dto.compilation.CompilationDto;
import ru.practicum.explore_with_me.dto.compilation.NewCompilationDto;
import ru.practicum.explore_with_me.dto.compilation.UpdateCompilationRequest;
import ru.practicum.explore_with_me.dto.event.EventShortDto;
import ru.practicum.explore_with_me.handler.exceptions.NotFoundRecordInBD;
import ru.practicum.explore_with_me.mapper.CompilationMapper;
import ru.practicum.explore_with_me.mapper.CustomMapper;
import ru.practicum.explore_with_me.mapper.EventMapper;
import ru.practicum.explore_with_me.model.Compilation;
import ru.practicum.explore_with_me.model.Event;
import ru.practicum.explore_with_me.model.ParticipationRequest;
import ru.practicum.explore_with_me.repository.CompilationRepository;
import ru.practicum.explore_with_me.repository.EventRepository;
import ru.practicum.explore_with_me.util.UtilService;

import java.util.*;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final CompilationMapper compilationMapper;
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final UtilService utilService;

    /**
     * Добавить в БД подборку.
     * <p>Пример тела запроса:</p>
     * <p>{"title":"Consequatur omnis sunt qui qui.","pinned":"true","events":[2]}</p>
     * @param newCompilationDto подборка событий при создании.
     * @return подборка событий.
     */
    @Override
    @Transactional
    public CompilationDto addCompilation(NewCompilationDto newCompilationDto) {
        //Считаем события из БД.
        Compilation compilation = compilationMapper.mapFromNewDtoToModel(newCompilationDto);

        List<Event> events = new ArrayList<>();
        if (newCompilationDto.getEvents() != null && !newCompilationDto.getEvents().isEmpty()) {
            events = eventRepository.findAllById(newCompilationDto.getEvents());
        }
        //Конвертируем список событий в набор.
        Set<Event> eventSet = new HashSet<>(events);
        //Присваиваем его подборке.
        compilation.setEvents(eventSet);
        //Сохраняем в БД.
        Compilation saved = compilationRepository.save(compilation);
        //Готовим ответ.
        //Получить просмотры списка событий:
        List<StatsDtoForView> views = utilService.getViews(events);
        events = utilService.fillViews(events, views);
        //Получить подтверждённые заявки:
        Map<Event, List<ParticipationRequest>> requests = utilService.prepareConfirmedRequest(events);
        //Заполняем подтверждёнными заявками:
        events = utilService.fillConfirmedRequests(events, requests);

        CompilationDto result = compilationMapper.mapToDto(saved);
        result.setEvents(eventMapper.mapFromModelListToShortDtoList(events));

        log.info("Создана подборка из {} событий.", events.size());
        return result;
    }

    @Override
    @Transactional
    public void deleteCompilation(Long compId) {
        getCompilationOrThrow(compId, "При удалении подборки ID = %d она не найдена в БД.");
        compilationRepository.deleteById(compId);
    }

    @Override
    @Transactional
    public CompilationDto updateCompilation(Long compId, UpdateCompilationRequest updateCompilationDto) {
        Compilation compilation = getCompilationOrThrow(compId,
                "При обновлении подборки ID = %d она не найдена в БД.");
        if (updateCompilationDto.getPinned() != null) {
            compilation.setPinned(updateCompilationDto.getPinned());
        }
        List<Event> events = new ArrayList<>();
        if (updateCompilationDto.getEvents() != null && !updateCompilationDto.getEvents().isEmpty()) {
            events = eventRepository.findAllById(updateCompilationDto.getEvents());
            compilation.setEvents(new HashSet<>(events));
        }
        if (updateCompilationDto.getTitle() != null && !updateCompilationDto.getTitle().isBlank()) {
            compilation.setTitle(updateCompilationDto.getTitle());
        }
        CompilationDto result = compilationMapper.mapToDto(compilation);

        //Получить просмотры списка событий:
        List<StatsDtoForView> views = utilService.getViews(events);
        events = utilService.fillViews(events, views);

        //Получить подтверждённые заявки для всех событий из всех подборок.
        Map<Event, List<ParticipationRequest>> requests = utilService.prepareConfirmedRequest(events);
        //Заполняем подтверждёнными заявками:
        events = utilService.fillConfirmedRequests(events, requests);

        result.setEvents(eventMapper.mapFromModelListToShortDtoList(events));
        return compilationMapper.mapToDto(compilation);
    }

    @Override
    public List<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from, size, Sort.by("id").ascending());
        List<Compilation> compilations = compilationRepository.findAllByPinned(pinned, pageable);

        Set<Event> events = new HashSet<>();
        for (Compilation compilation : compilations) {
            events.addAll(compilation.getEvents());
        }
        List<Event> eventList = new ArrayList<>(events);

        //Получить просмотры списка событий:
        List<StatsDtoForView> views = utilService.getViews(eventList);
        eventList = utilService.fillViews(eventList, views);

        //Получить подтверждённые заявки для всех событий из всех подборок.
        Map<Event, List<ParticipationRequest>> requests = utilService.prepareConfirmedRequest(eventList);
        //Заполняем подтверждёнными заявками:
        eventList = utilService.fillConfirmedRequests(eventList, requests);

        List<CompilationDto> result = new ArrayList<>();
        //Конвертируем события в DTO, распределив их по подборкам.
        for (Compilation c : compilations) {
            List<EventShortDto> eventArrayList = new ArrayList<>();
            CompilationDto compilationDto = CustomMapper.mapFromNewDtoToModel(c);
            compilationDto.setEvents(new ArrayList<>());
            Set<Event> eventSet = c.getEvents();
            for (Event ev : eventSet) {
                for (Event evFromList : eventList) {
                    if (evFromList != null && evFromList.getId().equals(ev.getId())) {
                        eventArrayList.add(eventMapper.mapToShortDto(evFromList));
                    }
                }
            }
            compilationDto.setEvents(eventArrayList);
            result.add(compilationDto);
        }
        return result;
    }

    @Override
    public CompilationDto getCompilationById(Long compId) {
        Compilation compilation = getCompilationOrThrow(compId,
                "При получении подборки по ID = %d она не найдена в БД.");

        List<Event> events = new ArrayList<>(compilation.getEvents());

        //Конвертируем список событий в набор.
        Set<Event> eventSet = new HashSet<>(events);
        //Присваиваем его подборке.
        compilation.setEvents(eventSet);
        //Сохраняем в БД.
        Compilation saved = compilationRepository.save(compilation);
        //Готовим ответ.
        //Получить просмотры списка событий:
        List<StatsDtoForView> views = utilService.getViews(events);
        events = utilService.fillViews(events, views);
        //Получить подтверждённые заявки:
        Map<Event, List<ParticipationRequest>> requests = utilService.prepareConfirmedRequest(events);
        //Заполняем подтверждёнными заявками:
        events = utilService.fillConfirmedRequests(events, requests);

        CompilationDto result = compilationMapper.mapToDto(saved);
        result.setEvents(eventMapper.mapFromModelListToShortDtoList(events));

        log.info("Создана подборка из {} событий.", events.size());

        return result;
    }

    /**
     * Проверка наличия подборки в БД.
     * @param compId  ID подборки.
     * @param message информация об ошибке, где должно быть поле для ID подборки в таком виде %d.
     * @return подборка.
     */
    private Compilation getCompilationOrThrow(Long compId, String message) {
        if (message == null || message.isBlank()) {
            message = "Подборка с ID = %d не найдена в БД.";
        }
        String finalMessage = message;
        return compilationRepository.findById(compId).orElseThrow(
                () -> new NotFoundRecordInBD(String.format(finalMessage, compId)));
    }
}
