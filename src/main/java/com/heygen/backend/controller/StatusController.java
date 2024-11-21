package com.heygen.backend.controller;

import org.springframework.web.bind.annotation.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;


@RestController
@RequestMapping("/status")
public class StatusController {
    private String status = "pending";
    private final List<String> logs = new ArrayList<>();
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    @GetMapping
    public StatusResponse getStatus() {
        return new StatusResponse(status);
    }

    @PostMapping("/test")
    public String setTestParameters(@RequestParam String result, @RequestParam int delay) {
        status = "pending";

        scheduler.schedule(() -> {
            if ("completed".equalsIgnoreCase(result)) {
                status = "completed";
            } else if ("error".equalsIgnoreCase(result)) {
                status = "error";
            } else {
                status = "invalid";
            }
        }, delay, TimeUnit.SECONDS);

        return String.format("Test setup successful! Result will be '%s' after %d seconds.", result, delay);
    }

    @GetMapping("/run-test")
    public List<String> runTest() throws IOException, InterruptedException {
        logs.clear();
        int maxRetries = 10;
        long delay = 1000;
        int retry = 0;

        logs.add("Starting integration test...");

        while (retry < maxRetries) {
            logs.add(String.format("Request #%d - Status: %s", retry + 1, status));

            if ("completed".equals(status)) {
                logs.add("Task completed successfully!");
                logs.add("Integration Test Completed - Final Result: completed");
                logs.add("Test Passed: Task completed successfully.");
                break;
            } else if ("error".equals(status)) {
                logs.add("Task encountered an error.");
                logs.add("Integration Test Completed - Final Result: error");
                logs.add("Test Passed: Task encountered an error.");
                break;
            }

            retry++;
            logs.add(String.format("Task is still pending, retrying... (Retry #%d, Delay: %d ms)", retry, delay));
            TimeUnit.MILLISECONDS.sleep(delay);
            delay *= 1.5;
        }

        if (retry >= maxRetries) {
            logs.add("Test failed: Max retries reached.");
        }

        logs.add("Integration test completed.");
        return logs;
    }

}

class StatusResponse {
    private String result;

    public StatusResponse(String result) {
        this.result = result;
    }

    public String getResult() {
        return result;
    }
}

@Controller
class TestPageController {

    @GetMapping("/test")
    public String testPage() {
        return "forward:/test.html";
    }
}