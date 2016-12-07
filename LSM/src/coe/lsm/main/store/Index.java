package coe.lsm.main.store;

import coe.lsm.main.model.Service;
import coe.lsm.main.model.Team;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by jesus on 3/18/16.
 */
public class Index {
    private int lastMinute;
    private int originOfTimeInSeconds;

    private TreeMap<Team, Integer> teamsPositions = new TreeMap<>();   // team number -> position
    private TreeMap<Service, Integer> servicesPositions = new TreeMap<>(); // service  -> position

    public Index(int lastMinute, int originOfTimeInSeconds, TreeMap<Team, Integer> teamsPositions, TreeMap<Service, Integer> servicesPositions) {
        this.lastMinute = lastMinute;
        this.originOfTimeInSeconds = originOfTimeInSeconds;
        this.teamsPositions = teamsPositions;
        this.servicesPositions = servicesPositions;
    }

    public int getLastMinute() {
        return lastMinute;
    }

    public List<Service> getPresentServices() {
        return new ArrayList<>(servicesPositions.keySet());
    }

    public List<Team> getExistingTeams() {
        return new ArrayList<>(teamsPositions.keySet());
    }

    public int getPositionForTeam(Team team) {
        if (teamsPositions.containsKey(team))
            return teamsPositions.get(team);
        return -1;
    }

    public int getPositionForService(Service service) {
        return servicesPositions.get(service);
    }

    public int getNumberOfServices() {
        return servicesPositions.size();
    }

    public int getOriginOfTimeInSeconds() {
        return originOfTimeInSeconds;
    }

    public Date getDate(int minutesAfterOrigin) {
        return new Date((originOfTimeInSeconds + minutesAfterOrigin * 60) * 1000L);
    }
}
