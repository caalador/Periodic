package org.percepta.mgrankvi.periodic;

import java.util.Collections;

/**
 * Created by Mikael on 27/03/14.
 */
public class Median extends Estimate {

    @Override
    public void calculate() {
        Collections.sort(actuals);
        Collections.sort(empties);
        if (actuals.size() % 2 == 0) {
            actual = actuals.get(actuals.size() / 2);
        } else {
            int actual1 = actuals.get((int) Math.floor(actuals.size() / 2.0));
            int actual2 = actuals.get((int) Math.ceil(actuals.size() / 2.0));
            actual = (actual1 + actual2) / 2;
        }
        if (empties.size() % 2 == 0) {
            empty = empties.get(empties.size() / 2);
        } else {
            int empty1 = empties.get((int) Math.floor(empties.size() / 2.0));
            int empty2 = empties.get((int) Math.ceil(empties.size() / 2.0));
            empty = (empty1 + empty2) / 2;
        }
    }

    @Override
    public int getLengthOfNextEmpty() {
        return empty;
    }

    @Override
    public int getLengthOfNextPeriod() {
        return actual;
    }

    boolean next = true;

}
