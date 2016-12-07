package coe.lsm.main;

import coe.lsm.main.model.Team;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.util.logging.Logger.getLogger;

/**
 * Created by jesus on 2/18/16.
 */
public class Configuration {
    private static final String CONFIGURATION_FILE = "lsm.properties";

    private File dataSource;
    private int readEverySeconds;

    private Date startingTime;
    private Date endTime;

    private int maxNumberOfTeams, maxNumberOfServices;

    private int pixelsPerMinute;
    private int pixelsPerService;

    private TreeMap<String, String> teamsNamesAndPwds = new TreeMap<>();

    private List<Team> allConfiguredTeams;

    private static final Configuration configuration = new Configuration() ;

    public static Configuration get() {
        return configuration ;
    }

    private Configuration() {
        // Load properties
        URL fileURL = Configuration.class.getClassLoader().getResource(CONFIGURATION_FILE);
        if (fileURL == null)
            throw new RuntimeException("Configuration file not found");

        // Read the properties
        try {
            InputStream input = new FileInputStream(new File(URLDecoder.decode(fileURL.getPath(), "UTF-8")));
            Properties properties = new Properties();
            properties.load(input);
            input.close();

            dataSource = new File(properties.getProperty("DATA_SOURCE"));
            readEverySeconds = Integer.parseInt(properties.getProperty("READ_EVERY_SECONDS"));

            String start = properties.getProperty("STARTT_TIME_yyyy.MMMM.dd.hh.mm");
            startingTime = new SimpleDateFormat("yyyy.MMMM.dd.hh.mm", Locale.ENGLISH).parse(start);
            String end = properties.getProperty("ENDING_TIME_yyyy.MMMM.dd.hh.mm");
            endTime = new SimpleDateFormat("yyyy.MMMM.dd.hh.mm", Locale.ENGLISH).parse(end);

            maxNumberOfTeams = Integer.parseInt(properties.getProperty("MAX_NUMBER_OF_TEAMS"));
            maxNumberOfServices = Integer.parseInt(properties.getProperty("MAX_NUMBER_OF_SERVICES"));
            pixelsPerMinute = Integer.parseInt(properties.getProperty("NUMBER_OF_PIXELS_PER_MINUTE"));
            pixelsPerService = Integer.parseInt(properties.getProperty("NUMBER_OF_PIXELS_PER_SERVICE"));

            Enumeration props = properties.propertyNames();
            while (props.hasMoreElements()) {
                String prop = (String) props.nextElement();
                prop = prop.toLowerCase();
                if (prop.startsWith("team")) teamsNamesAndPwds.put(prop, properties.getProperty(prop));
            }

        } catch (Exception e) {
            getLogger("Configuration").severe(e.getMessage());
            throw new RuntimeException("Bad configuration file: " + e.getMessage());
        }
    }

    public File getDataSource() {
        return dataSource;
    }

    public int getMaxNumberOfTeams() {
        return maxNumberOfTeams;
    }

    public int getMaxNumberOfServices() {
        return maxNumberOfServices;
    }

    public int getReadEverySeconds() {
        return readEverySeconds;
    }

    public Date getStartingTime() {
        return startingTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public int getPixelsPerMinute() {
        return pixelsPerMinute;
    }

    public int getPixelsPerService() {
        return pixelsPerService;
    }

    public Team teamFor(String team, String pwd) {
        if (team == null || pwd == null) return null;
        team = team.trim().toLowerCase();
        pwd = pwd.trim();

        String teamName = "team";
        if (team.length() == 1)
            teamName = teamName + "0" + team;
        else
            teamName += team;

        String realPwd = teamsNamesAndPwds.get(teamName);
        if (realPwd == null || !realPwd.equals(pwd)) return null;

        return Team.from(teamName);
    }

    public List<Team> getAllConfiguredTeams() {
        if (allConfiguredTeams == null) {
            allConfiguredTeams = new ArrayList<>();
            for (String teamName : teamsNamesAndPwds.keySet()) {
                Team team = Team.from(teamName);
                if (team != null && !allConfiguredTeams.contains(team)) allConfiguredTeams.add(team);
            }
        }

        return new ArrayList<>(allConfiguredTeams);
    }

    public List<Team> getConfiguredBlueTeams() {
        List<Team> teams = new ArrayList<>();
        for (Team t : getAllConfiguredTeams())
            if (t != null && t.isBlue()) teams.add(t);
        return teams;
    }

    public static void main(String[] args) {
        List<Team> teams = new Configuration().getAllConfiguredTeams();
        System.out.println("teams = " + teams);
    }
}
