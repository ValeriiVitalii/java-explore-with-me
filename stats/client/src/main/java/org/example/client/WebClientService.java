package org.example.client;

import org.example.model.Stats;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.json.JsonParseException;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import org.example.model.HitDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class WebClientService {
    protected final WebClient webClient;
    public static final String TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TIME_PATTERN);

    public WebClientService(WebClient.Builder webClientBuilder, @Value("${server-address.url}") String baseUrl) {
        this.webClient = webClientBuilder
                .baseUrl(baseUrl)
                .build();
    }

    public void postHit(String app,
                        String uri,
                        String ip,
                        String timestamp) throws JsonParseException {
        HitDto endpointHitDTO = HitDto.builder()
                .app(app)
                .uri(uri)
                .ip(ip)
                .timestamp(timestamp)
                .build();

        webClient.post()
                .uri("/hit")
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(endpointHitDTO), HitDto.class)
                .retrieve()
                .bodyToMono(HitDto.class)
                .block();
    }

    public List<Stats> getStats(LocalDateTime start,
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
                .bodyToFlux(Stats.class)
                .collectList()
                .block();
    }
}

