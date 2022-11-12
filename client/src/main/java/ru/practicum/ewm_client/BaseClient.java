package ru.practicum.ewm_client;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.lang.Nullable;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

public class BaseClient {
    protected final RestTemplate rest;

    public BaseClient(RestTemplate rest) {
        this.rest = rest;
    }


    protected <T,U> ResponseEntity<U> get(String path, Map<String, Object> parameters, ParameterizedTypeReference<U> type) {
        return get(path, null, parameters, type);
    }

    protected <T,U> ResponseEntity<U> get(String path, Long userId, @Nullable Map<String, Object> parameters, ParameterizedTypeReference<U> type) {
        return makeAndSendRequest(HttpMethod.GET, path, userId, parameters, null, type);
    }

    protected <T,U> ResponseEntity<U> post(String path, T body,ParameterizedTypeReference<U> type) {
        return post(path, null, null, body, type);
    }


    protected <T,U> ResponseEntity<U> post(String path, Long userId, @Nullable Map<String, Object> parameters, T body, ParameterizedTypeReference<U> type) {
        return makeAndSendRequest(HttpMethod.POST, path, userId, parameters, body, type);
    }


    private <T, U> ResponseEntity<U> makeAndSendRequest(HttpMethod method, String path, Long userId,
                                                          @Nullable Map<String, Object> parameters, @Nullable T body, ParameterizedTypeReference<U> type) {
        HttpEntity<T> requestEntity = new HttpEntity<>(body, defaultHeaders(userId));

        ResponseEntity<U> ewmServerResponse;
        try {
            if (parameters != null) {
                ewmServerResponse = rest.exchange(path, method, requestEntity, type, parameters);
            } else {
                ewmServerResponse = rest.exchange(path, method, requestEntity, type);
            }
        } catch (HttpStatusCodeException e) {
            return (ResponseEntity<U>) ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
        }
        return prepareGatewayResponse(ewmServerResponse);
    }

    private HttpHeaders defaultHeaders(Long userId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        if (userId != null) {
            headers.set("X-Sharer-User-Id", String.valueOf(userId));
        }
        return headers;
    }

    private static <T,U> ResponseEntity<U> prepareGatewayResponse(ResponseEntity<U> response) {
        if (response.getStatusCode().is2xxSuccessful()) {
            return response;
        }

        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.status(response.getStatusCode());

        if (response.hasBody()) {
            return responseBuilder.body(response.getBody());
        }

        return responseBuilder.build();
    }
}
