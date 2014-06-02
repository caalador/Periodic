package org.percepta.mgrankvi.periodic.gwt.client;

import org.percepta.mgrankvi.periodic.gwt.client.periodic.item.DataType;

public interface PeriodicalItem {

    public int getTypeLength(int y);

    public int getTypeFromBeginning(DataType type);

    public int getTypeFromEnd(DataType type);

    public void addPrevious(PeriodicalItem item);

    public void addNext(PeriodicalItem item);

    public int max();
}