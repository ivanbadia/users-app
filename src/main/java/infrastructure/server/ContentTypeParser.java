package infrastructure.server;


import java.util.Arrays;
import java.util.Optional;

public class ContentTypeParser {

    /**
     * Retrieves the supported content-type from the content-type header
     * @param contentTypeHeader
     * @return
     */
    public static Optional<ContentType> extractContentTypeFrom(String contentTypeHeader) {
        return extractContentType(contentTypeHeader, ";");
    }

    private static Optional<ContentType> supportedContentTypeFrom(String[] contentTypes) {
        return Arrays.stream(contentTypes)
                .map(String::trim)
                .filter(ContentType::isSupportedContentType)
                .findFirst()
                .map(ContentType::byValue);
    }


    /**
     * Retrieves the supported content-type from the Accept headerValue
     * @param headerValue
     * @return
     */
    public static Optional<ContentType> extractAcceptedContentTypeFrom(String headerValue) {
        return extractContentType(headerValue, ",");
    }

    private static Optional<ContentType> extractContentType(String headerValue, String separator) {
        return Optional.ofNullable(headerValue)
                .flatMap(header -> supportedContentTypeFrom(header.split(separator)));
    }

}
