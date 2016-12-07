package coe.wuti.wicket.var;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;

/**
 * Creado por Jesus el 19/5/15.
 * <p>
 * It will inform its listener as soon as the contain of the field changes
 */
@SuppressWarnings("UnusedDeclaration")
public class TextFieldAuto<T> extends TextField<T> {
    public static interface Listener {
        public void contentChanged(AjaxRequestTarget target);
    }

    public TextFieldAuto(String id, IModel<T> iModel, Listener listener) {
        super(id, iModel);

        add(new AjaxFormComponentUpdatingBehavior("onkeyup") {
                @Override
                protected void onUpdate(AjaxRequestTarget target) {
                    listener.contentChanged(target);
                }
            }
        );
    }
}