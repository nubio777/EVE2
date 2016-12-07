package coe.lsm.main.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by jesus on 3/29/16.
 */
public class Team implements Comparable<Team>, Serializable {
    private int number;

    public static final Team ALL = new Team(-1);
    public static final Team GREEN = new Team(0);
    public static final Team WHITE = new Team(0);
    public static final Team RED = new Team(0);

    private static final TreeMap<Integer, Team> blueTeams = new TreeMap<>();

    public static Team from(String name) {
        if (name == null) return null;

        name = name.trim().toLowerCase();
        if (name.contains("green")) return GREEN;
        if (name.contains("red")) return RED;
        if (name.contains("white")) return WHITE;

        if (name.startsWith("blue")) name = name.substring(4, name.length());
        if (name.startsWith("team")) name = name.substring(4, name.length());

        try {
            int number = Integer.parseInt(name);
            return from(number);
        } catch (Exception e) {
            return null;
        }
    }

    public static Team from(int number) {
        if (number == ALL.number) return ALL;
        if (number == GREEN.number) return GREEN;
        if (number == RED.number) return RED;
        if (number == WHITE.number) return WHITE;

        if (blueTeams.containsKey(number)) return blueTeams.get(number);
        Team team = new Team(number);
        blueTeams.put(number, team);
        return team;
    }

    private Team(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public String getFullName() {

        if (this == GREEN) return "green";
        if (this == WHITE) return "white";
        if (this == RED) return "red";

        if ((number < 10)) return "blue0" + number;
        return "blue" + number;
    }

    public boolean isBlue() {
        return number != 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Team team = (Team) o;

        if (number != team.number) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return number;
    }

    @Override
    public int compareTo(Team o) {
        if (o == null) return 1;
        return this.number - o.number;
    }

    public static void main(String[] args) {
        Team team = Team.from("01");
        System.out.println("team = " + team);
    }

    public static boolean isATrueBlue(int teamNr) {
        return blueTeams.containsKey(teamNr);
    }

    public static List<Team> getAllBlueTeams() {
        return new ArrayList<>(blueTeams.values());
    }
}

