package ru.jabki.final_user.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class ExternalTodoService {

    private final RestClient restClient;

    public ExternalTodoService(@Value("${external.todo.service.baseurl:http://localhost:8080}") String baseUrl) {
        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public boolean userByIdInvolved(long userId) {

        return Boolean.parseBoolean(restClient
                .get()
                .uri("/api/v1/todo/userByIdInvolved/{id}", userId)
                .retrieve().body(String.class));
    }
}
