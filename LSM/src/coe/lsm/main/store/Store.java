package coe.lsm.main.store;

import coe.lsm.main.Configuration;
import coe.lsm.main.model.Service;
import coe.lsm.main.model.Team;
import coe.lsm.main.read.Reading;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import static java.util.logging.Logger.getLogger;

/**
 * Created by jesus on 3/18/16.
 */
public class Store {
    private Spot[][][] data;
    private TreeMap<Service, Integer> servicesPositions = new TreeMap<>(); // full service name -> index in the data
    private TreeMap<Team, Integer> teamsPositions = new TreeMap<>();   // team number -> index in the data

    private int originOfTimeInSeconds;
    private int maxNumberOfMinutes;
    private int lastMinuteSoFar = 0;

    private int maxNumberOfTeams, maxNumberOfServices;

    private static final Store store = new Store (Configuration.get());

    public static Store get() {
        return store ;
    }

    private Store(Configuration configuration) {

        originOfTimeInSeconds = (int) (configuration.getStartingTime().getTime() / 1000L);
        int endOfTimeInSeconds = (int) (configuration.getEndTime().getTime() / 1000L);
        maxNumberOfMinutes = (endOfTimeInSeconds - originOfTimeInSeconds) / 60 + 1;

        maxNumberOfTeams = configuration.getMaxNumberOfTeams();
        maxNumberOfServices = configuration.getMaxNumberOfServices();

        data = new Spot[maxNumberOfTeams][maxNumberOfServices][maxNumberOfMinutes];
        for (int i = 0; i < maxNumberOfTeams; i++)
            for (int j = 0; j < maxNumberOfServices; j++)
                for (int k = 0; k < maxNumberOfMinutes; k++)
                    data[i][j][k] = new Spot();

    }

    public void addReading(Reading reading) {
        if (reading == null)
            return;

        int minutesAfter = reading.getMinutesAfterOrigin(originOfTimeInSeconds);
        if (originOfTimeInSeconds > reading.getLastReadInSeconds())
            return;

        if (minutesAfter > lastMinuteSoFar)
            lastMinuteSoFar = minutesAfter;


        Team team = Team.from(reading.getTeamNumber());
        Integer teamPosition = teamsPositions.get(team);
        if (teamPosition == null) {
            teamPosition = teamsPositions.size();
            if (teamPosition >= maxNumberOfTeams) {
                getLogger("Store").info("Team '" + team + "' rejected (max number of teams reached)");
                return;
            }
            teamsPositions.put(team, teamPosition);
        }

        Service service = Service.from(reading.getReducedHostName(), reading.getService()) ;
        Integer servicePosition = servicesPositions.get(service);
        if (servicePosition == null) {
            servicePosition = servicesPositions.size();
            if (servicePosition >= maxNumberOfServices) {
                for (Service s : servicesPositions.keySet())
                    System.out.println(s.getFull());
//                getLogger("Store").info("Service '" + service + "' rejected (max number of services reached)");
                return;
            }
            servicesPositions.put(service, servicePosition);
        }

        if (minutesAfter >= maxNumberOfMinutes) {
            getLogger("Store").severe("Service '" + service + "' rejected (max number of minutes reached)");
        }

        data[teamPosition][servicePosition][minutesAfter].append(reading.getStatus(), reading.getMsg());
    }

    public Index getCurrentIndex() {
        return new Index(lastMinuteSoFar, originOfTimeInSeconds, new TreeMap<>(teamsPositions), new TreeMap<>(servicesPositions));
    }

    public Spot getSpot(int teamPosition, int servicePosition, int minute) {
        if (teamPosition < 0 || servicePosition < 0 || minute < 0) return null;
        if (teamPosition > maxNumberOfTeams || servicePosition > maxNumberOfServices || minute > maxNumberOfMinutes) return new Spot();
        return data[teamPosition][servicePosition][minute];
    }
 }
