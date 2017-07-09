package infrastructure.template;


import java.util.Objects;

public class ModelAndView {

    private final Object model;
    private final String view;


    public ModelAndView(Object model, String view) {
        this.model = model;
        this.view = view;
    }

    public Object getModel() {
        return model;
    }


    public String getView() {
        return view;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ModelAndView that = (ModelAndView) o;
        return Objects.equals(model, that.model) &&
                Objects.equals(view, that.view);
    }

    @Override
    public int hashCode() {
        return Objects.hash(model, view);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ModelAndView{");
        sb.append("model=").append(model);
        sb.append(", view='").append(view).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
