package coe.lsm.servlets.chart;

import coe.lsm.gui.pages.main.chart.Chart;
import coe.lsm.main.model.Team;
import coe.lsm.main.store.Store;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * Created by jesus on 5/4/16.
 */
@WebServlet("/chart")
public class SChart extends HttpServlet {


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // todo: validate session permissions!!

        try {
            Store store = Store.get();
            Chart chart = new Chart(store, store.getCurrentIndex());
            int teamNumber = Integer.parseInt(request.getParameter("teamNumber"));
            response.getOutputStream().println(chart.getJSFor(Team.from(teamNumber)));
        } catch (Throwable t) {
            response.getOutputStream().println("");
        }
    }
}


