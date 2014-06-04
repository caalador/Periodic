package org.percepta.mgrankvi.periodic;

import com.google.gwt.thirdparty.guava.common.collect.Lists;
import com.vaadin.ui.AbstractComponentContainer;
import com.vaadin.ui.Component;
import com.vaadin.ui.HasComponents;
import org.percepta.mgrankvi.periodic.gwt.client.periodic.PeriodicState;
import org.percepta.mgrankvi.periodic.gwt.client.periodic.item.DataType;
import org.percepta.mgrankvi.periodic.gwt.client.periodic.item.Period;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Component for visualisation of periodic data.
 *
 * @author Mikael Grankvist / Vaadin }>
 */
public class Periodic extends AbstractComponentContainer implements HasComponents {

    private static final long serialVersionUID = -1418456044741108983L;

    List<Component> children = new LinkedList<Component>();

    public Periodic() {

    }

    /**
     * Set maximum size for left side scale.
     *
     * @param scale
     */
    public void setScale(int scale) {
        getState().scale = scale;
    }

    /**
     * Set height of the graph in px
     *
     * @param height
     */
    public void setHeightPx(int height) {
        getState().heightPx = height;
    }

    /**
     * Set width of the graph in px
     *
     * @param width
     */
    public void setWidthPx(int width) {
        getState().widthPx = width;
    }

    /**
     * Animate on first draw in given time.
     *
     * @param time Animation time in ms
     */
    public void setAnimate(int time) {
        getState().animate = time;
    }

    /**
     * If immediate tooltip will follow mouse if something to show.
     *
     * @param immediateTooltip
     */
    public void setImmediateTooltip(boolean immediateTooltip) {
        super.setImmediate(immediateTooltip);
    }

    @Override
    protected PeriodicState getState() {
        return (PeriodicState) super.getState();
    }

    @Override
    public void addComponent(final Component c) {
        children.add(c);
        super.addComponent(c);
        markAsDirty();
    }

    @Override
    public void removeComponent(final Component c) {
        children.remove(c);
        super.removeComponent(c);
        markAsDirty();
    }

    @Override
    public void replaceComponent(final Component oldComponent, final Component newComponent) {
        final int index = children.indexOf(oldComponent);
        if (index != -1) {
            children.remove(index);
            children.add(index, newComponent);
            fireComponentDetachEvent(oldComponent);
            fireComponentAttachEvent(newComponent);
            markAsDirty();
        }
    }

    @Override
    public int getComponentCount() {
        return children.size();
    }

    @Override
    public Iterator<Component> iterator() {
        return children.iterator();
    }

    public void setWidth(int width) {
        getState().widthPx = width;
    }

    public void setHeight(int height) {
        getState().heightPx = height;
    }

    /**
     * Remove all estimates.
     */
    public void clearEstimates() {
        for (Component c : children) {
            if (c instanceof PeriodicItem) {
                List<Period> estimates = Lists.newArrayList();
                List<Period> data = ((PeriodicItem) c).getData();
                for (Period p : data) {
                    if (p.type.equals(DataType.ESTIMATE)) {
                        estimates.add(p);
                    }
                }
                for (Period p : estimates) {
                    data.remove(p);
                }
                ((PeriodicItem) c).setData(data);
            }
        }
    }

    /**
     * Create estimates from existing data.
     *
     * @param estimateClass Estimate class implementation
     */
    public void estimateOccurrences(Class estimateClass) {
        List<DataType[]> data = Lists.newLinkedList();
        LinkedList<Periodical> periodicItems = Lists.newLinkedList();

        // Collect all periodic items without data
        for (Component c : children) {
            if (c instanceof Periodical) {
                final Periodical periodicItem = (Periodical) c;
                if (periodicItem.getData().isEmpty()) {
                    periodicItems.add(periodicItem);
                } else {
                    data.add(periodicItem.getDataSet());
                }
            }
        }

        if (periodicItems.isEmpty()) {
            return;
        }

        // Add first periodic item containing data to list as first as this is where we start
        if (children.indexOf(periodicItems.getFirst()) > 0) {
            int index = children.indexOf(periodicItems.getFirst());
            Component c = children.get(--index);
            while (!(c instanceof Periodical) && index >= 0) {
                c = children.get(--index);
            }
            if (c instanceof Periodical) {
                periodicItems.addFirst((Periodical) c);
            }
        }

        // Populate estimation
        Estimate estimate = getEstimate(data, estimateClass);
        // Have estimation implementation calculate estimates from data
        estimate.calculate();

        // Create periods using estimation data.
        EstimateData estimateData = new EstimateData();
        estimateData.estimate = estimate;
        estimateData.emptySlots = estimate.getLengthOfNextEmpty();
        estimateData.periodSlots = estimate.getLengthOfNextPeriod();
        estimateData.lastPeriod = new Period(Lists.newLinkedList(periodicItems.getFirst().getData()).getLast());

        for (Periodical item : periodicItems) {
            estimateData.item = item;

            fillPeriodicItem(estimateData);
        }

    }

    private void fillPeriodicItem(EstimateData estimateData) {
        if (estimateData.emptySlots > 0) {
            // Take away from emptySlots until end inside item.
            if (estimateData.lastPeriod.end + estimateData.emptySlots < estimateData.item.getLength()) {
                estimateData.lastPeriod.end += estimateData.emptySlots;

                estimateData.emptySlots = 0;
            } else {
                estimateData.emptySlots -= estimateData.item.getLength() - estimateData.lastPeriod.end;
                // start from beginning.
                estimateData.lastPeriod = new Period(1, 1);

                // Get next item to work with
                return;
            }
        }

        if (estimateData.lastPeriod.end + estimateData.periodSlots < estimateData.item.getLength()) {
            Period newPeriod = new Period(estimateData.lastPeriod.end, estimateData.lastPeriod.end + estimateData.periodSlots);
            newPeriod.type = DataType.ESTIMATE;
            estimateData.item.addPeriod(newPeriod);

            // Create new period as last period as it may be edited
            estimateData.lastPeriod = new Period(newPeriod);

            // As all data has been used get next set of empty + actual
            estimateData.updateSlots();

            // Recursive call for same item as we may fit more data into this or use up some empty slots
            fillPeriodicItem(estimateData);
        } else {
            // last position to end.
            Period newPeriod = new Period(estimateData.lastPeriod.end, estimateData.item.getLength());
            newPeriod.type = DataType.ESTIMATE;
            estimateData.item.addPeriod(newPeriod);

            // Create new period as last period as it may be edited
            estimateData.lastPeriod = new Period(newPeriod);

            estimateData.periodSlots -= estimateData.lastPeriod.getLength();
            estimateData.lastPeriod = new Period(1, 1);
        }

    }

    private Estimate getEstimate(List<DataType[]> data, Class estimateClass) {

        Estimate estimate;

        Object object = null;
        try {
            object = estimateClass.newInstance();
        } catch (InstantiationException e) {
            System.out.println(e);
        } catch (IllegalAccessException e) {
            System.out.println(e);
        }
        if (object instanceof Estimate) {
            estimate = (Estimate) object;
        } else {
            estimate = new Median();
        }

        DataType target = data.get(0)[0];
        int length = 0;
        for (DataType[] array : data) {
            for (DataType type : array) {
                if (type.equals(target)) {
                    length++;
                } else if (type.equals(DataType.ACTUAL)) {
                    // PreviousTarget Empty or Estimate
                    estimate.addEmpty(length);
                    length = 1;
                    target = type;
                } else if (type.equals(DataType.EMPTY)) {
                    // PreviousTarget Actual or Estimate
                    estimate.addActual(length);
                    length = 1;
                    target = type;
                } else {
                    length = 1;
                    target = type;
                }
            }
        }
        return estimate;
    }

    private class EstimateData {
        Periodical item;
        Integer emptySlots;
        Integer periodSlots;
        Period lastPeriod;
        Estimate estimate;

        public void updateSlots() {
            emptySlots = estimate.getLengthOfNextEmpty();
            periodSlots = estimate.getLengthOfNextPeriod();
        }
    }
}
