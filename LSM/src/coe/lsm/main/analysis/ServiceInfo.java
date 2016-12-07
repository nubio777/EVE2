package coe.lsm.main.analysis;

import coe.lsm.main.model.Service;
import coe.lsm.main.model.Status;

import java.util.HashMap;

/**
 * Created by jesus on 4/11/16.
 */
public class ServiceInfo {
    private Service service;
    private Status lastStatus;
    private String lastMsg;
    private HashMap<Status, Integer> minutesInStatus = new HashMap<>();

    public ServiceInfo(Service service, Status lastStatus, String lastMsg, HashMap<Status, Integer> minutesInStatus) {
        this.service = service;
        this.lastStatus = lastStatus;
        this.lastMsg = lastMsg;
        this.minutesInStatus = minutesInStatus;
    }

    public Service getService() {
        return service;
    }

    public Status getLastStatus() {
        return lastStatus;
    }

    public String getLastMsg() {
        return lastMsg;
    }

    public int getNumberOfMinutesInStatus(Status status) {
        if (minutesInStatus.containsKey(status)) return minutesInStatus.get(status);
        return 0;
    }
}
