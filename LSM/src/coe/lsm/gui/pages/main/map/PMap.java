package coe.lsm.gui.pages.main.map;

import coe.lsm.gui.app.App;
import coe.lsm.main.model.Team;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.resource.PackageResourceReference;

/**
 * User: jrubio
 */
public class PMap extends Panel {
    private int teamNumber ;

    public PMap(String id, Team team) {
        super(id);
        this.teamNumber = team != null ? team.getNumber() : 0 ;
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.render(JavaScriptHeaderItem.forReference(new PackageResourceReference(this.getClass(), "PMap.js")));
        response.render(OnDomReadyHeaderItem.forScript("map('" + teamNumber + "');"));
    }
}
