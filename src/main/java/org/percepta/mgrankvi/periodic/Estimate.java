package org.percepta.mgrankvi.periodic;

import com.google.gwt.thirdparty.guava.common.collect.Lists;

import java.util.LinkedList;

/**
 * Created by Mikael on 30/05/14.
 */
public abstract class Estimate {

    public int empty;
    public int actual;

    public LinkedList<Integer> actuals = Lists.newLinkedList();
    public LinkedList<Integer> empties = Lists.newLinkedList();

    public void addEmpty(int length) {
        empties.add(length);
    }

    public void addActual(int length) {
        actuals.add(length);
    }

    public abstract void calculate();

    public abstract int getLengthOfNextEmpty();

    public abstract int getLengthOfNextPeriod();
    public void fullyAddedPeriod(){}
}
