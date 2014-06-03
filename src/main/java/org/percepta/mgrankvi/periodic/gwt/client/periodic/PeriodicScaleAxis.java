package org.percepta.mgrankvi.periodic.gwt.client.periodic;

import com.google.gwt.animation.client.Animation;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.TextMetrics;

public class PeriodicScaleAxis {

    private int scale;
    private int width;
    private int height;
    protected double perStep;
    private double fontSize = 11.0;

    private boolean animate = false;
    private int animationTime = 3000;
    private boolean animating = false;

    protected double verticalScaleWidth;

    public PeriodicScaleAxis(int scale, int width, int height) {
        this.scale = scale;
        this.width = width;
        this.height = height;

        setPerStep();
    }

    private void setPerStep() {
        perStep = (height - (fontSize * 2)) / scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
        setPerStep();
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
        setPerStep();
    }

    public int getActiveHeight() {
        return (int) (height - (fontSize * 2));
    }

    public void setFontSize(double fontSize) {
        this.fontSize = fontSize;
        setPerStep();
    }

    public void setSize(Context2d context){
        TextMetrics textInfo = context.measureText(Integer.toString(scale));
        verticalScaleWidth = textInfo.getWidth() + 10;
    }

    public void paint(Context2d context) {
        if(animating){
            return;
        }
        context.save();
        context.setFont("bold 11px Courier New");

        TextMetrics textInfo = context.measureText(Integer.toString(scale));
        verticalScaleWidth = textInfo.getWidth() + 10;
        double heightOffset = height - (fontSize * 2);

        context.beginPath();
        context.moveTo(verticalScaleWidth, 0);
        context.lineTo(verticalScaleWidth, heightOffset);
        context.lineTo(width, heightOffset);
        context.moveTo(verticalScaleWidth, 0);
        context.closePath();
        context.stroke();

        if (animate) {
            animate(context);
        } else {
            context.beginPath();

            for (int i = 1; i <= scale; i++) {
                context.fillText(Integer.toString(i), 5, perStep * i);
            }
            context.closePath();
        }
        context.restore();
    }

    private void animate(final Context2d context) {
        final Animation animator = new Animation() {
            int latestDrawn = 0;

            @Override
            protected void onUpdate(final double progress) {
                int draw = (int) (scale * progress);
                if (draw != latestDrawn) {
                    context.save();
                    context.beginPath();

                    context.fillText(Integer.toString(draw), 5, perStep * draw);

                    context.closePath();
                    context.fill();

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

    public void animate(int time) {
        animate = true;
        animationTime = time;
    }

}
