package org.percepta.mgrankvi.periodic.gwt.client;

import com.google.gwt.canvas.dom.client.Context2d;

public interface PeriodicPaintable {

    public void paint(Context2d context);

    public void animate(int time);

    public void setPosition(double position);

    public double getPosition();

    public void setHeight(int height);

    public void setStepSize(double stepSize);

    public void setLow(boolean low);
}
