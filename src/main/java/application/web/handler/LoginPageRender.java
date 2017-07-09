package application.web.handler;

import annotations.VisibleForTesting;
import infrastructure.template.ModelAndView;
import infrastructure.template.TemplateEngine;
import io.reactivex.Maybe;

import java.util.Map;

public class LoginPageRender {

    public static final String ERROR_MESSAGE_VARIABLE = "message";
    public static final String USERNAME_VARIABLE = "username";
    public static final String PASSWORD_VARIABLE = "password";
    public static final String FROM_VARIABLE = "from";
    @VisibleForTesting
    public static final String LOGIN_TEMPLATE = "login";

    private TemplateEngine templateEngine;

    public LoginPageRender(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }


    public Maybe<String> render(){
        return Maybe.just(templateEngine.render(LOGIN_TEMPLATE));
    }

    public Maybe<String> render(Map<String, Object> scopes){
        return  Maybe.just(templateEngine.render(new ModelAndView(scopes, LOGIN_TEMPLATE)));
    }
}
