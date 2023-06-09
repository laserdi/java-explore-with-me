package ru.practicum.explore_with_me.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.explore_with_me.dto.StatWithHits;
import ru.practicum.explore_with_me.dto.StatsDtoForSave;
import ru.practicum.explore_with_me.dto.StatsDtoForView;
import ru.practicum.explore_with_me.mapper.StatMapper;
import ru.practicum.explore_with_me.model.Application;
import ru.practicum.explore_with_me.model.Stat;
import ru.practicum.explore_with_me.repositiry.StatRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatsServiceImpl implements StatsService {
    private final StatRepository statRepository;
    private final ApplicationService applicationService;
    private final StatMapper statMapper;

    /**
     * Сохранить в БД статистики информацию о запросе эндпоинта.
     * @param statDto информация об эн запросе.
     */
    @Override
    public void save(StatsDtoForSave statDto) {
        //Если нет записи об этой программе, то записываем её в БД.
        Application application = applicationService.getByName(statDto.getApp())
                .orElseGet(() -> applicationService.save(new Application(statDto.getApp())));

        Stat stat = statMapper.mapFromSaveToModel(statDto);
        stat.setApp(application);
        statRepository.save(stat);
    }

    /**
     * Получить статистику по эндпоинтам uris за период между start и end.
     * @param start  момент начала периода выдачи статистики.
     * @param end    момент конца периода выдачи статистики.
     * @param uris   список эндпоинтов (
     * @param unique выдать результаты, группируя по уникальным IP-адреса, с которых был запрос.
     * @return список статистики.
     */
    @Override
    public List<StatsDtoForView> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        List<StatWithHits> result;
        if (unique) {
            if (uris == null || uris.isEmpty()) {
                //Если нет эндпоинтов, то вывести список всех уникальных эндпоинтов и их посещений
                // пользователями с уникальными IP-адресами.
                log.info("Получение статистики: в запросе эндпоинтов нет, unique = true");
                result = statRepository.findAllUniqueWhenUriIsEmpty(start, end);
            } else {
                //Если эндпоинты есть, то поиск по ним.
                log.info("Получение статистики: в запросе эндпоинты есть, unique = true");
                result = statRepository.findAllUniqueWhenUriIsNotEmpty(start, end, uris);
            }
        } else {
            if (uris == null || uris.isEmpty()) {
                log.info("Получение статистики: в запросе эндпоинтов нет, unique = false");
                result = statRepository.findAllWhenUriIsEmpty(start, end);
            } else {
                log.info("Получение статистики: в запросе эндпоинты есть, unique = false");
                result = statRepository.findAllWhenStarEndUris(start, end, uris);
            }
        }

        return result.stream().map(statMapper::mapToDtoForView).collect(Collectors.toList());
    }
}
