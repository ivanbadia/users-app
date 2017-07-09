package infrastructure.session;

import java.util.UUID;


final class SessionIdGenerator {


    private SessionIdGenerator(){}

    public static String generateSessionId() {
        return UUID.randomUUID().toString();
    }

}
