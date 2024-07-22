package com.followinsider.modules.logging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.followinsider.common.utils.ListUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class LogService {

    @Value("${logging.file.name}")
    private String filename;

    private final ObjectMapper jsonMapper;

    public List<Log> getLogs(LogLevel level, int limit, boolean inverse) throws IOException {
        Path path = Path.of(filename);

        try (Stream<String> stream = Files.lines(path)) {
            List<Log> logs = getStreamLogs(stream, level, limit);
            return inverse ? ListUtils.inverse(logs) : logs;
        }
    }

    private List<Log> getStreamLogs(Stream<String> stream, LogLevel level, int limit) {
        return stream
                .map(this::parseLog)
                .filter(log -> log != null && log.level().ordinal() >= level.ordinal())
                .limit(limit)
                .collect(Collectors.toList());
    }

    private Log parseLog(String line) {
        try {
            return jsonMapper.readValue(line, Log.class);
        } catch (JsonProcessingException e) {
            log.error("Failed to parse a log: {}", line);
            return null;
        }
    }

}
