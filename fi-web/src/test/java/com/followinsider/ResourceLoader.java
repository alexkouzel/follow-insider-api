package com.followinsider;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class ResourceLoader {

    public String loadTxt(String name) throws IOException {
        try (InputStream stream = loadStream(name)) {
            byte[] bytes = stream.readAllBytes();
            return new String(bytes, StandardCharsets.UTF_8);
        }
    }

    public <T> T loadXml(String name, Class<T> type) throws IOException {
        return loadByMapper(name, type, new XmlMapper());
    }

    public <T> T loadJson(String name, Class<T> type) throws IOException {
        return loadByMapper(name, type, new JsonMapper());
    }

    private <T> T loadByMapper(String name, Class<T> type, ObjectMapper mapper) throws IOException {
        return mapper.readValue(loadResourceFile(name), type);
    }

    private InputStream loadStream(String name) {
        return this.getClass().getClassLoader().getResourceAsStream(name);
    }

    private File loadResourceFile(String name) throws IOException {
        URL url = this.getClass().getClassLoader().getResource(name);
        try {
            return new File(Objects.requireNonNull(url).toURI());
        } catch (URISyntaxException e) {
            throw new IOException(e.getMessage());
        }
    }

}
