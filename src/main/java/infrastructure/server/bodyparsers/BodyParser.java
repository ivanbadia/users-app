package infrastructure.server.bodyparsers;

import java.io.InputStream;

public interface BodyParser {


    /**
     * Returns the Java object populated with the content of the input stream
     *
     * @param inputStream Stream from which the body can be read
     * @param classOfT The class we expect
     * @return The object instance
     */
    <T> T parse(InputStream inputStream, Class<T> classOfT);
}
