package org.percepta.mgrankvi.periodic.gwt.client.periodic.item;

import com.google.gwt.animation.client.Animation;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.ui.Widget;
import org.percepta.mgrankvi.periodic.gwt.client.PeriodicMovable;
import org.percepta.mgrankvi.periodic.gwt.client.PeriodicPaintable;
import org.percepta.mgrankvi.periodic.gwt.client.PeriodicalItem;

public class CPeriodicItem extends Widget implements PeriodicPaintable, PeriodicMovable, PeriodicalItem {

    private String label;
    private DataType[] data;
    private int height;

    protected double position;
    protected double stepSize;
    protected boolean low = false;

    private boolean animate = false;
    private int animationTime = 3000;
    private boolean animating = false;

    private PeriodicalItem previous = null;
    private PeriodicalItem next = null;

    private double[] positions;

    public CPeriodicItem() {
        // dummy element
        setElement(Document.get().createDivElement());
    }

    @Override
    public void setHeight(int height) {
        this.height = height;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setData(DataType[] data) {
        this.data = data;
        positions = new double[data.length];
    }

    @Override
    public void setLow(boolean low) {
        this.low = low;
    }

    @Override
    public void setPosition(double position) {
        this.position = position;
    }

    @Override
    public double getPosition() {
        return position;
    }

    @Override
    public void paint(Context2d context) {
        if (label == null || animating) {
            return;
        }
        context.save();
        context.setFont("bold 11px Courier New");

        double textWidth = context.measureText(label).getWidth();
        context.beginPath();
        context.fillText(label, position - (textWidth / 2), height + (low ? 21 : 12));
        context.closePath();
        context.restore();

        if (animate) {
            animate(context);
        } else {
          /*  context.save();
            context.setStrokeStyle(CssColor.make("GRAY"));
            context.beginPath();
            context.strokeRect(position - 1, 0, 2, stepSize * data.length);
            context.closePath();
            context.restore();*/

            // Paint selected spots.
            for (int i = 0; i < data.length; i++) {
                context.save();
                context.setStrokeStyle(CssColor.make("GRAY"));

                double yPosition = stepSize * i;
                positions[i] = yPosition;
                context.beginPath();
                if (data[i].equals(DataType.ACTUAL)) {
                    context.save();
                    context.setFillStyle(CssColor.make("GREEN"));
                    context.setStrokeStyle(CssColor.make("DARKGREEN"));
                    context.fillRect(position - 3, yPosition, 6, stepSize);
                    context.strokeRect(position - 3, yPosition, 6, stepSize);
                    context.restore();
                } else if (data[i].equals(DataType.ESTIMATE)) {
                    context.save();
                    context.setFillStyle(CssColor.make("BLUE"));
                    context.setStrokeStyle(CssColor.make("DARKBLUE"));
                    context.fillRect(position - 3, yPosition, 6, stepSize);
                    context.strokeRect(position - 3, yPosition, 6, stepSize);
                    context.restore();
                } else {
                    context.strokeRect(position - 1, yPosition, 2, stepSize);
                }
                context.closePath();
                context.restore();
            }
        }
    }

    private void animate(final Context2d context) {
        final Animation animator = new Animation() {
            int latestDrawn = 0;
            int scale = data.length;

            @Override
            protected void onUpdate(final double progress) {
                int draw = (int) (scale * progress);
                if (draw != latestDrawn) {
                    context.save();
                    context.setStrokeStyle(CssColor.make("GRAY"));
                    context.beginPath();

                    double yPosition = stepSize * (draw - 1);
                    if (data[draw].equals(DataType.ACTUAL)) {
                        context.save();
                        context.setFillStyle(CssColor.make("GREEN"));
                        context.setStrokeStyle(CssColor.make("DARKGREEN"));
                        context.fillRect(position - 3, yPosition, 6, stepSize);
                        context.strokeRect(position - 3, yPosition, 6, stepSize);
                        context.restore();
                    } else if (data[draw].equals(DataType.ESTIMATE)) {
                        context.save();
                        context.setFillStyle(CssColor.make("BLUE"));
                        context.setStrokeStyle(CssColor.make("DARKBLUE"));
                        context.fillRect(position - 3, yPosition, 6, stepSize);
                        context.strokeRect(position - 3, yPosition, 6, stepSize);
                        context.restore();
                    } else {
                        context.strokeRect(position - 1, yPosition, 2, stepSize);
                    }
                    positions[draw] = yPosition;

                    context.closePath();

                    context.restore();

                    latestDrawn = draw;
                }
            }

            @Override
            protected void onComplete() {
                super.onComplete();
                animate = false;
                animating = false;
            }
        };
        animator.run(animationTime);
        animating = true;
    }

    @Override
    public void animate(int time) {
        animate = true;
        animationTime = time;
    }

    @Override
    public void setStepSize(double stepSize) {
        this.stepSize = stepSize;
    }

    @Override
    public void move(double amount) {
        position = position + amount;
    }

    @Override
    public int getTypeLength(int y) {
        int length = 0;
        int position = 0;
        while (position < positions.length && positions[position + 1] <= y) {
            position++;
        }

        DataType type = data[position];
        // Find first position with same data as in position
        while (position > 0 && type == data[position - 1]) {
            position--;
        }

        if (position == 0 && previous != null) {
            length = previous.getTypeFromEnd(type);
        }

        for (int i = position; i < data.length; i++) {
            if (data[i] == type) {
                length++;
                if ((i + 1) == data.length && next != null) {
                    length += next.getTypeFromBeginning(type);
                }
            } else {
                break;
            }
        }

        return length;
    }

    @Override
    public int getTypeFromBeginning(DataType type) {
        int length = 0;
        int position = 0;

        while (position < data.length && type == data[position]) {
            position++;
            length++;
        }

        if (next != null && position == data.length) {
            length += next.getTypeFromBeginning(type);
        }

        return length;

    }

    @Override
    public int getTypeFromEnd(DataType type) {
        int length = 0;
        int position = data.length - 1;

        while (position >= 0 && type == data[position]) {
            position--;
            length++;
        }

        if (position <= 0 && previous != null) {
            length += previous.getTypeFromEnd(type);
        }

        return length;
    }

    @Override
    public void addPrevious(PeriodicalItem item) {
        previous = item;
    }

    @Override
    public void addNext(PeriodicalItem item) {
        next = item;
    }

    @Override
    public int max() {
        return data.length;
    }
}
