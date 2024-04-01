package com.memori.memori_api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/heartbeat", produces = "application/json")
public class HeartbeatController {
    @GetMapping
    public ResponseEntity<String> heartbeat() {
        String responseBody = "{\"status\": \"OK\"}";
        return ResponseEntity.ok().body(responseBody);
    }
}
