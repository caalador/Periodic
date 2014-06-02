package org.percepta.mgrankvi.periodic.gwt.client.split;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.AbstractComponentConnector;
import com.vaadin.shared.ui.Connect;
import org.percepta.mgrankvi.periodic.Split;

@Connect(Split.class)
public class SplitConnector extends AbstractComponentConnector {

    private static final long serialVersionUID = -479659036729408971L;

    @Override
    public void init() {
        super.init();
    }

    @Override
    protected Widget createWidget() {
        return GWT.create(CSplit.class);
    }

    @Override
    public CSplit getWidget() {
        return (CSplit) super.getWidget();
    }

    @Override
    public SplitState getState() {
        return (SplitState) super.getState();
    }

    @Override
    public void onStateChanged(final StateChangeEvent stateChangeEvent) {
        super.onStateChanged(stateChangeEvent);

        getWidget().setLabel(getState().label);
    }

}
