package infrastructure.server;


import annotations.VisibleForTesting;
import infrastructure.server.constants.Headers;

import java.net.HttpCookie;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public abstract class AbstractHttpResponse implements HttpResponse{


    private Integer statusCode;
    private final Map<String, String> headers = new HashMap<>();
    @VisibleForTesting
    protected Object body;

    protected AbstractHttpResponse() {
    }

    @Override
    public HttpResponse setStatusCode(int statusCode) {
        this.statusCode = statusCode;
        return this;
    }

    @Override
    public HttpResponse setBody(Object body) {
        this.body = body;
        return this;
    }


    public Optional<String> contentType(){
        return header(Headers.CONTENT_TYPE);
   }

    @Override
    public Optional<String> header(String header){
        return Optional.ofNullable(this.headers.get(header));
    }

    @Override
    public HttpResponse setContentType(ContentType contentType){
        if(contentType!=null) {
            addHeader(Headers.CONTENT_TYPE, contentType.value());
        }
        return this;
    }

    @Override
    public HttpResponse addHeader(String name, String value) {
        this.headers.put(name, value);
        return this;
    }

    @Override
    public HttpResponse addCookie(HttpCookie cookie) {
        //the HttpCookie.toString doesn't add httponly even when httponly is true so it's necessary to add it
        addHeader(Headers.SET_COOKIE, cookie.toString()+"; HttpOnly");
        return this;
    }


    protected Map<String, String> headers(){
        return this.headers;
    }


    @Override
    public Integer statusCode() {
        return statusCode;
    }

    @Override
    public boolean hasBody(){
        return body!=null;
    }


}
