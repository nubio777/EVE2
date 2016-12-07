package coe.lsm.main.analysis;

import coe.lsm.main.Configuration;
import coe.lsm.main.store.Spot;
import coe.lsm.main.read.Reading;
import coe.lsm.main.model.Service;
import coe.lsm.main.model.Status;
import coe.lsm.main.model.Team;
import coe.lsm.main.read.FileReader;
import coe.lsm.main.store.Index;
import coe.lsm.main.store.Store;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by jesus on 4/13/16.
 */
public class Analysis {
    List<HostInfo> hostInfos;

    public Analysis(Store store, Team team) {
        hostInfos = new ArrayList<>();

        Index index = store.getCurrentIndex();
        int teamPosition = index.getPositionForTeam(team);
        if (teamPosition == -1) return;

        List<Service> services = index.getPresentServices();
        if (services == null) return;
        int lastMinute = index.getLastMinute();
        if (lastMinute > 1) lastMinute -- ; // We have little information the very last minute, so we better use the previous minute

        Spot spot = null;
        for (Service service : services) {
            int servicePosition = index.getPositionForService(service);

            HashMap<Status, Integer> minutesInStatus = new HashMap<>();
            for (int minute = 0; minute <= lastMinute; minute++) {
                spot = store.getSpot(teamPosition, servicePosition, minute);
                if (spot == null) continue;

                Status status = spot.getLastStatus();
                if (status == null) status = Status.UNKNOWN;

                if (minutesInStatus.containsKey(status)) {
                    int soFar = minutesInStatus.get(status);
                    if (minutesInStatus.containsKey(status)) minutesInStatus.put(status, soFar + 1);
                } else
                    minutesInStatus.put(status, 1);
            }

            Status lastStatus = Status.UNKNOWN;
            String lastMsg = null;
            if (spot != null) {
                lastStatus = spot.getLastStatus();
                lastMsg = spot.getLastMsg();
            }

            ServiceInfo serviceInfo = new ServiceInfo(service, lastStatus, lastMsg, minutesInStatus);

            boolean added = false;
            for (HostInfo hostInfo : hostInfos)
                if (hostInfo.getHostName().equals(service.getReducedHostName())) {
                    hostInfo.addServiceInfo(serviceInfo);
                    added = true;
                }

            if (!added) {
                HostInfo hostInfo = new HostInfo(service.getReducedHostName());
                hostInfo.addServiceInfo(serviceInfo);
                hostInfos.add(hostInfo);
            }
        }
    }

    public int getNumberOfServicesInStatus(Status status) {
        int res = 0;

        if (hostInfos != null)
            for (HostInfo hostInfo : hostInfos)
                res += hostInfo.getNumberOfServicesInStatus(status);

        return res;
    }


    public List<HostInfo> filter(Status status) {
        List<HostInfo> res = new ArrayList<>();
        for (HostInfo hostInfo : hostInfos) {
            List<ServiceInfo> serviceInfos = hostInfo.getServicesMatching(status);
            if (serviceInfos != null && serviceInfos.size() > 0)
                res.add(new HostInfo(hostInfo.getHostName(), serviceInfos));
        }
        return res;
    }

    public HostInfo getInfoOf(String hostName) {
        if (hostName == null) return null;
        hostName = hostName.trim().toLowerCase();
        for (HostInfo hostInfo : hostInfos)
            if (hostInfo.getHostName() != null && hostInfo.getHostName().trim().toLowerCase().equals(hostName))
                return hostInfo;
        return null;
    }


    public static void main(String[] args) throws Exception {
        Configuration configuration = Configuration.get() ;
        Store store = Store.get() ;

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

                Analysis analysis = new Analysis(store, Team.from("01"));
                int ok = analysis.getNumberOfServicesInStatus(Status.OK);
                System.out.println("ok = " + ok);

                int critical = analysis.getNumberOfServicesInStatus(Status.CRITICAL);
                System.out.println("critical = " + critical);
            }
        }, 1);

    }
}
