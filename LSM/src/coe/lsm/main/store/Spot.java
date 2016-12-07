package coe.lsm.main.store;

import coe.lsm.main.model.Status;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by jesus on 3/18/16.
 */
public class Spot {
    private ArrayList<Status> statuses = new ArrayList<>(3);
    private ArrayList<String> messages = new ArrayList<>(3);

    private boolean mixedStatus = false;

    public Spot() {
    }

    public void append(Status status, String msg) {
        if (status == null) return;

        for (Status s : statuses)
            if (s != null && s != status) {
                mixedStatus = true;
                break;
            }

        statuses.add(status);
        if (msg != null)
            messages.add(msg);
    }

    public Status getLastStatus() {
        if (statuses != null && statuses.size() > 0) return statuses.get(statuses.size() - 1);
        return Status.UNKNOWN;
    }

    public String getLastMsg() {
        if (messages != null && messages.size() > 0) return messages.get(messages.size() - 1);
        return null;
    }

    public boolean isMixedStatus() {
        return mixedStatus;
    }

    public ArrayList<Status> getStatuses() {
        return statuses;
    }

    public ArrayList<String> getMessages() {
        return messages;
    }

    public Color getColor() {
        if (statuses.size() == 0)
            return Color.black;

        if (!mixedStatus)
            return statuses.get(0).getColor();

        else {
            int redBuck = 0, greenBuck = 0, blueBuck = 0;
            for (Status s : statuses) {
                Color c = s.getColor();
                redBuck += c.getRed();
                greenBuck += c.getGreen();
                blueBuck += c.getBlue();
            }

            int count = statuses.size();
            return new Color(redBuck / count, greenBuck / count, blueBuck / count);
        }
    }
}
