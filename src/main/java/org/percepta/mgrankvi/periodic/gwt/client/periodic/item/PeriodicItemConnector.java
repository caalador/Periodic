package org.percepta.mgrankvi.periodic.gwt.client.periodic.item;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.AbstractComponentConnector;
import com.vaadin.shared.ui.Connect;
import org.percepta.mgrankvi.periodic.PeriodicItem;

import java.util.List;

@Connect(PeriodicItem.class)
public class PeriodicItemConnector extends AbstractComponentConnector {

    private static final long serialVersionUID = -479659036729408971L;

    @Override
    public void init() {
        super.init();
    }

    @Override
    protected Widget createWidget() {
        return GWT.create(CPeriodicItem.class);
    }

    @Override
    public CPeriodicItem getWidget() {
        return (CPeriodicItem) super.getWidget();
    }

    @Override
    public PeriodicItemState getState() {
        return (PeriodicItemState) super.getState();
    }

    @Override
    public void onStateChanged(final StateChangeEvent stateChangeEvent) {
        super.onStateChanged(stateChangeEvent);

        getWidget().setLabel(getState().label);
        getWidget().setData(buildDataSet(getState().length, getState().periods));
    }

    private DataType[] buildDataSet(int length, List<Period> periods) {
        DataType[] data = new DataType[length];

        for (int i = 0; i < length; i++) {
            data[i] = DataType.EMPTY;
        }
        for (Period period : periods) {
            for (int i = period.start - 1; i < period.end; i++) {
                data[i] = period.type;
            }
        }
        return data;
    }

}
