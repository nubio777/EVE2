package coe.wuti.wicket.popup;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;

import java.io.Serializable;

/**
 * Clase que define un botón que se puede añadir al popup
 */
public class PopupButton implements Serializable {

    public static class OK extends PopupButton {
        public OK() {
            super("OK", "icon_check icono");
            setReaccionaAlEnter(true);
        }

        @Override
        public void pulsado(Popup popup, AjaxRequestTarget target) {
            popup.close(target);
        }
    }

    public static class Close extends PopupButton {
        public Close() {
            super("Close", "icon_close_alt2 icono");
            setReaccionaAlEscape(true);
        }

        @Override
        public void pulsado(Popup popup, AjaxRequestTarget target) {
            popup.close(target);
        }
    }

    public static class Cancel extends PopupButton {
        public Cancel() {
            super("Cancel", "icon_close_alt2 icono");
            setReaccionaAlEscape(true);
        }

        @Override
        public void pulsado(Popup popup, AjaxRequestTarget target) {
            popup.close(target);
        }
    }

    public static class Yes extends PopupButton {
        public Yes() {
            super("Yes", "icon_check icono");
            setReaccionaAlEnter(true);
        }

        @Override
        public void pulsado(Popup popup, AjaxRequestTarget target) {
            popup.close(target);
        }
    }

    public static class No extends PopupButton {
        public No() {
            super("No", "icon_close_alt2 icono");
            setReaccionaAlEscape(true);
        }

        @Override
        public void pulsado(Popup popup, AjaxRequestTarget target) {
            popup.close(target);
        }
    }

    public static class Download extends PopupButton {
        private String url;

        public Download(String url) {
            super("Descargar", "arrow_carrot-2down icono");
            this.url = url;
        }

        @Override
        public void pulsado(Popup popup, AjaxRequestTarget target) {
            popup.close(target);
            target.appendJavaScript("window.open('" + url + "')");
        }
    }

    private String nombre;
    private String nombreClaseConIcono = "";
    private AjaxLink ajaxLinkAsociado;
    private boolean reaccionaAlEnter = false;
    private boolean reaccionaAlEscape = false;

    public PopupButton(String nombre) {
        this(nombre, "");
    }

    public PopupButton(String nombre, String nombreClaseConIcono) {
        this.nombre = nombre;
        this.nombreClaseConIcono = nombreClaseConIcono;
    }

    public String getNombre() {
        return nombre;
    }

    public String getNombreClaseConIcono() {
        return nombreClaseConIcono;
    }

    public AjaxLink getAjaxLinkAsociado() {
        return ajaxLinkAsociado;
    }

    public void setAjaxLinkAsociado(AjaxLink ajaxLinkAsociado) {
        this.ajaxLinkAsociado = ajaxLinkAsociado;
    }

    public boolean isReaccionaAlEnter() {
        return reaccionaAlEnter;
    }

    public void setReaccionaAlEnter(boolean reaccionaAlEnter) {
        this.reaccionaAlEnter = reaccionaAlEnter;
    }

    public boolean isReaccionaAlEscape() {
        return reaccionaAlEscape;
    }

    public void setReaccionaAlEscape(boolean reaccionaAlEscape) {
        this.reaccionaAlEscape = reaccionaAlEscape;
    }

    public void pulsado(Popup popup, AjaxRequestTarget target) {
    }
}
