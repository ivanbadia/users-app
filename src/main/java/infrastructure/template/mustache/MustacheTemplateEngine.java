package infrastructure.template.mustache;


import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import infrastructure.template.ModelAndView;
import infrastructure.template.TemplateEngine;

import java.io.StringWriter;

public class MustacheTemplateEngine implements TemplateEngine{

    private static final String TEMPLATES_EXTENSION = ".mustache";
    private static final String FOLDER = "templates/";

    @Override
    public String render(ModelAndView modelAndView) {
        return render( modelAndView.getView(), modelAndView.getModel());
    }

    private String render(String view, Object model) {
        //I create a instance of DefaultMustacheFactory so there is no cache of compiled templates
        MustacheFactory mf = new DefaultMustacheFactory();
        Mustache mustache = mf.compile(FOLDER +view +TEMPLATES_EXTENSION);
        StringWriter stringWriter = new StringWriter();
        mustache.execute(stringWriter, model);
        return stringWriter.toString();
    }

    @Override
    public String render(String view) {
        return render(view, null);
    }
}
