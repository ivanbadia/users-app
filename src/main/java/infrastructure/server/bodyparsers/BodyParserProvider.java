package infrastructure.server.bodyparsers;


import infrastructure.server.ContentType;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class BodyParserProvider {

    private static final Map<ContentType, BodyParser> bodyParsersByContentType = Collections.unmodifiableMap(new HashMap<ContentType, BodyParser>() {
        {
            put(ContentType.JSON, new JsonBodyParser());
            put(ContentType.XML, new XmlBodyParser());
        }
    });

    private static class BodyParserProviderHolder {
        static final BodyParserProvider INSTANCE = new BodyParserProvider();
    }

    public static BodyParserProvider instance(){
        return BodyParserProviderHolder.INSTANCE;
    }

    public Optional<BodyParser> bodyParserFor(ContentType contentType){
        return Optional.ofNullable(bodyParsersByContentType.get(contentType));
    }
}
