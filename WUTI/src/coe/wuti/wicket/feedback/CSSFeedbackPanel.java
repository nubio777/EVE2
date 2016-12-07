package coe.wuti.wicket.feedback;

import org.apache.wicket.Component;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.feedback.IFeedbackMessageFilter;
import org.apache.wicket.markup.html.panel.FeedbackPanel;

/**
* Created by jesus on 1/13/16.
*/
class CSSFeedbackPanel extends FeedbackPanel {

    public CSSFeedbackPanel(final String id, final IFeedbackMessageFilter filter) {
        super(id, filter);
    }

    public CSSFeedbackPanel(final String id) {
        super(id);
    }

    @Override
    protected Component newMessageDisplayComponent(final String id, final FeedbackMessage message) {
        final Component newMessageDisplayComponent = super.newMessageDisplayComponent(id, message);

       /*
        * CSS class resulting: feedbackUNDEFINED feedbackDEBUG feedbackINFO
                * feedbackWARNING feedbackERROR feedbackFATAL
        */
//            newMessageDisplayComponent
//                    .add(new AttributeAppender("class", new Model<String>(
//                            "feedback" + message.getLevelAsString()), " "));
        return newMessageDisplayComponent;
    }
}
