package application.web.controller;

import annotations.VisibleForTesting;
import application.auth.handler.SessionAuthFilter;
import application.web.handler.LoginUser;
import application.web.handler.LogoutUser;
import application.web.handler.RenderHelloPage;
import application.web.handler.LoginPageRender;
import domain.model.user.AuthenticationService;
import domain.model.user.Role;
import infrastructure.server.Controller;
import infrastructure.session.SessionStore;
import infrastructure.template.TemplateEngine;
import io.reactivex.Maybe;

import static infrastructure.server.ContentType.HTML;
import static infrastructure.server.Routes.filter;
import static infrastructure.server.Routes.get;
import static infrastructure.server.Routes.post;


public class ApplicationController  implements Controller{

    private static final String LOGIN = "/web/login";
    @VisibleForTesting
    static final String HOME_PAGE = "/web/restricted/links";
    @VisibleForTesting
    static final String PAGE1 = "/web/restricted/page1";
    @VisibleForTesting
    static final String PAGE2 = "/web/restricted/page2";
    @VisibleForTesting
    static final String PAGE3 = "/web/restricted/page3";
    @VisibleForTesting
    static final String LOGOUT = "/web/logout";

    private final TemplateEngine templateEngine;
    private final SessionStore sessionStore;
    private final AuthenticationService authenticationService;

    public ApplicationController(TemplateEngine templateEngine, SessionStore sessionStore, AuthenticationService authenticationService) {
        this.templateEngine = templateEngine;
        this.sessionStore = sessionStore;
        this.authenticationService = authenticationService;
    }

    @Override
    public void configure() {

        get("/")
                .handler(httpContext -> {
                    httpContext.response().redirect(LOGIN);
                    return Maybe.empty();
                });

        LoginPageRender loginPageRender = new LoginPageRender(templateEngine);

        get(LOGIN)
                .produces(HTML)
                .handler(httpContext -> loginPageRender.render());


        post(LOGIN)
                .produces(HTML)
                .handler(new LoginUser(sessionStore, authenticationService, loginPageRender, HOME_PAGE));


        post(LOGOUT)
                .handler(new LogoutUser(sessionStore, LOGIN));


        //Restricted area. Requires session
        filter("/web/restricted/*")
                .handler(new SessionAuthFilter(sessionStore, loginPageRender));

        get(HOME_PAGE)
                .produces(HTML)
                .handler(httpContext -> Maybe.just(templateEngine.render("links")));



        RenderHelloPage renderHelloPage = new RenderHelloPage(templateEngine);

        get(PAGE1)
                .produces(HTML)
                .addAuthority(Role.PAGE_1)
                .handler(renderHelloPage);

        get(PAGE2)
                .produces(HTML)
                .addAuthority(Role.PAGE_2)
                .handler(renderHelloPage);

        get(PAGE3)
                .produces(HTML)
                .addAuthority(Role.PAGE_3)
                .handler(renderHelloPage);



    }


}
