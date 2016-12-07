package coe.lsm.servlets.map;

import coe.lsm.main.model.Service;
import coe.lsm.main.model.Status;
import coe.lsm.main.model.Team;
import coe.lsm.main.store.Index;
import coe.lsm.main.store.Spot;
import coe.lsm.main.store.Store;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;


/**
 * Created by jesus on 5/4/16.
 */
@WebServlet("/load")
public class SLoad extends HttpServlet {

    private static final int MIN_NUMBER_OF_MINUTES_SENT = 3;
    private static final int MAX_NUMBER_OF_MINUTES_SENT = 15;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // todo: validate session permissions!!
//        HttpSession session = request.getSession();

        try {
            int teamNumber = Integer.parseInt(request.getParameter("teamNumber"));
            int lastMinuteAvailable = Integer.parseInt(request.getParameter("lastMinuteAvailable"));
            load(response, teamNumber, lastMinuteAvailable);
        } catch (Throwable t) {
            response.getOutputStream().println("");
        }
    }


    /**
     * ***************************************
     * teamNumber = 0 means all teams !!
     * ***************************************
     */
    private void load(HttpServletResponse response, int requestedTeamNr, int requestedFromMinute) throws Exception {
//        long init = System.currentTimeMillis();

        Store store = Store.get();
        Index index = store.getCurrentIndex();

        int initMinute = requestedFromMinute - MIN_NUMBER_OF_MINUTES_SENT;
        if (initMinute < 0) initMinute = 0;

        int lastMinute = index.getLastMinute();
        if (initMinute > lastMinute) initMinute = lastMinute;

        boolean pendingData = lastMinute - initMinute > MAX_NUMBER_OF_MINUTES_SENT;
        if (pendingData) lastMinute = initMinute + MAX_NUMBER_OF_MINUTES_SENT;

        StringBuilder sb = new StringBuilder();

        if (requestedTeamNr != 0 && !Team.isATrueBlue(requestedTeamNr)) {
            sb.append("NO_OK: bad team number");
        } else {
            // Header
            sb.append("OK").append("H");
            sb.append(pendingData).append("H");
            sb.append(index.getOriginOfTimeInSeconds() * 1000L).append("H");
            sb.append(initMinute).append("H");
            sb.append(lastMinute).append("H");

            // Content for each team
            List<Team> teams = Team.getAllBlueTeams();
            Collections.sort(teams);

            for (Team team : teams) {
                if (requestedTeamNr != 0 && requestedTeamNr != team.getNumber()) continue;

                sb.append(team.getNumber()).append("$");
                sb.append(getActiveServices(store, index, team)).append("$");

                int teamPosition = index.getPositionForTeam(team);
                for (int minute = initMinute; minute <= lastMinute; minute++) {
                    for (Service service : index.getPresentServices()) {
                        int servicePosition = index.getPositionForService(service);
                        Spot spot = store.getSpot(teamPosition, servicePosition, minute);
                        if (spot != null)
                            appendColor(sb, spot).append("|");
                    }
                    sb.append("L");
                }
                sb.append("T");
            }

            // Services
            sb.append("H");
            for (Service service : index.getPresentServices())
                sb.append(service.getFull()).append("|");

            // Write results
            response.getOutputStream().print(sb.toString());
        }

//        System.out.println("--> Loading time : " + (System.currentTimeMillis() - init) + " milli secs");
    }

    private StringBuilder appendColor(StringBuilder sb, Spot spot) {
        if (spot.isMixedStatus())
            return sb.append(Integer.toHexString(spot.getColor().getRGB()).substring(2));
        else {
            Status status = spot.getLastStatus();
            if (status == Status.OK) return sb.append("");
            else return sb.append(spot.getLastStatus().getFirstLetter());
        }
    }

    private int getActiveServices(Store store, Index index, Team team) {
        int res = 0;

        int teamPosition = index.getPositionForTeam(team);
        for (Service service : index.getPresentServices()) {
            int servicePosition = index.getPositionForService(service);
            Spot spot = store.getSpot(teamPosition, servicePosition, index.getLastMinute() - 1);
            if (spot == null || spot.getLastStatus() == null || !spot.getLastStatus().equals(Status.CRITICAL)) res++;
        }

        return res;
    }
}


