package org.percepta.mgrankvi.periodic.gwt.client;

import org.percepta.mgrankvi.periodic.gwt.client.periodic.item.DataType;

/**
 *
 */
public interface PeriodicalItem {

    /**
     * Get set length for type at position y
     *
     * @param y Position to get type and length from
     * @return Period length
     */
    public int getTypeLength(int y);

    /**
     * Get amount of occurrences of {@link DataType} starting from the beginning
     *
     * @param type {@link DataType} to count
     * @return
     */
    public int getTypeFromBeginning(DataType type);

    /**
     * Get amount of occurrences of {@link DataType} starting from the end
     *
     * @param type {@link DataType} to count
     * @return
     */
    public int getTypeFromEnd(DataType type);

    /**
     * Set previous periodical item (used for traversing)
     *
     * @param item {@link PeriodicalItem} that comes before this
     */
    public void setPrevious(PeriodicalItem item);

    /**
     * Set next periodical item (used for traversing)
     *
     * @param item {@link PeriodicalItem} that comes after this
     */
    public void setNext(PeriodicalItem item);

    /**
     * Get item length
     *
     * @return item full length
     */
    public int fullLength();
}