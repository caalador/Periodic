package org.percepta.mgrankvi.periodic;

import com.google.gwt.thirdparty.guava.common.collect.Lists;
import org.percepta.mgrankvi.periodic.gwt.client.periodic.item.DataType;

import java.util.LinkedList;
import java.util.List;

/**
 * Abstract class for Estimating future data based on existing collected data
 * Created by Mikael on 30/05/14.
 */
public abstract class Estimate {

    public int empty;
    public int actual;

    public LinkedList<Integer> actuals = Lists.newLinkedList();
    public LinkedList<Integer> empties = Lists.newLinkedList();
    public List<EstimateData> fullData = Lists.newLinkedList();

    /**
     * Add new empty item
     *
     * @param length
     */
    public void addEmpty(int length) {
        empties.add(length);
        fullData.add(new EstimateData(DataType.ESTIMATE, length));
    }

    /**
     * Add new period length
     *
     * @param length
     */
    public void addActual(int length) {
        actuals.add(length);
        fullData.add(new EstimateData(DataType.ACTUAL, length));
    }

    /**
     * The estimation implementation should analyze the data set and create it's period and empty data information
     */
    public abstract void calculate();

    /**
     * The length of the next empty space to be added before the next new period
     *
     * @return Next empty length
     */
    public abstract int getLengthOfNextEmpty();

    /**
     * The length of the next estimated period
     *
     * @return Period Next period length
     */
    public abstract int getLengthOfNextPeriod();

}
