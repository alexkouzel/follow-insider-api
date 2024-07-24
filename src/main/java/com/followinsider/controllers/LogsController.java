package com.followinsider.controllers;

import com.followinsider.modules.logging.Log;
import com.followinsider.modules.logging.LogLevel;
import com.followinsider.modules.logging.LogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/logs")
@RequiredArgsConstructor
@Slf4j
public class LogsController {

    private final LogService logService;

    @GetMapping
    public List<Log> logs(
            @RequestParam(value = "level", defaultValue = "INFO") LogLevel level,
            @RequestParam(value = "limit", defaultValue = "100") int limit,
            @RequestParam(value = "inverse", defaultValue = "true") boolean inverse
    ) throws IOException {
        for (int i = 0; i < 200; i++) {
            log.info("{}", i);
        }
        return logService.getLogs(level, limit, inverse);
    }

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(IOException.class)
    public void handleIOException(IOException e) {
    }

}
