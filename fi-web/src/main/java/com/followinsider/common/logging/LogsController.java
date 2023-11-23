package com.followinsider.common.logging;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/logs")
@RequiredArgsConstructor
public class LogsController {

    private final LogsService logsService;

    @GetMapping("/")
    public List<Log> getLogs(@RequestParam(value = "from", defaultValue = "0") int from,
                             @RequestParam(value = "to", defaultValue = "100") int to,
                             @RequestParam(value = "exclude", defaultValue = "") Set<String> exclude) {
        return logsService.getLogs(from, to, exclude);
    }

    @DeleteMapping("/clear")
    public void clearLogs() {
        logsService.clearLogs();
    }

}
