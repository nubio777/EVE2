package coe.lsm.gui.pages.login;


import coe.lsm.gui.app.App;
import coe.lsm.gui.pages.base.WPBase;
import coe.lsm.gui.pages.main.WPMain;
import coe.lsm.main.model.Team;
import coe.wuti.wicket.popup.Popup;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.validator.StringValidator;

/**
 * User: jrubio
 */
public class WPLogin extends WPBase {
    String teamNumber = "", pwd = "";

    public WPLogin() {
        App.get().getLSMSession().setAuthenticated(false);

        // Main form
        Form form = new StatelessForm("form");
        form.setOutputMarkupId(true);
        add(form);

        // Feedback
        FeedbackPanel feedbackPanel = new FeedbackPanel("feedback");
        feedbackPanel.setOutputMarkupId(true);
        feedbackPanel.setVisible(false);
        form.add(feedbackPanel);

        // Popup
        Popup popup = new Popup("popup");
        add(popup);

        // Fields
        form.add(new TextField<>("teamNumber", new PropertyModel<>(this, "teamNumber")).setRequired(true).add(StringValidator.lengthBetween(1, 20)));
        form.add(new PasswordTextField("password", new PropertyModel<>(this, "pwd")).setRequired(true).add(StringValidator.lengthBetween(1, 20)));

        // Submit
        AjaxSubmitLink alSubmit = new AjaxSubmitLink("submit") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                Team team = App.get().getConfiguration().teamFor(teamNumber, pwd);
                if (team != null) {
                    App.get().getLSMSession().setLoginTeam(team);
                    App.get().getLSMSession().setAuthenticated(true);
                    setResponsePage(WPMain.class);
                } else {
                    error ("Incorrect team and/or password") ;
                    feedbackPanel.setVisible(true);
                    target.add(form);
                }
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                feedbackPanel.setVisible(true);
                target.add(form);
            }
        };

        form.add(alSubmit);
        form.setDefaultButton(alSubmit);
    }


    @Override
    public String getPageTitle() {
        return "LS16 MV - Login";
    }
}
