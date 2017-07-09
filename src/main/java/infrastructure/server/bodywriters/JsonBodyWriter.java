package infrastructure.server.bodywriters;


import com.fasterxml.jackson.databind.ObjectMapper;
import infrastructure.server.exceptions.InternalServerException;

import java.io.IOException;
import java.io.OutputStream;

public class JsonBodyWriter implements BodyWriter{
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void write(Object object, OutputStream os) {
        try {
            objectMapper.writeValue(os, object);
        } catch (IOException e) {
            throw new InternalServerException("Error writing json", e);
        }
    }
}
