package coe.lsm.main.model;

import java.util.TreeMap;

/**
 * Created by jesus on 4/13/16.
 */
public class Service implements Comparable<Service> {
    private String reducedHostName; // Does not include "blue*". For example "idc2"
    private String name; // For example "ping"

    private String full; // reducedHostName - name

    private static final TreeMap<String, Service> services = new TreeMap<>();


    public static Service from(String reducedHostName, String name) {
        if (reducedHostName == null) return null;
        if (name == null) return null;

        String full = reducedHostName.trim() + " - " + name.trim();
        full = full.toLowerCase();

        if (services.containsKey(full)) return services.get(full);

        Service service = new Service(reducedHostName, name, full);
        services.put(full, service);
        return service;
    }

    public Service(String reducedHostName, String name, String full) {
        this.reducedHostName = reducedHostName;
        this.name = name;
        this.full = full;
    }

    public String getReducedHostName() {
        return reducedHostName;
    }

    public String getName() {
        return name;
    }

    public String getFull() {
        return full;
    }

    @Override
    public int compareTo(Service o) {
        return full.compareTo(o.full) ;
    }
}
