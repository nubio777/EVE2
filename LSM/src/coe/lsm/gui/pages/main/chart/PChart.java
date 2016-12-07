package coe.lsm.gui.pages.main.chart;

import coe.lsm.gui.app.App;
import coe.lsm.main.model.Team;
import coe.lsm.main.store.Store;
import org.apache.wicket.ajax.AbstractAjaxTimerBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.util.time.Duration;

/**
 * User: jrubio
 */
public class PChart extends Panel {
    private Team team; // if team is null it means all teams !
    private Store store;

     public PChart(String id, Team team) {
        super(id);

        this.team = team;
        this.store = App.get().getStore();

        // The name of the team
        add(new Label("teamNumber", team != null ? "team " + team.getNumber() : "all teams"));

        // Update every minute
        add(new AbstractAjaxTimerBehavior(Duration.seconds(60.0)) {
            @Override
            protected void onTimer(AjaxRequestTarget target) {
                target.appendJavaScript(getJSNow());
            }
        });
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.render(OnDomReadyHeaderItem.forScript(getJSNow()));
    }

    private String getJSNow() {
        Chart chart = new Chart(store, store.getCurrentIndex());
        return "var chart=" + chart.getJSFor(team) + "; $('#chart').highcharts(chart);";
    }
}
