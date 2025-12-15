package com.minsu.webservice.global;

import com.minsu.webservice.global.error.ApiResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Map;

@RestController
public class HealthController {

    @Value("${app.version:0.0.1}")
    private String version;

    @Value("${app.buildTime:unknown}")
    private String buildTime;

    @GetMapping("/health")
    public ApiResponse<?> health() {
        return ApiResponse.ok("HEALTH_OK", "OK", Map.of(
                "status", "OK",
                "version", version,
                "buildTime", buildTime,
                "time", Instant.now().toString()
        ));
    }
}
