package coe.lsm.main.analysis;

import coe.lsm.main.model.Status;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jesus on 4/12/16.
 */
public class HostInfo {
    private String hostName;
    private List<ServiceInfo> serviceInfos = new ArrayList<>();

    public HostInfo(String hostName) {
        this.hostName = hostName;
    }

    public HostInfo(String hostName, List<ServiceInfo> serviceInfos) {
        this.hostName = hostName;
        this.serviceInfos = serviceInfos;
    }

    public String getHostName() {
        return hostName;
    }

    public List<ServiceInfo> getServiceInfos() {
        return serviceInfos;
    }

    public void addServiceInfo(ServiceInfo serviceInfo) {
        if (serviceInfo != null) serviceInfos.add(serviceInfo);
    }

    public int getNumberOfServicesInStatus(Status status) {
        int res = 0;

        if (serviceInfos != null)
            for (ServiceInfo serviceInfo : serviceInfos)
                if (serviceInfo.getLastStatus() != null && serviceInfo.getLastStatus().equals(status)) res++;

        return res;
    }

    public List<ServiceInfo> getServicesMatching(Status status) {
        List<ServiceInfo> res = new ArrayList<>();
        if (serviceInfos != null)
            for (ServiceInfo s : serviceInfos) if (s.getLastStatus() != null && s.getLastStatus().equals(status)) res.add(s);
        return res;
    }

    public String getAbbreviatedHostName() {
        if (hostName != null && hostName.contains("blue")) return hostName.substring(0, hostName.indexOf("blue") - 1);
        return hostName;
    }

}
