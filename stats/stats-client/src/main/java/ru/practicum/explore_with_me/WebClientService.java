package ru.practicum.explore_with_me;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.practicum.explore_with_me.dto.StatsDtoForSave;
import ru.practicum.explore_with_me.dto.StatsDtoForView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class WebClientService {
    protected final WebClient webClient;
    private final String baseUrl;
    public static final String TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TIME_PATTERN);

    @Autowired
    public WebClientService(WebClient.Builder webClientBuilder, @Value("${stats-server.url}") String baseUrl) {
        this.baseUrl = baseUrl;
        webClientBuilder.baseUrl(baseUrl);
        this.webClient = webClientBuilder.build();
    }

    public void saveStats(String app,
                        String uri,
                        String ip,
                        LocalDateTime timestamp) {
        StatsDtoForSave statsDtoForSave = StatsDtoForSave.builder()
                .app(app)
                .uri(uri)
                .ip(ip)
                .timestamp(timestamp)
                .build();

        webClient.post()
                .uri("/hit")
                .body(Mono.just(statsDtoForSave), StatsDtoForSave.class)
                .retrieve()
                .bodyToMono(StatsDtoForSave.class)
                .block();
    }

    public List<StatsDtoForView> getStats(LocalDateTime start,
                                          LocalDateTime end,
                                          List<String> uris,
                                          boolean unique) {

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/stats")
                        .queryParam("start", start.format(formatter))
                        .queryParam("end", end.format(formatter))
                        .queryParam("unique", String.valueOf(unique))
                        .queryParam("uris", uris)
                        .build())
                .retrieve()
                .bodyToFlux(StatsDtoForView.class)
                .collectList()
                .block();
    }
}