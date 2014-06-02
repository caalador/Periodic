package org.percepta.mgrankvi.periodic;

import java.util.Collections;

/**
 * Created by Mikael on 27/03/14.
 */
public class Mean extends Estimate {

    @Override
    public void calculate() {
        Collections.sort(actuals);
        Collections.sort(empties);

        actualMean();
        emptyMean();

    }

    private void emptyMean() {
        empty = 0;
        for(int empt : empties){
            empty += empt;
        }

        empty -= empties.getFirst();
        empty -= empties.getLast();
        empty = empty / (empties.size()-2);
    }

    private void actualMean() {
        actual = 0;
        for(int act : actuals){
            actual += act;
        }
        actual -= actuals.getFirst();
        actual -= actuals.getLast();
        actual = actual / (actuals.size()-2);
    }

    @Override
    public int getLengthOfNextEmpty() {
        return empty;
    }

    @Override
    public int getLengthOfNextPeriod() {
        return actual;
    }

}
