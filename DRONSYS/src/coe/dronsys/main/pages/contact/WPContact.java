package coe.dronsys.main.pages.contact;

import coe.dronsys.main.pages.base.WPBase;
import coe.wuti.jpa.JPAField;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.form.EmailTextField;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.validator.StringValidator;


/**
 * User: jrubio
 */
public class WPContact extends WPBase {
    Contact contact = new Contact();

    public WPContact() {

        /////////////////////////////
        // Support links
        /////////////////////////////

        add(new RoadToHeaven("openTicket", "At this moment this functionality is beeing updated. <br>Please, keep checking back, because it won't be long now."));
        add(new RoadToHeaven("chatLive", "Sadly, live chat is dead at this very moment.<br> Please, come back later"));

        /////////////////////////////
        //  Contact form
        /////////////////////////////

        // Form
        Form form = new Form("form");
        add(form);

        // Error
        FeedbackPanel feedbackPanel = new FeedbackPanel("error");
        feedbackPanel.setOutputMarkupId(true);
        add(feedbackPanel);

        // Name
        TextField<String> tfName = new TextField<>("name", new PropertyModel<>(contact, "name"));
        tfName.setRequired(JPAField.isRequired(Contact.class, "name"));
        tfName.add(StringValidator.lengthBetween(0, JPAField.getLength(Contact.class, "name")));
        form.add(tfName);

        // Mail
        EmailTextField tfMail = new EmailTextField("email", new PropertyModel<>(contact, "email"));
        tfMail.setRequired(JPAField.isRequired(Contact.class, "email"));
        tfMail.add(StringValidator.lengthBetween(0, JPAField.getLength(Contact.class, "email")));
        form.add(tfMail);

        // Message
        TextArea<String> taMessage = new TextArea<>("message", new PropertyModel<>(contact, "message"));
        taMessage.setRequired(JPAField.isRequired(Contact.class, "message"));
        taMessage.add(StringValidator.lengthBetween(0, JPAField.getLength(Contact.class, "message")));
        form.add(taMessage);

        // Submit link (updates feedback)
        form.add(new AjaxSubmitLink("send") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                getPopup().showMessage("Great! your message has been sent.", "Info", target);
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                target.add(feedbackPanel);
            }
        });
    }

    private class RoadToHeaven extends AjaxLink<Void> {
        private String msg;

        RoadToHeaven(String id, String msg) {
            super(id);
            this.msg = msg;
        }

        @Override
        public void onClick(AjaxRequestTarget target) {
            getPopup().showMessage(msg, "Information", target);
        }
    }

    @Override
    public String getPageTitle() {
        return "Contact";
    }


    @Override
    public MenuItem getAssociatedMeuItem() {
        return MenuItem.contact;
    }
}
