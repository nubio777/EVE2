package coe.wuti.wicket.var;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import java.util.ArrayList;
import java.util.List;

/**
 * User: jesus
 * Date: 9/1/15
 */
public class DropDownChooseOne<T> extends DropDownChoice<T> {

    public DropDownChooseOne(String id, List<? extends T> choices) {
        super(id, prepareOptions(choices));
    }

    public DropDownChooseOne(String id, IModel<T> model, List<? extends T> opciones) {
        super(id, model, prepareOptions(opciones));
    }

    public DropDownChooseOne(String id, IModel<T> model, PropertyModel<List<T>> modeloOPciones) {
        super(id, model, prepareModel(modeloOPciones));
    }

    private static List prepareOptions(List inputOptions) {
        if (inputOptions != null && !inputOptions.contains(null)) {
            List newOpctions = new ArrayList(inputOptions);
            newOpctions.add(0, null);
            return newOpctions;
        }
        return inputOptions;
    }

    private static <T> PropertyModel<List<T>> prepareModel(PropertyModel<List<T>> optionsModel) {
        List<? extends T> opcionesDeEntrada = optionsModel.getObject();
        List newOptions = prepareOptions(opcionesDeEntrada);
        optionsModel.setObject(newOptions);
        return optionsModel;
    }
}
