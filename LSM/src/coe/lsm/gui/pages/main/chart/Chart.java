package coe.lsm.gui.pages.main.chart;

import coe.lsm.main.Configuration;
import coe.lsm.main.model.Service;
import coe.lsm.main.model.Status;
import coe.lsm.main.model.Team;
import coe.lsm.main.read.FileReader;
import coe.lsm.main.read.Reading;
import coe.lsm.main.store.Index;
import coe.lsm.main.store.Spot;
import coe.lsm.main.store.Store;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by jesus on 2/25/16.
 * <p>
 * Generates JavaScript code for highcharts library
 */
public class Chart {

    private Store store;
    private Index index;

    public Chart(Store store, Index index) {
        this.store = store;
        this.index = index;
    }

    public String getJSForAllTeams() {
        return getJSFor(null);
    }

    // If team is null it means all teams!!
    public String getJSFor(Team team) {
        return
                "                    {\n" +
                        "                        chart: {\n" +
                        "                            backgroundColor: 'transparent'\n" +
                        "                        },\n" +
                        "                        title: {\n" +
                        "                            text: 'Percentage (%) of active services over time', x: -20, // center\n" +
                        "                            style: {\n" +
                        "                                color: '#2a8e8b',\n" +
                        "                                fontWeight: 'bold',\n" +
                        "                                fontSize: '22px'\n" +
                        "                            }\n" +
                        "                        },\n" +
                        "                        subtitle: {\n" +
                        "                            text: 'Time evolution', x: -20\n" +
                        "                        },\n" +
                        "                        xAxis: {\n" +
                        "                            type: 'datetime',\n" +
                        "                            dateTimeLabelFormats: {hour: '%H:%M'}\n" +
                        "                        },\n" +
                        "                        yAxis: {\n" +
                        "                            title: {\n" +
                        "                                text: '% of active services'\n" +
                        "                            },\n" +
                        "                            plotLines: [\n" +
                        "                                {\n" +
                        "                                    value: 0, width: 1, color: '#808080'\n" +
                        "                                }\n" +
                        "                            ]\n" +
                        "                        },\n" +
                        "                        tooltip: {\n" +
                        "                            valueSuffix: '%'\n" +
                        "                        },\n" +
                        "                        legend: {\n" +
                        "                            layout: 'vertical', align: 'right', verticalAlign: 'middle', borderWidth: 0\n" +
                        "                        },\n" +
                        "                        series: " + getData(team) + "\n" +
                        "                    }";
    }


    private String getData(Team team) {
        List<Team> teams = new ArrayList<>();

        if (team != null)
            teams.add(team);
        else
            teams.addAll(index.getExistingTeams());

        StringBuilder sb = new StringBuilder();
        sb.append("[\n");

        if (teams.size() > 0) {
            for (Team t : teams) sb.append(getDetail(t)).append(",");
            deleteComma(sb);
        }

        sb.append("\n]");
        return sb.toString();
    }

    private String getDetail(Team team) {
        StringBuilder sb = new StringBuilder();

        sb.append("          {\n");
        sb.append("             \"name\": \"Team ").append(team.getNumber()).append("\" ,\n");
        sb.append("             \"data\": [");


        int numberOfMinutes = index.getLastMinute();
        int numberOfValues = numberOfMinutes < 60 ? numberOfMinutes : 60;
        float interval = numberOfMinutes < 60 ? 1.0f : numberOfMinutes / (numberOfValues * 1.0f);

        for (int i = 0; i < numberOfValues; i++)
            evaluateMinute(sb, team, Math.round(i * interval));
//        evaluateMinute(sb, team, numberOfMinutes);

        deleteComma(sb);

        sb.append("]\n");
        sb.append("          }");

        return sb.toString();
    }

    private void evaluateMinute(StringBuilder sb, Team team, int minute) {
        long date = getMillisecondsAfterEpoch(index.getOriginOfTimeInSeconds(), minute);
        sb.append("[").append(date);
        sb.append(", ");
        int numberOfDown = countServices(team, Status.CRITICAL, minute);
        int total = index.getNumberOfServices();
        int percentage = 100 - Math.round((100.0f * numberOfDown) / total);
        sb.append(percentage).append("],");
    }

    private int countServices(Team team, Status status, int minutesAfterOrigin) {
        if (minutesAfterOrigin <= 0) return 0;
        if (minutesAfterOrigin > index.getLastMinute()) minutesAfterOrigin = index.getLastMinute();

        int res = 0;
        for (Service service : index.getPresentServices()) {
            Spot spot = store.getSpot(index.getPositionForTeam(team), index.getPositionForService(service), minutesAfterOrigin);
            if (spot == null) continue;
            Status last = spot.getLastStatus();
            if (last != null && last.equals(status)) res++;
        }

//        System.out.println("Team = " + team + " status = " + status + " res = " + res + " minutes = " + minutesAfterOrigin);

        return res;
    }


    private long getMillisecondsAfterEpoch(int originOfTimeInSeconds, int minutesAfterOriginOfTime) {
        return new Date(originOfTimeInSeconds * 1000L + minutesAfterOriginOfTime * 60 * 1000L).getTime();
    }

    private void deleteComma(StringBuilder sb) {
        int lastComma = sb.lastIndexOf(",");
        if (lastComma > 0)
            sb.deleteCharAt(sb.lastIndexOf(","));
    }

    public static void main(String[] args) throws Exception {
        Configuration configuration = new Configuration();
        Store store = new Store(configuration);
        FileReader fileReader = new FileReader(configuration);

        fileReader.readContinuous(new FileReader.Listener() {
            @Override
            public void newLine(Reading reading) {
                store.addReading(reading);
            }

            @Override
            public void error(String msg) {
                System.out.println("msg = " + msg);
            }

            @Override
            public void end(String msg) {
                System.out.println("msg = " + msg);
            }

            @Override
            public void idle(int seconds) {
                System.out.println("seconds idle = " + seconds);
            }
        }, 1);

        Thread.sleep(1000L);
        Chart chart = new Chart(store, store.getCurrentIndex());
        String res = chart.getJSFor(Team.from("01"));
//        String res = chart.getJSForAllTeams() ;
        System.out.println("res = " + res);
    }
}
