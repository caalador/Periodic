package org.percepta.mgrankvi.periodic;

import com.google.gwt.thirdparty.guava.common.collect.Lists;
import org.percepta.mgrankvi.periodic.gwt.client.periodic.item.DataType;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Mikael on 30/05/14.
 */
public abstract class Estimate {

    public int empty;
    public int actual;

    public LinkedList<Integer> actuals = Lists.newLinkedList();
    public LinkedList<Integer> empties = Lists.newLinkedList();
    public List<EstimateData> fullData = Lists.newLinkedList();

    public void addEmpty(int length) {
        empties.add(length);
        fullData.add(new EstimateData(DataType.ESTIMATE, length));
    }

    public void addActual(int length) {
        actuals.add(length);
        fullData.add(new EstimateData(DataType.ACTUAL, length));
    }

    public abstract void calculate();

    public abstract int getLengthOfNextEmpty();

    public abstract int getLengthOfNextPeriod();

}
