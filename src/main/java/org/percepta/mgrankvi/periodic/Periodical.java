package org.percepta.mgrankvi.periodic;

import org.percepta.mgrankvi.periodic.gwt.client.periodic.item.DataType;
import org.percepta.mgrankvi.periodic.gwt.client.periodic.item.Period;

import java.util.List;

/**
 * Interface for server side periodic item
 * Created by Mikael on 04/06/14.
 */
public interface Periodical {

    /**
     * Return the total length of the item
     *
     * @return Total length of item
     */
    public int getLength();

    /**
     * Return item data as Periods
     *
     * @return Period data
     */
    public List<Period> getData();

    /**
     * Return item data as a DataType array
     *
     * @return DataType array
     */
    public DataType[] getDataSet();

    /**
     * Add a new period to item
     *
     * @param period Period to add
     */
    public void addPeriod(Period period);
}
