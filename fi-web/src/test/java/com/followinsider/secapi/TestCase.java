package com.followinsider.secapi;

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

public class TestCase {

    protected <T> T loadXmlResource(String name, Class<T> type) throws IOException {
        return loadResource(name, type, new XmlMapper());
    }

    protected <T> T loadJsonResource(String name, Class<T> type) throws IOException {
        return loadResource(name, type, new JsonMapper());
    }

    protected <T> T loadResource(String name, Class<T> type, ObjectMapper mapper) throws IOException {
        return mapper.readValue(loadResourceFile(name), type);
    }

    protected String loadTxtResource(String name) throws IOException {
        try (InputStream stream = loadStreamResource(name)) {
            byte[] bytes = stream.readAllBytes();
            return new String(bytes, StandardCharsets.UTF_8);
        }
    }

    protected InputStream loadStreamResource(String name) {
        return this.getClass().getClassLoader().getResourceAsStream(name);
    }

    protected File loadResourceFile(String name) throws IOException {
        URL url = this.getClass().getClassLoader().getResource(name);
        try {
            return new File(Objects.requireNonNull(url).toURI());
        } catch (URISyntaxException e) {
            throw new IOException(e.getMessage());
        }
    }

}
