package org.percepta.mgrankvi.periodic.gwt.client.periodic.item;

import java.io.Serializable;

/**
 * Created by Mikael on 25/03/14.
 */
public class Period implements Serializable {

    public int start;
    public int end;
    public DataType type = DataType.ACTUAL;

    private Period() {
    }

    public Period(int start, int end) {
        this.start = start;
        this.end = end;
    }

    public Period(Period period) {
        this.start = period.start;
        this.end = period.end;
    }

    public Period(String period) {
        int index = period.indexOf(':');
        if (index == -1) {
            throw new RuntimeException("Faulty input string for Period.");
        }
        String[] parts = period.split(":");
        if (parts.length != 2) {
            throw new RuntimeException("Faulty input string for Period.");
        }
        start = Integer.parseInt(parts[0]);
        end = Integer.parseInt(parts[1]);
    }

    public int getLength() {
        return end - start + 1;
    }
}
