package coe.dronsys.test.var;

import coe.dronsys.main.app.User;
import coe.dronsys.main.pages.base.WPBase;
import coe.wuti.jpa.DAO;
import coe.wuti.wicket.security.SecureForm;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;

import java.util.List;

/**
 * User: jrubio
 */
public class WPTestAdmin extends WPBase {

    User selectedUser;
    MainFormModel mainFormModel = new MainFormModel();

    public WPTestAdmin() {

        // The selection of the user
        Form userForm = new SecureForm("userForm");
        add(userForm);
        DropDownChoice<User> ddcUsers = new DropDownChoice<>("userSelection",
                new PropertyModel<>(this, "selectedUser"),
                new LoadableDetachableModel<List<User>>() {
                    @Override
                    protected List<User> load() {
                        return DAO.get().getList(User.class);
                    }
                },
                new ChoiceRenderer<User>() {
                    @Override
                    public Object getDisplayValue(User object) {
                        return object.getName();
                    }

                    @Override
                    public String getIdValue(User object, int index) {
                        return "" + object.getId(); // todo exposure of the id of the database
                    }

                    @Override
                    public User getObject(String id, IModel<? extends List<? extends User>> choices) {
                        if (choices != null)
                            for (User u : choices.getObject())
                                if (id.equals("" + u.getId())) return u;
                        return null;
                    }
                }
        );
        userForm.add(ddcUsers);

        // The user form //

        // Container of textFields (needed to respond to ajax update)
        WebMarkupContainer formContainer = new WebMarkupContainer("formContainer");
        formContainer.setOutputMarkupId(true);
        add(formContainer);

        // The user form
        SecureForm mainForm = new SecureForm<>("mainForm", new CompoundPropertyModel<>(mainFormModel));
        formContainer.add(mainForm);
        mainForm.setVisible(false);

        // Feedback
        FeedbackPanel feedback = new FeedbackPanel("error");
        feedback.setOutputMarkupId(true) ;
        mainForm.add(feedback);

        // Fields
        mainForm.add(new TextField<String>("name"));
        mainForm.add(new EmailTextField("email"));
        mainForm.add(new TextField<String>("creditCard"));

        // Update
        mainForm.add(new AjaxSubmitLink("update") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                target.add(feedback);
                getPopup().showMessage("Data have (not) been updated, just a test!", "Info", target);
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                target.add(feedback);
            }
        });

        mainForm.add(new AjaxLink("delete") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                getPopup().showMessage("User has (not) been deleted, just a test!", "Info", target);
            }
        });

        // Ajax update on user selection
        ddcUsers.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                if (selectedUser != null) {
                    mainFormModel.name = selectedUser.getName();
                    mainFormModel.email = selectedUser.getEmail();
                    mainFormModel.creditCard = selectedUser.getCreditCardNr();
                }
                mainForm.setVisible(selectedUser != null);
                target.add(formContainer);

            }


        });
    }

    private class MainFormModel {
        String name, email, creditCard;
    }

    @Override
    public String getPageTitle() {
        return "Administration";
    }


    @Override
    public MenuItem getAssociatedMeuItem() {
        return null;
    }
}
