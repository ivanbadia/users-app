package infrastructure;


import infrastructure.server.Server;
import infrastructure.server.httpserver.SunHttpServer;
import infrastructure.session.LocalSessionStore;
import infrastructure.session.SessionStore;
import infrastructure.template.TemplateEngine;
import infrastructure.template.mustache.MustacheTemplateEngine;

public class InfrastructureContext {

    public static TemplateEngine templateEngine(){
        return new MustacheTemplateEngine();
    }

    public static SessionStore sessionStore(){
        return LocalSessionStore.instance();
    }

    public static Server server(){
        return new SunHttpServer();
    }
}
