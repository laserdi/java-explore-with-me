package ru.practicum.explore_with_me;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
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
//@RequiredArgsConstructor
@Slf4j
public class StatsClient {
    /**
     * RestTemplate предусматривает API более высокого уровня в отличие от клиентских библиотек HTTP.
     * Он позволяет с легкостью вызывать конечные точки REST в одной строке. Он раскрывает следующие группы
     * <a href="https://javarush.com/quests/lectures/questspring.level06.lecture00">перегруженных методов.</a>
     */
    /**
     * <p>Источник здесь.</p>
     * <a href="https://for-each.dev/lessons/b/-spring-value-annotation">...</a>
     */
    @Value("${stats-server.url}")
    private String statsServer;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        // Do any additional configuration here
        return builder.build();
    }

    private final RestTemplate restTemplate = restTemplate(new RestTemplateBuilder());

    /**
     * <p>Получение из БД информации об обращениях к ресурсу.</p>
     * {{baseUrl}}/stats?start=2020-05-05 00:00:00&end=2035-05-05 00:00:00&uris={{uri}}
     * <p>Помните, что URI-шаблоны автоматически кодируются, как показано в следующем примере:</p>
     * <p>restTemplate.getForObject("https://example.com/hotel list", String.class);</p>
     * Результат запроса по "https://example.com/hotel%20list"
     * @param uris список адресов.
     * @return список посещений для разных эндпоинтов.
     */
    public ResponseEntity<List<StatsDtoForView>> getStats(List<String> uris) {
        return getStats(LocalDateTime.of(2000, 1, 1, 0, 0, 0),
                LocalDateTime.now(), uris, false);
    }

    /**
     * <p>Получение из БД информации об обращениях к ресурсу.</p>
     * {{baseUrl}}/stats?start=2020-05-05 00:00:00&end=2035-05-05 00:00:00&uris={{uri}}
     * <p>Помните, что URI-шаблоны автоматически кодируются, как показано в следующем примере:</p>
     * <p>restTemplate.getForObject("https://example.com/hotel list", String.class);</p>
     * Результат запроса по "https://example.com/hotel%20list"
     * @return список посещений для разных эндпоинтов.
     */
    public ResponseEntity<List<StatsDtoForView>> getStats(LocalDateTime start, LocalDateTime end,
                                                          List<String> uris, boolean unique) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);
        StringBuilder urisForExchange = new StringBuilder();
        for (int i = 0; i < uris.size(); i++) {
            if (i < (uris.size() - 1)) {
                //Если не последний эндпоинт...
                urisForExchange.append("uris").append("=").append(uris.get(i)).append(",");
            } else {
                //Иначе...
                urisForExchange.append("uris").append("=").append(uris.get(i));
            }
        }
        Map<String, Object> uriVariables = Map.of(
                "start", start.format(formatter),
                "end", end.format(formatter),
                "uris", urisForExchange.toString(),
                "unique", unique);

        String uri = statsServer + "/stats?start={start}&end={end}&uris={uris}&unique={unique}";
        log.info("** GET STATS: **\t\t{}", uri);

        ResponseEntity<List<StatsDtoForView>> response = restTemplate.exchange(uri, HttpMethod.GET, requestEntity,
                new ParameterizedTypeReference<List<StatsDtoForView>>() {
                },
                uriVariables);

        //
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
        restTemplate.exchange(statsServer + "/hit", HttpMethod.POST, requestEntity, StatsDtoForSave.class);
    }
}
