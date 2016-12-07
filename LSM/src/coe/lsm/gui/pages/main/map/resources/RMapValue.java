package coe.lsm.gui.pages.main.map.resources;

import coe.lsm.gui.app.App;
import coe.lsm.main.model.Service;
import coe.lsm.main.model.Status;
import coe.lsm.main.model.Team;
import coe.lsm.main.store.Index;
import coe.lsm.main.store.Spot;
import coe.lsm.main.store.Store;
import org.apache.wicket.request.resource.AbstractResource;
import org.apache.wicket.util.time.Duration;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * Created by jesus on 3/29/16.
 */
public class RMapValue extends AbstractResource {
    @Override
    protected ResourceResponse newResourceResponse(Attributes attributes) {
        ResourceResponse resourceResponse = new ResourceResponse();
        resourceResponse.setContentType("text");
        resourceResponse.setCacheDuration(Duration.seconds(0));
        resourceResponse.setWriteCallback(new WriteCallback() {
            @Override
            public void writeData(Attributes attributes) throws IOException {

                try {
                    int teamNr = Integer.parseInt(attributes.getParameters().get("teamNumber").toString());
                    int minute = Integer.parseInt(attributes.getParameters().get("minute").toString());
                    int serviceNr = Integer.parseInt(attributes.getParameters().get("serviceNumber").toString());
                    attributes.getResponse().write(getInfoOnClickFor(teamNr, minute, serviceNr, "|"));
                } catch (Exception e) {
                    attributes.getResponse().write("");
                }
            }
        });

        return resourceResponse;
    }

    private String getInfoOnClickFor(int teamNr, int minute, int serviceNr, String separator) {
        String noInfo = "Info not available " + separator + separator + separator + separator + separator;

        Store store = App.get().getStore();
        Index index = store.getCurrentIndex();

        // Minute
        if (minute < 0 || serviceNr < 0) return noInfo;

        // Team
        if (!Team.isATrueBlue(teamNr)) return noInfo;
        Team team = Team.from(teamNr);
        if (team == null) return noInfo;

        // Service
        java.util.List<Service> presentServices = index.getPresentServices();
        if (serviceNr >= presentServices.size()) return noInfo;
        Service service = presentServices.get(serviceNr);

        // Spot
        Spot spot = store.getSpot(index.getPositionForTeam(team), index.getPositionForService(service), minute);
        if (spot == null) return noInfo;

        return codeInfo(spot, team, service, index.getDate(minute), separator);
    }

    public String codeInfo(Spot spot, Team team, Service service, Date lastRead, String separator) {

        // The status
        List<Status> statuses = spot.getStatuses();
        String status = "";
        if (statuses.size() == 1)
            status = statuses.get(0).toString();
        else if (statuses.size() > 1) {
            if (spot.isMixedStatus()) {
                for (Status s : statuses)
                    status += s.toString() + ", ";
                status = status.substring(0, status.length() - 2);
            } else
                status = spot.getLastStatus().toString() + " (" + statuses.size() + ")";
        }
        if (status == null || status.trim().length() == 0) status = "No data available";

        // The host and time
        String host = service.getReducedHostName();
        String time = new SimpleDateFormat("HH:mm").format(lastRead);

        // The msg
        String msg = "";
        List<String> messages = spot.getMessages();
        if (messages != null)
            for (String m : messages)
                msg += m + "<br>";

        // Final result
        return status + separator + service.getName() + separator + team.getFullName() + separator + host + separator + time + separator + msg;
    }

}
