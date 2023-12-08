package com.followinsider.core.logging;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class LogService {

    @Setter
    @Value("${logging.file.name}")
    private String logsPath;

    @PostConstruct
    public void init() {
        clearLogs();
    }

    public List<Log> getLogs(int from, int to, Set<String> exclude) {
        return getLogs(from, to, exclude, true);
    }

    public List<Log> getLogs(int from, int to, Set<String> exclude, boolean reverseOrder) {
        Deque<Log> logs = new ArrayDeque<>();

        Path projectRoot = Paths.get(System.getProperty("user.dir"));
        Path path = projectRoot.resolve(logsPath);

        try (LineNumberReader reader = new LineNumberReader(Files.newBufferedReader(path))) {
            ObjectMapper mapper = new ObjectMapper();
            String line;

            while ((line = reader.readLine()) != null) {
                int lineNumber = reader.getLineNumber();
                if (lineNumber >= from && lineNumber <= to) {
                    Log log = mapper.readValue(line, Log.class);
                    if (anyCaseContains(log.getType(), exclude)) {
                        continue;
                    }
                    if (reverseOrder) {
                        logs.addFirst(log);
                    } else {
                        logs.add(log);
                    }
                } else if (lineNumber > to) {
                    break;
                }
            }
        } catch (IOException e) {
            log.error("Failed loading logs :: {}", e.getMessage());
        }
        return new ArrayList<>(logs);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    public void clearLogs() {
        try {
            new FileWriter(logsPath, false).close();
        } catch (IOException e) {
            log.error("Failed clearing logs :: {}", e.getMessage());
        }
    }

    private boolean anyCaseContains(String value, Set<String> values) {
        if (value == null) return false;
        value = value.toUpperCase();
        return values.stream()
                .filter(Objects::nonNull)
                .map(String::toUpperCase)
                .anyMatch(value::equals);
    }

}
