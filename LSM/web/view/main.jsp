<%@ page import="coe.lsm.gui.pages.main.WPMain" %>
<%@ page import="coe.lsm.main.model.Team" %>

<%
    //    boolean development = WebXML.<Boolean>readParameter("development");
    boolean development = true;

    // Selected views and teams
    int teamNumber = 0;
    WPMain.View view = null;

    try {
        String pView = request.getParameter("view");
        String pTeam = request.getParameter("team");

        teamNumber = Integer.parseInt(pTeam);
        view = WPMain.View.valueOf(pView);

        if (view == null || teamNumber < 0 || teamNumber > 20) throw new Exception();

    } catch (Throwable t) {
        if (development) {
            teamNumber = 0;
            view = WPMain.View.Map;
        } else {
            response.sendRedirect("SLogout");
        }
    }

    // Allow selection of "all teams"
    boolean isPossibleToSelectAllTeams = development ? true : false;
%>

<!DOCTYPE html>

<html class="no-js" lang="en">

<head dir="ltr" lang="en">

    <!-- Metas
   ============================================= -->

    <meta charset="UTF-8">
    <meta name="robots" content="noindex, nofollow">
    <meta name="viewport" content="width=device-width,initial-scale=1,maximum-scale=1.0">


    <!-- Favicon and title
    <!--============================================= -->

    <title>LS - Status View</title>
    <link rel="icon" href="../var/favicon.ico" type="image/png">

    <!--JQuery  -->
    <script type="text/javascript" src="../jquery/jquery-1.11.3.min.js"></script>

    <!--Bootstrap -->
    <script type="text/javascript" src="../bootstrap/bootstrap.min.js"></script>
    <link rel="stylesheet" type="text/css" href="../bootstrap/bootstrap.min.css">

    <!--Part of jQuery ui -->
    <script type="text/javascript" src="../jquery/jquery-ui.min.js"></script>

    <!--highcharts -->
    <script type="text/javascript" src="../highcharts/highcharts.js"></script>

    <!-- bPopup -->
    <script type="text/javascript" src="../bpopup/jquery.bpopup.min.js"></script>


    <!-- Particular CSS and JS
    <!--============================================= -->
    <link rel="stylesheet" type="text/css" href="main.css">
    <script type="text/javascript" src="main.js"></script>

    <link rel="stylesheet" type="text/css" href="map/map.css">
    <script type="text/javascript" src="map/map.js"></script>

    <link rel="stylesheet" type="text/css" href="chart/chart.css">
    <script type="text/javascript" src="chart/chart.js"></script>


    <!-- Execution
    <!--============================================= -->
    <%
        if (view == WPMain.View.Map) out.println("<script> initMap(" + teamNumber + "); </script>");
//        if (view == WPMain.View.Chart) out.println("<script> initChart(" + teamNumber + "); </script>");
//        if (view == WPMain.View.Current) out.println("<script> initCurrent(" + teamNumber + "); </script>");
    %>

</head>


<div class="header">

    <div class="options">

        <form method="get">
            <div class="option">
                <label for="view">View</label>

                <select id="view" name="view" onchange="this.form.submit();">
                    <%
                        for (WPMain.View v : WPMain.View.values()) {
                            String selected = v.equals(view) ? "selected" : "";
                            out.println("<option value='" + v.name() + "' " + selected + ">" + v.name() + "</option>");
                        }
                    %>
                </select>
            </div>

            <div class="option">
                <label for="team">Team</label>
                <select id="team" name="team" onchange="this.form.submit();">
                    <%
                        if (isPossibleToSelectAllTeams)
                            out.println("<option value='" + 0 + "' " + (teamNumber == 0 ? "selected" : "") + "> All teams </option>");

                        for (Team t : Team.getAllBlueTeams()) {
                            String selected = t.getNumber() == teamNumber ? "selected" : "";
                            out.println("<option value='" + t.getNumber() + "' " + selected + ">" + t.getNumber() + "</option>");
                        }
                    %>
                </select>
            </div>
        </form>

    </div>

    <div class="infoForLogout">
        (You are logged in as team <b>'<%= teamNumber == 0 ? "green" : "Blue" + teamNumber %>'</b>. <a href="SLogout">Logout</a>)
    </div>

</div>


<div id="mainContent" class="content">
</div>