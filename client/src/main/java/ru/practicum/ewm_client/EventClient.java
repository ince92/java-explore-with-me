package ru.practicum.ewm_client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class EventClient extends BaseClient {
    @Autowired
    public EventClient(@Value("${stat-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> getViews(List<String> uriList, Boolean unique, String start, String end) {
        StringBuilder str = new StringBuilder();
        for (String uri : uriList) {
            str.append("uris=").append(uri).append("&");
        }
        Map<String, Object> parameters = Map.of(
                "unique", unique,
                "start", start,
                "end", end
        );
        return get("/stats?" + str + "unique={unique}&start={start}&end={end}", parameters);
    }

    public void hit(String uri, String ip) {
        post("/hit", new EndpointHitDto("main-service", uri, ip, LocalDateTime.now().toString()));
    }


}
