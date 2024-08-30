package com.followinsider.modules.logging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.followinsider.common.utils.ListUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class LogService {

    private static final String FILE = "logs/app.log";

    private static final String BACKUP_FILE = "logs/app-%d.log";

    private static final int BACKUP_COUNT = 2;

    private final ObjectMapper jsonMapper;

    public List<Log> getLogs(LogLevel level, int limit, boolean inverse) throws IOException {
        List<Log> logs = getLogs(level, limit, getFiles());
        return inverse ? ListUtils.inverse(logs) : logs;
    }

    private String[] getFiles() {
        String[] files = new String[BACKUP_COUNT + 1];
        files[0] = FILE;

        for (int i = 1; i <= BACKUP_COUNT; i++) {
            files[i] = String.format(BACKUP_FILE, i);
        }
        return files;
    }

    private List<Log> getLogs(LogLevel level, int limit, String[] files) throws IOException {
        List<Log> logs = new ArrayList<>();

        for (int i = 0; i < files.length && limit > 0; i++) {
            List<Log> fileLogs = getLogs(level, limit, files[i]);
            logs.addAll(fileLogs);
            limit -= fileLogs.size();
        }
        return logs;
    }

    private List<Log> getLogs(LogLevel level, int limit, String file) throws IOException {
        Path path = Path.of(file);

        if (!Files.exists(path))
            return new ArrayList<>();

        try (Stream<String> stream = Files.lines(path)) {
            return getStreamLogs(stream, level, limit);
        }
    }

    private List<Log> getStreamLogs(Stream<String> stream, LogLevel level, int limit) {
        return stream
            .map(this::parseLog)
            .filter(log -> log != null && log.logLevel().ordinal() >= level.ordinal())
            .limit(limit)
            .collect(Collectors.toList());
    }

    private Log parseLog(String line) {
        try {
            return jsonMapper.readValue(line, Log.class);
        } catch (JsonProcessingException e) {
            log.error("Failed log parsing :: line: '{}', error: '{}'", line, e.getMessage());
            return null;
        }
    }

}
