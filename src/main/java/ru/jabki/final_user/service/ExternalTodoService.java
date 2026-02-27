package ru.jabki.final_user.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class ExternalTodoService {

    private final RestClient restClient;

    public ExternalTodoService() {
        this.restClient = RestClient.builder()
                .baseUrl("http://localhost:8080/api/v1/todo")
                .build();
    }

    public boolean userByIdInvolved(long userId) {

        return Boolean.parseBoolean(restClient
                .get()
                .uri("/userByIdInvolved/{id}", userId)
                .retrieve().body(String.class));
    }
}
