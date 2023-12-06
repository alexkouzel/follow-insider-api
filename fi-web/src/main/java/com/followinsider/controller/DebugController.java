package com.followinsider.controller;

import com.followinsider.core.logging.LogService;
import com.followinsider.core.logging.Log;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/debug")
@RequiredArgsConstructor
public class DebugController {

    private final LogService logService;

    @GetMapping("/logs")
    public List<Log> getLogs(@RequestParam(value = "from", defaultValue = "0") int from,
                             @RequestParam(value = "to", defaultValue = "100") int to,
                             @RequestParam(value = "exclude", defaultValue = "") Set<String> exclude) {
        return logService.getLogs(from, to, exclude);
    }

    @DeleteMapping("/logs/clear")
    public void clearLogs() {
        logService.clearLogs();
    }

}
