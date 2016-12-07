package coe.lsm.gui.pages.main;

import coe.lsm.gui.app.App;
import coe.lsm.gui.app.LSMSession;
import coe.lsm.gui.pages.base.WPBase;
import coe.lsm.gui.pages.login.WPLogin;
import coe.lsm.gui.pages.main.chart.PChart;
import coe.lsm.gui.pages.main.current.PCurrent;
import coe.lsm.gui.pages.main.map.PMap;
import coe.lsm.main.model.Team;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * User: jrubio
 */
public class WPMain extends WPBase {
    public static enum View {
        Map("Full history") ; // , Chart("Time evolution"), Current("Current state");

        private String name;

        View(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    private LSMSession lsmSession = App.get().getLSMSession();

    private static final Team teamEquivalentToAll = Team.from(0);

    View selectedView;
    Team selectedTeam;

    public WPMain(PageParameters pageParameters) {

        // Authentication control
        if (!lsmSession.isAuthenticated()) {

            if (App.get().isDevelopment()) {
                lsmSession.setAuthenticated(true);
                lsmSession.setLoginTeam(Team.GREEN);
            } else {
                setResponsePage(WPLogin.class);
                return;
            }
        }

        // Team number and logout
        add(new Label("teamDescription", lsmSession.getLoginTeam().getFullName()));
        add(new Link("logout") {
            @Override
            public void onClick() {
                lsmSession.setAuthenticated(false);
                lsmSession.setLoginTeam(null);
                WPMain.this.setResponsePage(WPLogin.class);
            }
        });

        // Parameters
        if (pageParameters == null)
            setDefaults();
        else
            try {
                View view = View.valueOf(pageParameters.get("view").toString());
                selectedView = view != null ? view : View.Map;

                if (!lsmSession.getLoginTeam().isBlue()) {
                    int teamNr = Integer.parseInt(pageParameters.get("team").toString());
                    Team team = Team.from(teamNr);
                    if (team != null && App.get().getConfiguration().getAllConfiguredTeams().contains(team))
                        selectedTeam = team;
                    else
                        selectedTeam = teamEquivalentToAll;
                }
            } catch (Throwable t) {
                setDefaults();
            }

        // Form
        Form form = new Form("form") {
            @Override
            protected void onSubmit() {

                System.out.println("after onsubmit selectedView = " + selectedView);
                System.out.println("after onsubmit selectedTeam = " + selectedTeam);

                PageParameters pageParameters = new PageParameters();
                if (selectedView != null)
                    pageParameters.add("view", selectedView.name());
                if (selectedTeam != null)
                    pageParameters.add("team", selectedTeam.getNumber());
//                throw new RedirectToUrlException("/main.jsp?type=" + selectedView.name() + "&team=" + selectedTeam.getNumber()) ;
                setResponsePage(new WPMain(pageParameters));
            }

//            @Override
//            protected boolean encodeUrlInHiddenFields() {
//                return false;
//            }

        };
        add(form);

        // View
        DropDownChoice<View> viewChoice = new DropDownChoice<>("view", new PropertyModel<>(this, "selectedView"), Arrays.asList(View.values()));
        form.add(viewChoice);

        // Team chooser
        DropDownChoice<Team> teamChoice = new DropDownChoice<>("team", new PropertyModel<>(this, "selectedTeam"), new TeamsModel(), new TeamRenderer());
        teamChoice.setVisible(!lsmSession.getLoginTeam().isBlue());
        form.add(teamChoice);

        // Main contents
        add(getPanel("mainPanel"));
    }

    private void setDefaults() {
        if (selectedView == null) selectedView = View.Map;
        selectedTeam = lsmSession.getLoginTeam().isBlue() ? lsmSession.getLoginTeam() : teamEquivalentToAll;
    }

    private Panel getPanel(String id) {

        Team teamParameter = selectedTeam == teamEquivalentToAll ? null : selectedTeam;

        if (selectedView == View.Map)
            return new PMap("mainPanel", teamParameter);

//        else if (selectedView == View.Current)
//            return new PCurrent("mainPanel", teamParameter);
//
//        else if (selectedView == View.Chart)
//            return new PChart("mainPanel", teamParameter);

        else
            return new EmptyPanel(id);

    }


    private class TeamsModel extends LoadableDetachableModel<List<Team>> {
        @Override
        protected List<Team> load() {
            List<Team> teams = new ArrayList<>(App.get().getConfiguration().getConfiguredBlueTeams());
            teams.add(teamEquivalentToAll);
            Collections.sort(teams);
            return teams;
        }
    }

    private class TeamRenderer implements IChoiceRenderer<Team> {
        @Override
        public Object getDisplayValue(Team object) {
            if (object == teamEquivalentToAll) return "All teams";
            return "" + object.getNumber();
        }

        @Override
        public String getIdValue(Team object, int index) {
            return "" + object.getNumber();
        }

        @Override
        public Team getObject(String id, IModel<? extends List<? extends Team>> choices) {
            return Team.from(id);
        }
    }
}
