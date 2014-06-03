package org.percepta.mgrankvi.periodic;

import org.percepta.mgrankvi.periodic.gwt.client.periodic.item.DataType;

/**
 * Created by Mikael on 03/06/14.
 */
public class EstimateData {

    public DataType type;
    public int length;

    public EstimateData() {

    }

    public EstimateData(DataType type, int length) {
        this.type = type;
        this.length = length;
    }
}
