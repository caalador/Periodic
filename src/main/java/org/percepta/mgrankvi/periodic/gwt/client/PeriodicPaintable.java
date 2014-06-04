package org.percepta.mgrankvi.periodic.gwt.client;

import com.google.gwt.canvas.dom.client.Context2d;

/**
 * Interface for items that can be painted.
 */
public interface PeriodicPaintable {

    /**
     * Paint item to given {@link com.google.gwt.canvas.client.Canvas} using given Context
     *
     * @param context Canvas context to use
     */
    public void paint(Context2d context);

    /**
     * Set animation of component
     *
     * @param time
     */
    public void animate(int time);

    /**
     * Horizontal position of item
     *
     * @param position
     */
    public void setPosition(double position);

    /**
     * Return item horizontal position
     *
     * @return
     */
    public double getPosition();

    /**
     * Sets the height that is available for use so component stays inside wanted area.
     *
     * @param height
     */
    public void setHeight(int height);

    /**
     * Periodic data item  gets the height of one unit.
     *
     * @param stepSize
     */
    public void setStepSize(double stepSize);

    /**
     * Information on if label should be drawn down (low).
     * Labels are set up or down.
     *
     * low is used within the formula: height + (low ? 21 : 12)
     *
     * @param low
     */
    public void setLow(boolean low);
}
