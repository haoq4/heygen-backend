package com.heygen.backend;

import com.heygen.client.VideoTranslationClient;

import java.io.IOException;

public class IntegrationTest {
    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("Starting integration test...");

        VideoTranslationClient client = new VideoTranslationClient("http://localhost:8080", 10, 1000);

        String result = client.getStatus();

        System.out.printf("Integration Test Completed - Final Result: %s%n", result);

        if ("completed".equals(result)) {
            System.out.println("Test Passed: Task completed successfully.");
        } else if ("error".equals(result)) {
            System.out.println("Test Passed: Task encountered an error.");
        } else {
            System.out.println("Test Failed: Unexpected result.");
        }
    }
}