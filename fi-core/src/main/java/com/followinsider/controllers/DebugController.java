package com.followinsider.controllers;

import com.followinsider.core.logging.Log;
import com.followinsider.core.logging.LogLevel;
import com.followinsider.core.logging.LogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/debug")
@RequiredArgsConstructor
public class DebugController {

    private final LogService logService;

    @GetMapping("/logs")
    public List<Log> getCurrentLogs(
            @RequestParam(value = "level", defaultValue = "INFO") LogLevel level,
            @RequestParam(value = "file", defaultValue = "app.log") String file
    ) throws IOException {
        return logService.getFileLogs(file, level);
    }

    @DeleteMapping("/logs/clear")
    public void clearLogs() throws IOException {
        logService.clearLogs();
    }

    @ExceptionHandler(IOException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleIOException(IOException e) {
        return e.getMessage();
    }

}
