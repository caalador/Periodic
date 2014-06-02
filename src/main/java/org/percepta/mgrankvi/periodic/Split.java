package org.percepta.mgrankvi.periodic;

import com.vaadin.ui.AbstractComponent;
import org.percepta.mgrankvi.periodic.gwt.client.split.SplitState;

public class Split extends AbstractComponent {

    private static final long serialVersionUID = -8316846251162309697L;

    public Split(String caption) {
        getState().label = caption;
    }

    @Override
    protected SplitState getState() {
        return (SplitState) super.getState();
    }
}
