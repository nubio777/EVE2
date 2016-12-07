package coe.lsm.gui.pages.main.current;

import coe.lsm.gui.app.App;
import coe.lsm.main.analysis.Analysis;
import coe.lsm.main.analysis.HostInfo;
import coe.lsm.main.analysis.ServiceInfo;
import coe.lsm.main.model.Status;
import coe.lsm.main.model.Team;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AbstractAjaxTimerBehavior;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.time.Duration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;

/**
 * User: jrubio
 */
public class PCurrent extends Panel {
    private TreeMap<Team, Analysis> analysisMap = new TreeMap<>();
    private Status selectedStatus;

    private WebMarkupContainer wmcStatuses;
    private WebMarkupContainer wmcDetails;

    private boolean onlyOneTeam;

    // team = null means all teams !
    public PCurrent(String id, Team team) {
        super(id);

        this.onlyOneTeam = team != null;

        // The analysis
        analyze(team);

        // The name of the team
        add(new Label("teamNumber", team != null ? "team " + team.getNumber() : "all teams"));

        // The statuses
        wmcStatuses = new WebMarkupContainer("statuses");
        wmcStatuses.setOutputMarkupId(true);
        add(wmcStatuses);

        selectedStatus = App.get().getLSMSession().getLastSelectedStatus();

        ListView<Status> lvStatuses = new ListView<Status>("values", Arrays.asList(Status.values())) {
            @Override
            protected void populateItem(ListItem<Status> item) {
                Status status = item.getModelObject();

                Button button = new Button("value");
                button.setVisible(isButtonVisible(status));
                item.add(button);

                button.add(new AttributeModifier("class", "btn btn-" + status.getCssBoots()));
                button.add(new Label("number", new NumberOfServicesInStatusModel(status)));
                button.add(new Label("name", status.name()));

                button.add(new AjaxEventBehavior("click") {
                    @Override
                    protected void onEvent(AjaxRequestTarget target) {
                        selectedStatus = status;
                        App.get().getLSMSession().setLastSelectedStatus(status);
                        target.add(wmcDetails);
                    }
                });
            }
        };
        wmcStatuses.add(lvStatuses);

        // The details
        wmcDetails = new WebMarkupContainer("details");
        wmcDetails.setOutputMarkupId(true);
        add(wmcDetails);

        ListView<String> lvHosts = new ListView<String>("detail", new HostsModel()) {
            @Override
            protected void populateItem(ListItem<String> item) {
                String host = item.getModelObject();

                item.add(new Label("host", host));
                if (onlyOneTeam)
                    item.add(new FServicesOneTeam("services", host));
                else
                    item.add(new FServicesAllTeams("services", host));

            }
        };
        wmcDetails.add(lvHosts);

        // Update every minute
        add(new AbstractAjaxTimerBehavior(Duration.seconds(60.0)) {
            @Override
            protected void onTimer(AjaxRequestTarget target) {
                analyze(team);
                target.add(wmcStatuses);
                target.add(wmcDetails);
            }
        });
    }

    private void analyze(Team team) {
        analysisMap.clear();

        if (onlyOneTeam)
            analysisMap.put(team, new Analysis(App.get().getStore(), team));
        else {
            List<Team> teams = App.get().getConfiguration().getConfiguredBlueTeams();
            for (Team t : teams)
                analysisMap.put(t, new Analysis(App.get().getStore(), t));
        }
    }

    private boolean isButtonVisible(Status status) {
        if (onlyOneTeam)
            return analysisMap.get(analysisMap.firstKey()).getNumberOfServicesInStatus(status) > 0;
        else {
            for (Team t : analysisMap.keySet())
                if (analysisMap.get(t).getNumberOfServicesInStatus(status) > 0) return true;
        }

        return false;
    }

    private class NumberOfServicesInStatusModel extends LoadableDetachableModel<Integer> {
        private Status status;

        private NumberOfServicesInStatusModel(Status status) {
            this.status = status;
        }

        @Override
        protected Integer load() {
            int res = 0;
            if (onlyOneTeam)
                res = analysisMap.get(analysisMap.firstKey()).getNumberOfServicesInStatus(status);
            else {
                for (Team t : analysisMap.keySet())
                    res += analysisMap.get(t).getNumberOfServicesInStatus(status);
            }

//            System.out.println("res = " + res);
            return res;
        }
    }

    private class HostsModel extends LoadableDetachableModel<List<String>> {
        @Override
        protected List<String> load() {
            List<String> res = new ArrayList<>();
            for (Team t : analysisMap.keySet())
                for (HostInfo hostInfo : analysisMap.get(t).filter(selectedStatus))
                    if (!res.contains(hostInfo.getHostName())) res.add(hostInfo.getHostName());


            return res;
        }
    }

    private class FServicesOneTeam extends Fragment {
        FServicesOneTeam(String id, String host) {
            super(id, "fServicesOneTeam", PCurrent.this);
            Analysis analysis = analysisMap.get(analysisMap.firstKey());
            HostInfo hostInfo = analysis.getInfoOf(host);
            add(new ListView<ServiceInfo>("serviceElement", hostInfo != null ? hostInfo.getServicesMatching(selectedStatus) : new ArrayList<>()) {
                @Override
                protected void populateItem(ListItem<ServiceInfo> item) {
                    item.add(new ServiceLabel("service", item.getModelObject()));
                }
            });

        }
    }

    private class FServicesAllTeams extends Fragment {
        FServicesAllTeams(String id, String host) {
            super(id, "fServicesAllTeams", PCurrent.this);
            List<Team> teams = new ArrayList<>();
            for (Team t : analysisMap.keySet()) {
                HostInfo hostInfo = analysisMap.get(t).getInfoOf(host);
                if (hostInfo != null && hostInfo.getNumberOfServicesInStatus(selectedStatus) > 0) teams.add(t);
            }

            add(new ListView<Team>("teamLine", teams) {
                @Override
                protected void populateItem(ListItem<Team> item) {
                    Team team = item.getModelObject();
                    item.add(new Label("blueTeam", "Team " + team.getNumber()));

                    HostInfo hostInfo = analysisMap.get(team).getInfoOf(host);
                    item.add(new ListView<ServiceInfo>("serviceElement", hostInfo != null ? hostInfo.getServiceInfos() : new ArrayList<>()) {
                        @Override
                        protected void populateItem(ListItem<ServiceInfo> item) {
                            item.add(new ServiceLabel("service", item.getModelObject()));
                        }
                    });
                }
            });
        }
    }

    private class ServiceLabel extends Label {

        private ServiceLabel(String id, ServiceInfo serviceInfo) {
            super(id, new Model());
            String since = format(serviceInfo.getNumberOfMinutesInStatus(serviceInfo.getLastStatus()));
            setDefaultModelObject(serviceInfo.getService().getName() + " (" + since + ")");
            add(new AttributeModifier("class", "label label-" + selectedStatus.getCssBoots()));

            String tooltip = selectedStatus.name() + " since " + since + " ago";
            if (!selectedStatus.equals(Status.OK)) tooltip += ".\nLast info available: " + serviceInfo.getLastMsg();
            add(new AttributeModifier("title", tooltip));
        }

        private String format(int minutes) {
            if (minutes < 60)
                return minutes + "'";
            else if (minutes < 60 * 24) {
                int hours = minutes / 60;
                int res = minutes - hours * 60;
                return hours + "h " + res + "'";
            } else
                return "days";
        }
    }
}
