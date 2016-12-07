
package coe.dronsys.main.pages.help;

import coe.dronsys.main.pages.base.WPBase;
import coe.dronsys.test.zOldStuff.webservices.HelpWebService;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * User: jrubio
 */
public class WPHelp extends WPBase {

    private String searchTerm;
    private String searchResults;

    public WPHelp() {
        this(null);
    }

    public WPHelp(PageParameters pageParameters) {

        if (pageParameters != null && !pageParameters.isEmpty()) {
            searchTerm = pageParameters.get("search").toString();

            HelpWebService helpWebService = new HelpWebService() ;
            searchResults = helpWebService.getHelpText(searchTerm) ;
        }

        // The label with the results
        add(new Label("results", new Model<String>() {
            @Override
            public String getObject() {
                if (searchTerm == null)
                    return "";
                else if (searchResults == null || searchResults.trim().length() == 0)
                    return "Sorry. Currently we do not have information about '" + searchTerm.trim() + "'";
                else
                    return searchResults.trim();
            }
        }).setEscapeModelStrings(false));
    }

    @Override
    public String getPageTitle() {
        return "Help";
    }

    @Override
    public MenuItem getAssociatedMeuItem() {
        return null;
    }
}
