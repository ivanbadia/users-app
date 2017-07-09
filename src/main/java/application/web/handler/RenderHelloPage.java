package application.web.handler;

import infrastructure.server.Handler;
import infrastructure.server.HttpContext;
import infrastructure.template.ModelAndView;
import infrastructure.template.TemplateEngine;
import io.reactivex.Maybe;


public class RenderHelloPage implements Handler<String>{
    private final TemplateEngine templateEngine;

    public RenderHelloPage(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    @Override
    public Maybe<String> handle(HttpContext httpContext) {
        return Maybe.just(templateEngine.render(new ModelAndView(httpContext.user(), "page")));
    }
}
