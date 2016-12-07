package coe.dronsys.main.pages.blog;

import coe.dronsys.main.pages.base.WPBase;
import coe.dronsys.main.pages.blog.model.Comment;
import coe.dronsys.main.pages.blog.model.Entry;
import coe.dronsys.main.pages.login.WPLogin;
import coe.wuti.jpa.DAO;
import coe.wuti.jpa.Insert;
import coe.wuti.jpa.JPAField;
import coe.wuti.wicket.security.SecureForm;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.validator.StringValidator;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * User: jrubio
 */
public class WPBlogEntry extends WPBase {
    private Entry entry;
    String newComment;

    public WPBlogEntry() {
        // todo some extra work needed
        entry = DAO.get().getList(Entry.class).get(0);

        // Blog head
        add(new Label("mainTitle", entry.getTitle()));
        add(new Label("subTitle", entry.getSubTitle()));

        // Blog contain
        add(new Label("mainText", entry.getText()));

        // Blog author
        add(new Label("author", entry.getAuthor().getName()));
        add(new Label("aboutAuthor", entry.getAuthor().getAbout()));


        // Comments
        WebMarkupContainer commentsContainer = new WebMarkupContainer("listContainer");
        commentsContainer.setOutputMarkupId(true);
        commentsContainer.add(getListViewComments());
        add(commentsContainer);

        // Login to comment
        WebMarkupContainer loginToComment = new WebMarkupContainer("logInToComment");
        loginToComment.add(new BookmarkablePageLink("login", WPLogin.class, WPLogin.getPageParametersFor(WPLogin.ReturnTo.blog)));
        loginToComment.setVisible(!getDronsysSession().isAutenticated());
        add(loginToComment);

        // New comment form
        Form form = new SecureForm("form");
        form.setVisible(getDronsysSession().isAutenticated());
        form.setOutputMarkupId(true);
        add(form);

        TextArea<String> textArea = new TextArea<>("newComment", new PropertyModel<>(this, "newComment"));
        textArea.setRequired(true);
        textArea.add(StringValidator.lengthBetween(1, JPAField.getLength(Comment.class, "text")));
        form.add(textArea);

        // Errors
        FeedbackPanel feedbackPanel = new FeedbackPanel("error");
        feedbackPanel.setOutputMarkupId(true);
        add(feedbackPanel);

        form.add(new AjaxSubmitLink("submit") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                Comment comment = new Comment(entry, getDronsysSession().getUser(), newComment, new Date());
                try {
                    DAO.get().execute(new Insert(comment));
                    newComment = "";
                    target.add(form);
                    target.add(commentsContainer);
                } catch (Exception e) {
                    getPopup().showMessage("Error saving your comment: ", "Information", target);
                }
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                if (newComment == null || newComment.trim().length() == 0)
                    getPopup().showMessage("Please, write a comment", "Info", target);
                else
                    target.add(feedbackPanel);
            }
        });
    }

    private ListView<Comment> getListViewComments() {
        LoadableDetachableModel<List<Comment>> commentsModel = new LoadableDetachableModel<List<Comment>>() {
            @Override
            public List<Comment> load() {
                return DAO.get().getList(Comment.class, "Comment.byEntry", "entry", entry);
            }
        };

        return new ListView<Comment>("comments", commentsModel) {
            @Override
            protected void populateItem(ListItem<Comment> listItem) {
                Comment comment = listItem.getModelObject();
                listItem.add(new Label("commentAuthor", comment.getUser().getName()));
                listItem.add(new Label("when", when(comment)));
                listItem.add(new Label("commentText", comment.getText()));
            }
        };
    }

    private String when(Comment comment) {
        Date now = new Date();
        Date when = comment.getWhen();

        long differenceInSeconds = Math.abs(now.getTime() - when.getTime()) / 1000;

        if (differenceInSeconds < 5)
            return "right now";
        else if (differenceInSeconds < 60)
            return differenceInSeconds + " seconds ago";
        else if (differenceInSeconds < 60 * 2)
            return "1 minute ago";
        else if (differenceInSeconds < 60 * 60)
            return differenceInSeconds / 60 + " minutes ago";
        else if (differenceInSeconds < 60 * 60 * 24)
            return differenceInSeconds / 60 / 60 + " hours ago";
        return
                new SimpleDateFormat("yyyy-MMM-dd hh:mm").format(when);
    }

    @Override
    public String getPageTitle() {
        return "Blog";
    }

    @Override
    public MenuItem getAssociatedMeuItem() {
        return MenuItem.blog;
    }
}
