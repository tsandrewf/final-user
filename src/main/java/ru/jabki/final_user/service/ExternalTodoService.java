package ru.jabki.final_user.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@AllArgsConstructor
@Service
public class ExternalTodoService {

    private final RestClient restClient;

    public ExternalTodoService() {
        String baseUrl = null;

        {
            Properties properties = new Properties();
            InputStream inputStream =
                    getClass().getClassLoader().getResourceAsStream("application.properties");
            try {
                properties.load(inputStream);
                baseUrl = properties.getProperty("external.todo.service.baseurl");
            } catch (IOException e) {

            }
        }
        if (baseUrl == null) {
            baseUrl = "http://localhost:8080/api/v1/todo";
        }

        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public boolean userByIdInvolved(long userId) {

        return Boolean.parseBoolean(restClient
                .get()
                .uri("/userByIdInvolved/{id}", userId)
                .retrieve().body(String.class));
    }
}
