package infrastructure.server.bodywriters;


import infrastructure.server.ContentType;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class BodyWriterProvider {

    private static final BodyWriter DEFAULT_WRITER = new TextBodyWriter();
    private static final Map<String, BodyWriter> bodyWritersByContentType = Collections.unmodifiableMap(new HashMap<String, BodyWriter>() {
        {
            put(ContentType.JSON.value(), new JsonBodyWriter());
            put(ContentType.XML.value(), new XmlBodyWriter());
            put(ContentType.HTML.value(), new TextBodyWriter());
        }
    });

    private static class BodyWriterProviderHolder {
        static final BodyWriterProvider INSTANCE = new BodyWriterProvider();
    }

    private BodyWriterProvider(){}

    public static BodyWriterProvider instance(){
        return BodyWriterProviderHolder.INSTANCE;
    }

    public BodyWriter bodyWriterFor(String contentType){
        return Optional.ofNullable(bodyWritersByContentType.get(contentType))
                .orElse(DEFAULT_WRITER);
    }
}
