package infrastructure.template;


public interface TemplateEngine {

    String render(ModelAndView modelAndView);

    String render(String template);
}
