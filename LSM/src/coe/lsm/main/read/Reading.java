package coe.lsm.main.read;

import coe.lsm.main.model.Status;

import java.util.logging.Logger;

/**
 * Created by jesus on 2/19/16.
 */
public class Reading {
    private int teamNumber;
    private String host;
    private String service;
    private Status status;
    private String msg;
    private int lastReadInSeconds; // seconds after jan 1st, 1970

    private static int count = 0;

    public Reading(int teamNumber, String host, String service, Status status, String msg, int lastReadInSeconds) {
        // Input
        this.teamNumber = teamNumber;
        this.host = host != null ? host : "";
        this.service = service;
        this.status = status;
        this.msg = msg;
        this.lastReadInSeconds = lastReadInSeconds;

//        count++;
//        if (count % 100000 == 0)
//            Logger.getLogger("Reading").info(" count = " + count);
    }

    public static Reading getFromRawData(String rawLine) {
        // Basic validation
        if (rawLine == null || rawLine.trim().length() == 0) {
            return null;
        }

        // Split raw line (and more validations)
        rawLine = rawLine.trim().replace("\"", "");
        String[] parts = rawLine.split("\\|");
        if (parts.length != 6) {
            Logger.getLogger("Reading").info("Raw line does not have right format: " + rawLine);
            return null;
        }

        // From raw to fields
        String host = parts[0];
        String service = parts[1];
        String msg = parts[3];

        // The team number
        int team;
        int length = parts[0].length();
        if (length < 9) {
            Logger.getLogger("Reading").info("Raw line has wrong host identification");
            return null;
        }
        try {
            team = Integer.parseInt(parts[0].substring(length - 5, length - 3));
        } catch (NumberFormatException e) {
            Logger.getLogger("Reading").info("Raw line has wrong team identification");
            return null;
        }

        // The status
        Status status;
        try {
            status = Status.valueOf(parts[2]);
        } catch (Throwable t) {
            Logger.getLogger("Reading").info("Raw line has unknown status: " + parts[2]);
            status = Status.UNKNOWN;
        }

        // The time
        int endTime;
        try {
            endTime = Integer.parseInt(parts[5]);
        } catch (NumberFormatException e) {
            Logger.getLogger("Reading").info("Raw line has wrong end time: " + parts[5]);
            return null;
        }

        // Return result
        return new Reading(team, host, service, status, msg, endTime);
    }

    public int getMinutesAfterOrigin(int originOfTimeInSeconds) {
        return (int) ((lastReadInSeconds - originOfTimeInSeconds) / 60.0);
    }


    public int getTeamNumber() {
        return teamNumber;
    }

    public String getHost() {
        return host;
    }

    public String getReducedHostName() {
        int blueIndex = this.host.indexOf("blue");
        if (blueIndex < 0)
            return "unknown host";
        else
            return this.host.substring(0, blueIndex - 1);
    }

    public String getService() {
        return service;
    }

    public Status getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public int getLastReadInSeconds() {
        return lastReadInSeconds;
    }
}
