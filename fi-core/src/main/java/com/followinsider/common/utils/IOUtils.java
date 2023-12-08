package com.followinsider.common.utils;

import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Scanner;

@UtilityClass
public class IOUtils {

    public static Optional<String> loadResourceFile(String path) {
        InputStream stream = IOUtils.class.getResourceAsStream(path);
        if (stream == null) return Optional.empty();

        String content = new Scanner(stream, StandardCharsets.UTF_8)
                .useDelimiter("\\A")
                .next();

        return Optional.of(content);
    }

    public static Optional<String> loadFile(String path) {
        try {
            String content = Files.readString(Path.of(path));
            return Optional.of(content);

        } catch (IOException e) {
            return Optional.empty();
        }
    }

}
