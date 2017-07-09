package infrastructure.server.bodyparsers;


import com.fasterxml.jackson.databind.ObjectMapper;
import infrastructure.server.exceptions.BadRequestException;

import java.io.IOException;
import java.io.InputStream;

public class JsonBodyParser implements BodyParser{
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public <T> T parse(InputStream inputStream, Class<T> classOfT) {
        try {
            return objectMapper.readValue(inputStream, classOfT);
        } catch (IOException e) {
            throw new BadRequestException("The Json is not valid", e);
        }
    }
}
