package ru.practicum.explore_with_me.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.explore_with_me.dto.StatDto;
import ru.practicum.explore_with_me.dto.StatsDtoForSave;
import ru.practicum.explore_with_me.dto.StatsDtoForView;
import ru.practicum.explore_with_me.mapper.StatMapper;
import ru.practicum.explore_with_me.model.Application;
import ru.practicum.explore_with_me.model.Stat;
import ru.practicum.explore_with_me.repositiry.StatRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
        Optional<Application> application = applicationService.getByName(statDto.getApp());
        //Если нет записи об этой программе, то записываем её.
        if (application.isEmpty()) {
            Application applicationForSave = new Application(statDto.getApp());
            applicationService.save(applicationForSave);
        }
        Stat stat = statMapper.mapFromSaveToModel(statDto);
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
        List<StatDto> result = new ArrayList<>();
        if (unique) {
            if (uris.isEmpty()) {
                //Если нет эндпоинтов, то вывести список всех эндпоинтов и их посещений уникальными
                // пользователями с уникальными IP-адресами.
                result = statRepository.findAllWhenUriIsEmpty(start, end);
            }
            //Если эндпоинты есть, то поиск по ним.
//            result = statRepository.
        }

        return result.stream().map(statMapper::mapToDtoForView).collect(Collectors.toList());
    }
}
