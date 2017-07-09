package infrastructure.template.mustache;

import infrastructure.server.EntityForTesting;
import infrastructure.template.ModelAndView;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class MustacheTemplateEngineTest {

    @Test
    public void should_render_the_page_with_the_model_data()  {
        //Given a ModelAndView that uses the template src/test/resources/test_template.mustache
        ModelAndView modelAndView = new ModelAndView(new EntityForTesting(1, "hello"), "test_template");

        //When
        String result = new MustacheTemplateEngine().render(modelAndView);

        //Then
        assertThat(result)
                .isEqualTo("Id 1 and message hello");
    }


    @Test
    public void should_render_the_page_without_model()  {
        //Given a template src/test/resources/test_template_without_variable.mustache that hasn't got variables
        String template = "test_template_without_variable";

        //When
        String result = new MustacheTemplateEngine().render(template);

        //Then
        assertThat(result)
                .isEqualTo("Hello!!!");
    }
}