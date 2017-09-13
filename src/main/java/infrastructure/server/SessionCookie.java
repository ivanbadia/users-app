package infrastructure.server;


import java.net.HttpCookie;
import java.util.Optional;


class SessionCookie{

    private static final String SESSION_COOKIE_NAME = "JSESSIONID";

    private HttpCookie httpCookie;

    public SessionCookie(String value) {
        httpCookie = new HttpCookie(SESSION_COOKIE_NAME, value);
    }

    private SessionCookie(HttpCookie httpCookie) {
        this.httpCookie = httpCookie;
    }

    public static Optional<SessionCookie> fromHeader(String headerValue){
        return HttpCookie.parse(headerValue)
                .stream()
                .filter(cookie -> cookie.getName().equals(SESSION_COOKIE_NAME))
                .findFirst()
                .map(SessionCookie::new);
    }


    public String getValue(){
        return httpCookie.getValue();
    }


    public String getName(){
        return SESSION_COOKIE_NAME;
    }

    public HttpCookie toHttpCookie(){
        return httpCookie;
    }

    public String asHeaderValue(){
        return httpCookie.toString();
    }
}
