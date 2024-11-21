package com.heygen.client;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.util.concurrent.TimeUnit;
import java.time.LocalDateTime;

public class VideoTranslationClient {
    private final String baseUrl;
    private final int maxRetries;
    private final long initialDelay;

    public VideoTranslationClient(String baseUrl, int maxRetries, long initialDelay) {
        this.baseUrl = baseUrl;
        this.maxRetries = maxRetries;
        this.initialDelay = initialDelay;
    }

    public String getStatus() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        long delay = initialDelay;

        for (int retry = 0; retry < maxRetries; retry++) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "/status"))
                    .GET()
                    .build();

            LocalDateTime startTime = LocalDateTime.now();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            LocalDateTime endTime = LocalDateTime.now();

            String result = parseResult(response.body());
            System.out.printf(
                    "Request #%d at %s - Response: %s, Duration: %d ms%n",
                    retry + 1, startTime, result, java.time.Duration.between(startTime, endTime).toMillis()
            );

            if ("completed".equals(result)) {
                System.out.println("Task completed successfully!");
                return result;
            } else if ("error".equals(result)) {
                System.out.println("Task encountered an error.");
                return result;
            }

            System.out.println("Task is still pending, retrying...");
            TimeUnit.MILLISECONDS.sleep(delay);
            delay *= 1.5;
        }

        System.out.println("Max retries reached. Task status unknown.");
        return "unknown";
    }

    private String parseResult(String json) {
        return json.replaceAll("\\{\\s*\"result\"\\s*:\\s*\"(.*?)\"\\s*}", "$1");
    }
}
