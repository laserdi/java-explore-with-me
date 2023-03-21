package ru.practicum.explore_with_me;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.practicum.explore_with_me.dto.StatsDtoForSave;
import ru.practicum.explore_with_me.dto.StatsDtoForView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class StatsClient {
    /**
     * <p>Источник здесь.</p>
     * <a href="https://for-each.dev/lessons/b/-spring-value-annotation">...</a>
     */
    @Value("${statsServerUrl}")
    private String statsServer;
    /**
     * RestTemplate предусматривает API более высокого уровня в отличие от клиентских библиотек HTTP.
     * Он позволяет с легкостью вызывать конечные точки REST в одной строке. Он раскрывает следующие группы
     * <a href="https://javarush.com/quests/lectures/questspring.level06.lecture00">перегруженных методов.</a>
     */
    private RestTemplate restTemplate;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * <p>Получение из БД информации об обращениях к ресурсу.</p>
     * {{baseUrl}}/stats?start=2020-05-05 00:00:00&end=2035-05-05 00:00:00&uris={{uri}}
     * <p>Помните, что URI-шаблоны автоматически кодируются, как показано в следующем примере:</p>
     * <p>restTemplate.getForObject("https://example.com/hotel list", String.class);</p>
     * Результат запроса по "https://example.com/hotel%20list"
     * @return список посещений для разных эндпоинтов.
     */
    public ResponseEntity<List<StatsDtoForView>> getStats(LocalDateTime start, LocalDateTime end,
                                                          String[] uris, boolean unique) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);
        StringBuilder urisForExchange = new StringBuilder();
        for (int i = 0; i < uris.length; i++) {
            if (i < (uris.length - 1)) {
                //Если не последний эндпоинт...
                urisForExchange.append("uris").append("=").append(uris[i]).append(",");
            } else {
                //Иначе...
                urisForExchange.append("uris").append("=").append(uris[i]);
            }
        }
        Map<String, Object> uriVariables = Map.of(
                "start", start.format(formatter),
                "end", end.format(formatter),
                "uris", urisForExchange.toString(),
                "unique", unique);

        restTemplate = new RestTemplate();
        String uri = statsServer + "/stats?start={start}&end={end}&{uris}&{unique}";
        log.info("** GET STATS: **\t\t{}", uri);
        ParameterizedTypeReference<List<StatsDtoForView>> parTypeRef =
                new ParameterizedTypeReference<>() {
                };
        ResponseEntity<List<StatsDtoForView>> response = restTemplate.exchange(uri, HttpMethod.GET, requestEntity,
                parTypeRef, uriVariables);
        log.info(response.toString());
        return response;
    }

    /**
     * <p>Сохранение в БД информации об обращении к ресурсу.</p>
     * {{baseUrl}}/stats?start=2020-05-05 00:00:00&end=2035-05-05 00:00:00&uris={{uri}}
     */
    public void save(StatsDtoForSave statsDtoForSave) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<StatsDtoForSave> requestEntity = new HttpEntity<>(statsDtoForSave, httpHeaders);
        restTemplate.exchange(statsServer + "hit", HttpMethod.POST, requestEntity, StatsDtoForSave.class);
    }
}
