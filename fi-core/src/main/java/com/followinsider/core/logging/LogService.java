package com.followinsider.core.logging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.followinsider.common.utils.ListUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class LogService {

    @Value("${logging.dir}")
    private String logsDir;

    private final ObjectMapper jsonMapper;

    public void ping() {
        log.info("--- PING ---");
    }

    public List<Log> getFileLogs(String filename, LogLevel level, boolean inverse) throws IOException {
        Path path = Path.of(logsDir + "/" + filename);
        try (Stream<String> stream = Files.lines(path)) {
            List<Log> logs = getStreamLogs(stream, level);
            return inverse ? ListUtils.inverse(logs) : logs;
        }
    }

    private List<Log> getStreamLogs(Stream<String> stream, LogLevel level) {
        return stream
                .map(this::sneakyParseLog)
                .filter(log -> log.level().ordinal() >= level.ordinal())
                .collect(Collectors.toList());
    }

    @SneakyThrows
    private Log sneakyParseLog(String line) {
        return jsonMapper.readValue(line, Log.class);
    }

}
