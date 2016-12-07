package coe.dronsys.main.pages.login;

import coe.dronsys.main.app.DronsysSession;
import coe.dronsys.main.app.User;
import coe.dronsys.main.pages.base.WPBase;
import coe.dronsys.main.pages.blog.WPBlogEntry;
import coe.dronsys.main.pages.home.WPHome;
import coe.wuti.jpa.DAO;
import coe.wuti.jpa.JPAField;
import coe.wuti.wicket.security.SecureForm;
import coe.wuti.var.RandomStrings;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.EmailTextField;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.validation.validator.StringValidator;


/**
 * User: jrubio
 */

public class WPLogin extends WPBase {
    private static final String PAGE_PARAMETER = "returnTo";

    public static enum ReturnTo {
        blog(WPBlogEntry.class);

        private Class<? extends WebPage> theClass;

        ReturnTo(Class<? extends WebPage> theClass) {
            this.theClass = theClass;
        }

        public Class<? extends WebPage> getTheClass() {
            return theClass;
        }
    }

    // For the mail and pwd
    String mailValue, passwdValue, captchaFieldValue;

    // For the captcha
    private Image image;
    private Challenge challenge = new Challenge();
    private int numberOfTrials = 1;
    private static final int CAPTCHA_APPEARS_AFTER_NR = 0;


    public WPLogin(PageParameters pageParameters) {
        if (pageParameters != null && pageParameters.get(PAGE_PARAMETER) != null && ReturnTo.valueOf(pageParameters.get(PAGE_PARAMETER).toString()) != null)
            init(ReturnTo.valueOf(pageParameters.get(PAGE_PARAMETER).toString()).getTheClass());
        else
            init (WPLogin.class) ;
    }

    public static PageParameters getPageParametersFor(ReturnTo returnTo) {
        return new PageParameters().add(PAGE_PARAMETER, returnTo.name());
    }

    public WPLogin() {
        init(WPHome.class);
    }


    private void init(Class<? extends WebPage> returnTo) {
        // Main form
        Form form = new SecureForm("form");
        add(form);

        // Errors
        FeedbackPanel feedbackPanel = new FeedbackPanel("error");
        feedbackPanel.setOutputMarkupId(true);
        add(feedbackPanel);

        // Mail
        EmailTextField tfMail = new EmailTextField("email", new PropertyModel<>(this, "mailValue"));
        tfMail.add(StringValidator.lengthBetween(0, JPAField.getLength(User.class, "email")));
        tfMail.setRequired(true);
        form.add(tfMail);

        // Passwd
        PasswordTextField tfPasswd = new PasswordTextField("password", new PropertyModel<>(this, "passwdValue"));
        tfPasswd.add(StringValidator.lengthBetween(0, JPAField.getLength(User.class, "passwd")));
        tfPasswd.setRequired(true);
        form.add(tfPasswd);
//
//        // The captcha image
//        image = new Image("captchaImage", new CaptchaImageResource(challenge));
//        image.setOutputMarkupId(true);
//        form.add(image);
//
//        // Reload captcha links
//        form.add(new AjaxLink<String>("refreshCaptcha") {
//            @Override
//            public void onClick(AjaxRequestTarget target) {
//                challenge.createNew();
//                image.setImageResource(new CaptchaImageResource(challenge));
//                target.add(image);
//            }
//        });
//
//        // Captcha textfield
//        RequiredTextField<String> captchaField = new RequiredTextField<String>("captchaText", new PropertyModel<>(this, "captchaFieldValue")) {
//            @Override
//            protected final void onComponentTag(final ComponentTag tag) {
//                super.onComponentTag(tag);
//                tag.put("value", ""); // Clear value after every use!
//            }
//        };
//        captchaField.setOutputMarkupId(true);
//        form.add(captchaField);

        // Submit link (updates feedback in case of error)
        form.add(new AjaxSubmitLink("send") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {

                // Captcha OK ?
//                numberOfTrials++;
//                if (numberOfTrials > CAPTCHA_APPEARS_AFTER_NR) {
//                    if (!challenge.ok(captchaFieldValue)) {
//                        challenge.createNew();
//                        throw new RestartResponseException(WPLogin.class);
//                    }
//                }

                // User OK?
                User user = searchUser();
                if (user != null) {
                    DronsysSession session = (DronsysSession) Session.get();
                    session.setAutenticated(true);
                    session.setUser(user);
                    throw new RestartResponseException(returnTo);
                } else
                    getPopup().showMessage("Incorrect email and/or password", "Error", target);
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                target.add(feedbackPanel);
            }
        });
    }

    private User searchUser() {
        DAO dao = DAO.get();
        return dao.getObject(User.class, "User.byMailandPasswd", "email", mailValue, "passwd", passwdValue);
    }

    @Override
    public String getPageTitle() {
        return "Login";
    }

    @Override
    public MenuItem getAssociatedMeuItem() {
        return MenuItem.login;
    }

    private class Challenge extends Model<String> {
        private static final int CAPTCHA_LENGTH = 5;

        private String generatedText;

        private boolean needNew = true;

        @Override
        public String getObject() {
            if (generatedText == null || needNew) generatedText = RandomStrings.get(CAPTCHA_LENGTH);
            needNew = false;
            return generatedText;
        }

        void createNew() {
            this.needNew = true;
        }

        boolean ok(String text) {
            return text != null && text.trim().equals(generatedText);
        }
    }
}
