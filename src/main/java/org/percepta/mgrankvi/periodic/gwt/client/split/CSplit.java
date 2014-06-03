package org.percepta.mgrankvi.periodic.gwt.client.split;

import com.google.gwt.animation.client.Animation;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.ui.Widget;
import org.percepta.mgrankvi.periodic.gwt.client.PeriodicMovable;
import org.percepta.mgrankvi.periodic.gwt.client.PeriodicPaintable;

public class CSplit extends Widget implements PeriodicPaintable, PeriodicMovable {

    private String label;
    private int height;

    protected double position;

    private boolean animate = false;
    private int animationTime = 3000;

    private boolean animating = false;

    public CSplit() {
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

    @Override
    public void setLow(boolean low) {
    }

    @Override
    public void setPosition(double position) {
        this.position = position;
    }

    @Override
    public void paint(Context2d context) {
        if (label == null || animating) {
            return;
        }

        context.save();
        context.setFont("bold 11px Courier New");
        context.setFillStyle(CssColor.make("DARKGREEN"));

        context.beginPath();
        int yPosition = (height / 2) - (label.length() / 2 * 11);
        int nextYPosition = yPosition + 11;
        for (char c : label.toCharArray()) {
            double charWidth = context.measureText(String.valueOf(c)).getWidth();
            context.fillText(String.valueOf(c), position - (charWidth / 2), nextYPosition);
            nextYPosition += 11;
        }
        context.closePath();
        context.restore();

        if (animate) {
            animate(context);
        } else {
            // Paint selected spots.
            context.save();
            context.setStrokeStyle(CssColor.make("GREEN"));
            context.beginPath();
            context.strokeRect(position, 0, 1, yPosition);
            //context.strokeRect(position, yPosition + (label.length() * 11) + 6, 1, yPosition - 6);
            context.strokeRect(position, nextYPosition-5, 1, height - nextYPosition);
            context.closePath();
            context.restore();
        }
    }

    private void animate(final Context2d context) {
        final Animation animator = new Animation() {

            @Override
            protected void onUpdate(final double progress) {
            }

            @Override
            protected void onComplete() {
                super.onComplete();
                animate = false;
                animating = false;
            }
        };
        animator.run(animationTime);
    }

    @Override
    public void animate(int time) {
        animate = true;
        animationTime = time;
    }

    @Override
    public void setStepSize(double stepSize) {
    }

    @Override
    public void move(double amount) {
        position = position + amount;
    }

    @Override
    public double getPosition() {
        return position;
    }

}
