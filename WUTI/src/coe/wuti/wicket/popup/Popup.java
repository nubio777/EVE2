package coe.wuti.wicket.popup;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;

import java.util.Arrays;

/**
 * User: jrubio
 * Date: 13-jun-2013
 * <p>
 * Panel que puede funcionar como popup mediante la librería bPopup
 */
public class Popup extends Panel {

    private static final String opcionesBPopup = "{follow: [false, false], escClose: false, modalClose: false, modalColor:'#081613'}";

    private WebMarkupContainer wmcInterno;

    public Popup(String id) {
        super(id);
        setOutputMarkupId(true);

        // El div que podrá estar vacío o con contenido y sobre el que se hará el popup
        wmcInterno = new WebMarkupContainer("contenedorInternoPopup");
        wmcInterno.setOutputMarkupId(true);
        add(wmcInterno);

        // Por defecto, ponemos un panel vacío
        wmcInterno.add(new EmptyPanel("contenidoInternoPopup"));
    }

    public static String getWicketIdForComponents() {
        return "contenidoPrincipalPopup";
    }

    public void showComponent(Component component, String title, AjaxRequestTarget target, PopupButton... closeButtons) {
        // Ponemos el componente dentro del popup
        wmcInterno.addOrReplace(new FInterno("contenidoInternoPopup", title, component, target, closeButtons));
        target.add(wmcInterno);

        // Hacemos "popup"
        target.appendJavaScript("$('#" + wmcInterno.getMarkupId() + "').bPopup(" + opcionesBPopup + "); ");
    }

    public void showMessage(String msg, String title, AjaxRequestTarget target, PopupButton... closeButtons) {

        Label lbMsg = new Label(getWicketIdForComponents(), msg);
        lbMsg.setEscapeModelStrings(false);
        lbMsg.setOutputMarkupId(true);

        if (closeButtons != null && closeButtons.length > 0)
            showComponent(lbMsg, title, target, closeButtons);

        else
            // Si no hay botones, ponemos uno automáticamente
            showComponent(lbMsg, title, target, new PopupButton.OK());
    }

    public void showMessage(String msg, AjaxRequestTarget target, PopupButton... closeButtons) {
        showMessage(msg, "Information", target, closeButtons);
    }

    public void close(AjaxRequestTarget target) {
        String selectorCSS = "#" + wmcInterno.getMarkupId();
        String js = "$('" + selectorCSS + "').bPopup().close(true);";
        target.prependJavaScript(js);

        wmcInterno.addOrReplace(new EmptyPanel("contenidoInternoPopup"));
        target.add(wmcInterno);
    }

    public static interface ListenerEdicion {
        public abstract void aceptado(AjaxRequestTarget target);

        public abstract void cancelado(AjaxRequestTarget target);
    }

    private class FInterno extends Fragment {

        private boolean focoFijado = false;

        FInterno(String id, String titulo, Component principal, AjaxRequestTarget target, PopupButton... botones) {
            super(id, "fInterno", Popup.this);
            setOutputMarkupId(true);

            add(new Label("titulo", titulo).setOutputMarkupId(true));

            add(new ListView<PopupButton>("repetidorBotones", Arrays.asList(botones)) {
                @Override
                protected void populateItem(ListItem<PopupButton> item) {
                    final PopupButton popupButton = item.getModelObject();

                    AjaxLink link = new AjaxLink("boton") {
                        @Override
                        public void onClick(AjaxRequestTarget ajaxRequestTarget) {
                            popupButton.pulsado(Popup.this, ajaxRequestTarget);
                        }

                        @Override
                        protected void onComponentTag(ComponentTag tag) {
                            // Reclamamos foco, si lo necesitamos
                            if (popupButton.isReaccionaAlEscape() || popupButton.isReaccionaAlEnter()) {
                                tag.put("tabindex", "1");           // Permite que el componente pueda tener foco
                                tag.put("style", "outline:none;");  // Para no dibujar un rectángulo azul en torno al componente con foco
                            }

                            // Respuesta al enter (basta con poner ahí el foco, lo sabe hacer wicket)
                            if (popupButton.isReaccionaAlEnter() || botones.length == 1)
                                if (!focoFijado) target.focusComponent(this); // Si el foco se ha ido a un botón con "Esc", ya no se reutilizará el foco
//                                if (!focoFijado) add(new FocusOnLoadBehavior());

                            // Reacción a tecla escape: necesitmos jquery para forzar un click
                            if (popupButton.isReaccionaAlEscape() || botones.length == 1) {
                                String js = "if(event.keyCode == 27) $('#" + getMarkupId() + "').click() ;"; // 27 es el código de la tecla "Esc"
                                tag.put("onkeydown", js);
                                target.focusComponent(this);
                                focoFijado = true;
//                                add(new FocusOnLoadBehavior());
                            }

                            super.onComponentTag(tag);
                        }
                    };

                    link.setOutputMarkupId(true);
                    item.add(link);

                    popupButton.setAjaxLinkAsociado(link);

                    Label icono = new Label("icono");
                    icono.add(new AttributeModifier("class", popupButton.getNombreClaseConIcono()));
                    link.add(icono);

                    link.add(new Label("texto", popupButton.getNombre()));
                }
            });

            add(principal);
        }
    }
}

