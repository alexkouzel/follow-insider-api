package com.followinsider.common.utils;

import lombok.experimental.UtilityClass;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@UtilityClass
public class IOUtils {

    public static void readResourceByLine(String path, Consumer<String> consumer) throws IOException {
        InputStream stream = IOUtils.class.getResourceAsStream(path);
        StringUtils.readByLine(stream, consumer);
    }

    public static void clearDirectory(Path path, Function<String, Boolean> verifier) throws IOException {
        Files.walkFileTree(path, new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                String filename = file.getFileName().toString();

                if (!verifier.apply(filename)) {
                    throw new IOException("Failed to verify filename: " + filename);
                }
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    public static void forEachDirectoryFile(Path path, Consumer<Path> consumer) throws IOException {
        getDirectoryFilePaths(path).forEach(consumer);
    }

    public static List<Path> getDirectoryFilePaths(Path path) throws IOException {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
            return StreamSupport
                    .stream(stream.spliterator(), false)
                    .collect(Collectors.toList());
        }
    }

    public static List<String> readFileLines(Path path) throws IOException {
        List<String> lines = new ArrayList<>();
        readFileByLine(path, lines::add);
        return lines;
    }

    public static void readFileByLine(Path path, Consumer<String> consumer) throws IOException {
        InputStream stream = readFileAsStream(path);
        StringUtils.readByLine(stream, consumer);
    }

    public static InputStream readFileAsStream(Path path) throws FileNotFoundException {
        File file = new File(path.toString());
        return new FileInputStream(file);
    }

}
