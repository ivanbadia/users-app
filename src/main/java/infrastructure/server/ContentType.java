package infrastructure.server;


import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public enum  ContentType {

    JSON("application/json"), XML("application/xml"), HTML("text/html"), FORM_URL_ENCODED("application/x-www-form-urlencoded");

    private final String value;


    private static final Map<String, ContentType> byValue = new HashMap<>();
    static {
        for (ContentType e : ContentType.values()) {
            byValue.put(e.value(), e);
        }
    }

    ContentType(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }


    public static ContentType byValue(String contentType){
        return byValue.get(contentType);
    }

    public static boolean isSupportedContentType(String contentType){
        return Arrays.stream(values())
                .map(ContentType::value)
                .filter(description -> description.equals(contentType))
                .findFirst()
                .isPresent();
    }
}
