package org.percepta.mgrankvi.periodic;

import com.google.gwt.thirdparty.guava.common.collect.Lists;
import com.vaadin.ui.AbstractComponent;
import org.percepta.mgrankvi.periodic.gwt.client.periodic.item.DataType;
import org.percepta.mgrankvi.periodic.gwt.client.periodic.item.Period;
import org.percepta.mgrankvi.periodic.gwt.client.periodic.item.PeriodicItemState;

import java.util.List;

public class PeriodicItem extends AbstractComponent implements Periodical {

    private static final long serialVersionUID = -551357643545036296L;

    public PeriodicItem(String label, DataType[] data) {
        getState().periods = buildPeriods(data);
        getState().label = label;
        getState().length = data.length;
    }

    public PeriodicItem(String label, int[] data) {
        getState().label = label;
        getState().length = data.length;

        DataType[] dataArray = new DataType[data.length];
        for (int i = 0; i < data.length; i++) {
            dataArray[i] = (data[i] == 0 ? DataType.EMPTY : DataType.ACTUAL);
        }
        getState().periods = buildPeriods(dataArray);
    }

    public PeriodicItem(String label, int length, Period... periods) {
        getState().label = label;
        getState().length = length;
        getState().periods = Lists.newArrayList(periods);
    }

    public void setData(DataType[] data) {
        getState().periods = buildPeriods(data);
    }

    public void setData(List<Period> periods) {
        getState().periods = periods;
    }

    public void addPeriod(Period period) {
        getState().periods.add(period);
    }

    public String getLabel() {
        return getState().label;
    }

    @Override
    protected PeriodicItemState getState() {
        return (PeriodicItemState) super.getState();
    }

    public DataType[] getDataSet() {
        DataType[] data = new DataType[getState().length];

        for (int i = 0; i < getState().length; i++) {
            data[i] = DataType.EMPTY;
        }
        for (Period period : getState().periods) {
            for (int i = period.start - 1; i < period.end; i++) {
                data[i] = period.type;
            }
        }
        return data;
    }

    public List<Period> getData() {
        return Lists.newLinkedList(getState().periods);
    }

    private List<Period> buildPeriods(DataType[] data) {
        List<Period> periods = Lists.newLinkedList();
        Period period = null;
        for (int i = 0; i < data.length; i++) {
            if (!data[i].equals(DataType.EMPTY) && period == null) {
                period = new Period(i + 1, -1);
                period.type = data[i];
            } else if (period != null && (data[i].equals(DataType.EMPTY) || data[i] != period.type)) {
                period.end = i;
                periods.add(period);
                period = null;
                if (!data[i].equals(DataType.EMPTY)) {
                    period = new Period(i + 1, -1);
                    period.type = data[i];
                }
            }
        }
        if (period != null) {
            period.end = data.length;
            periods.add(period);
        }
        return periods;
    }

    public int getLength() {
        return getState().length;
    }
}
