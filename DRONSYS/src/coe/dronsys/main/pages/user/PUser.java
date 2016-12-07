package coe.dronsys.main.pages.user;

import coe.dronsys.main.app.DronsysSession;
import coe.dronsys.main.app.User;
import coe.dronsys.main.var.SVG;
import coe.wuti.jpa.DAO;
import coe.wuti.jpa.Insert;
import coe.wuti.jpa.JPAField;
import coe.wuti.jpa.Update;
import coe.wuti.wicket.popup.Popup;
import coe.wuti.wicket.var.IndicatingAjaxSubmitLink;
import org.apache.commons.io.IOUtils;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.EmailTextField;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.image.NonCachingImage;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.resource.DynamicImageResource;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.validation.validator.StringValidator;

import java.util.List;

/**
 * Created by jesus on 2/1/16.
 */
public class PUser extends Panel {
    private User user;


    public PUser(String id, Popup popup) {
        super(id);

        // The session and the user
        DronsysSession session = (DronsysSession) Session.get();
        if (session.isAutenticated()) user = session.getUser();
        else user = new User();

        // Main form
        Form form = new Form("form");
        form.setMultiPart(true);
        add(form);

        // Errors
        FeedbackPanel feedbackPanel = new FeedbackPanel("error");
        feedbackPanel.setOutputMarkupId(true);
        add(feedbackPanel);

        // Name
        TextField<String> tfName = new TextField<>("name", new PropertyModel<>(user, "name"));
        tfName.setRequired(JPAField.isRequired(User.class, "name"));
        tfName.add(StringValidator.lengthBetween(0, JPAField.getLength(User.class, "name")));
        form.add(tfName);

        // Mail
        EmailTextField tfMail = new EmailTextField("email", new PropertyModel<>(user, "email"));
        tfMail.setEnabled(user.getId() == 0L);
        tfMail.setRequired(JPAField.isRequired(User.class, "email"));
        tfMail.add(StringValidator.lengthBetween(0, JPAField.getLength(User.class, "email")));
        form.add(tfMail);

        // Passwd
        PasswordTextField tfPasswd = new PasswordTextField("passwd", new PropertyModel<>(user, "passwd"));
        tfPasswd.setRequired(JPAField.isRequired(User.class, "passwd"));
        tfPasswd.add(StringValidator.lengthBetween(0, JPAField.getLength(User.class, "passwd")));
        form.add(tfPasswd);

        // Credit card
        TextField<String> tfCreditCard = new TextField<>("creditCard", new PropertyModel<>(user, "creditCardNr"));
        tfCreditCard.setRequired(JPAField.isRequired(User.class, "creditCardNr"));
        tfCreditCard.add(StringValidator.lengthBetween(0, JPAField.getLength(User.class, "creditCardNr")));
        form.add(tfCreditCard);

        // Picture
        Image userPicture = new NonCachingImage("userPicture", new AbstractReadOnlyModel<DynamicImageResource>() {
            @Override
            public DynamicImageResource getObject() {
                DynamicImageResource dir = new DynamicImageResource() {
                    @Override
                    protected byte[] getImageData(Attributes attributes) {
                        if (user != null && user.getPicture() != null)
                            return SVG.beautifyPicture(user.getPicture(), user.getName());
                        else
                            try {
                                PackageResourceReference reference = new PackageResourceReference(PUser.class, "defaultpic.png");
                                return reference.getResource() != null ? IOUtils.toByteArray(reference.getResource().getResourceStream().getInputStream()) : null;
                            } catch (Exception e) {
                                return null;
                            }
                    }
                };
                dir.setFormat(user.getPictureFormat() == null ? "" : user.getPictureFormat());
                return dir;
            }
        });
        userPicture.setOutputMarkupId(true);
        form.add(userPicture);

        // Picture upload
        FileUploadField fileUploadField = new FileUploadField("pictureUpload");
        form.add(fileUploadField);


        // Submit link
        form.add(new IndicatingAjaxSubmitLink("send") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                // Clean feedback
                target.add(feedbackPanel);

                // Chceck email is not already in database (in case new user)
                if (user.getId() == 0L) {
                    List<User> users = DAO.get().getList(User.class, "User.byMail", "email", user.getEmail());
                    if (users != null && users.size() > 0) {
                        popup.showMessage("The email has been already used", "Information", target);
                        return ;
                    }
                }

                // Handle image (rest of fields are handled automatically by their associated components)
                final FileUpload uploadedFile = fileUploadField.getFileUpload();
                if (uploadedFile != null) {
                    try {
                        user.setPicture(uploadedFile.getBytes());
                        user.setPictureType(uploadedFile.getContentType());
                        target.add(userPicture);
                    } catch (Exception ex) {
                        popup.showMessage("Could not handle picture: " + ex.getMessage(), "Error", target);
                        return;
                    }
                }

                // Modify database contents and update session if needed.
                try {
                    DAO dao = DAO.get();
                    if (session.isAutenticated())
                        dao.execute(new Update(user));
                    else {
                        dao.execute(new Insert(user));
                        ((DronsysSession) getSession()).setAutenticated(true);
                        ((DronsysSession) getSession()).setUser(user);
                    }

                    popup.showMessage("Thank you for the information.<br>All your data have been saved in our highly secure servers.", "Info", target);
                } catch (Exception e) {
                    popup.showMessage("Could not update database " + e.getMessage(), "Error", target);
                }
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                target.add(feedbackPanel);
            }
        });
    }
}