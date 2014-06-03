package org.percepta.mgrankvi.periodic.gwt.client.periodic;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.VConsole;
import org.percepta.mgrankvi.periodic.gwt.client.PeriodicMovable;
import org.percepta.mgrankvi.periodic.gwt.client.PeriodicPaintable;
import org.percepta.mgrankvi.periodic.gwt.client.PeriodicalItem;

import java.util.LinkedList;
import java.util.List;

public class CPeriodic extends Composite implements MouseDownHandler, MouseMoveHandler, MouseUpHandler, MouseOutHandler {

    private static final String CLASSNAME = "c-periodic";

    protected final Canvas canvas;
    protected final Canvas tooltipCanvas;

    private final FlowPanel content;
    private final SimplePanel baseContent;

    private int width = 400;
    private int height = 300;

    private int scale = 31;
    private boolean animate = true;
    private int animationTime = 3000;

    private boolean immediate = false;
    private boolean move = false;
    private int down = 0;

    private final PeriodicScaleAxis scaleAxis = new PeriodicScaleAxis(scale, width, height);
    private final List<PeriodicPaintable> paintable = new LinkedList<PeriodicPaintable>();
    private PeriodicalItem lastPeriodical = null;

    private Timer hold = null;

    private int origin = 0;

    public CPeriodic() {
        content = new FlowPanel();
        content.setSize(width + "px", height + "px");

        baseContent = new SimplePanel();
        baseContent.add(content);

        initWidget(baseContent);

        setSize(width + "px", height + "px");
        setStyleName(CLASSNAME);

        addDomHandler(this, MouseDownEvent.getType());
        addDomHandler(this, MouseMoveEvent.getType());
        addDomHandler(this, MouseUpEvent.getType());

        canvas = Canvas.createIfSupported();
        tooltipCanvas = Canvas.createIfSupported();
        if (canvas != null) {
            content.add(canvas);
            content.add(tooltipCanvas);
            tooltipCanvas.getCanvasElement().getStyle().setPosition(Style.Position.RELATIVE);
            tooltipCanvas.getCanvasElement().getStyle().setTop(0, Style.Unit.PX);
            //tooltipCanvas.getCanvasElement().getStyle().setLeft(-width, Style.Unit.PX);
            tooltipCanvas.getCanvasElement().getStyle().setTop(-height, Style.Unit.PX);
            if (animate) {
                scaleAxis.animate(3000);
            }
            scaleAxis.setSize(canvas.getContext2d());
            clearCanvas();
        } else {
            getElement().setInnerHTML("Canvas not supported");
        }
    }

    protected void setSize(int width, int height) {

        this.width = width;
        this.height = height;
        scaleAxis.setWidth(width);
        scaleAxis.setHeight(height);
        //tooltipCanvas.getCanvasElement().getStyle().setLeft(-width, Style.Unit.PX);
        tooltipCanvas.getCanvasElement().getStyle().setTop(-height-3, Style.Unit.PX);
        baseContent.setSize(width + "px", height + "px");
        content.setSize(width + "px", height + "px");

        clearCanvas();
    }

    protected void setScale(int scale) {
        this.scale = scale;
        scaleAxis.setScale(scale);
        clearCanvas();

        for (PeriodicPaintable item : paintable) {
            item.setStepSize(scaleAxis.perStep);
        }
        //paint();
    }

    protected void setImmediate(boolean immediate) {
        this.immediate = immediate;
    }

    protected void clearCanvas() {
        canvas.setCoordinateSpaceWidth(width);
        canvas.setCoordinateSpaceHeight(height);

        tooltipCanvas.setCoordinateSpaceWidth(width);
        tooltipCanvas.setCoordinateSpaceHeight(height);
    }

    protected void animate(int time) {
        this.animate = true;
        animationTime = time;
        for (PeriodicPaintable item : paintable) {
            item.animate(time);
        }
        scaleAxis.animate(time);
        this.animate = false;
    }

    protected void paint() {
        scaleAxis.paint(canvas.getContext2d());
        for (PeriodicPaintable item : paintable) {
            if (item.getPosition() > scaleAxis.verticalScaleWidth) {
                item.paint(canvas.getContext2d());
            }
        }
    }

    public void clearChildItems() {
        paintable.clear();
    }

    public void add(Widget widget) {
        if (widget instanceof PeriodicPaintable) {
            PeriodicPaintable item = (PeriodicPaintable) widget;
            item.setHeight(scaleAxis.getActiveHeight());
            item.setStepSize(scaleAxis.perStep);
            paintable.add(item);
            item.setPosition(scaleAxis.verticalScaleWidth + (paintable.size() * 20));

            item.setLow(paintable.size() % 2 == 0);
            if (animate) {
                item.animate(animationTime);
            }
            if (widget instanceof PeriodicalItem) {
                if (lastPeriodical != null) {
                    lastPeriodical.addNext((PeriodicalItem) widget);
                    ((PeriodicalItem) widget).addPrevious(lastPeriodical);
                }
                lastPeriodical = (PeriodicalItem) widget;
            }
        } else {
            content.add(widget);
        }
    }

    /**
     * Mouse handlers
     */

    @Override
    public void onMouseUp(MouseUpEvent event) {
        move = false;
        down = 0;
    }

    @Override
    public void onMouseMove(MouseMoveEvent event) {
        if (hold != null) {
            hold.cancel();
            hold = null;
        }
        if (move) {
            for (PeriodicPaintable item : paintable) {
                if (item instanceof PeriodicMovable) {
                    ((PeriodicMovable) item).move(event.getClientX() - down);
                }
            }
            origin += event.getClientX() - down;
            down = event.getClientX();
            clearCanvas();
            paint();
        } else {
            int x = event.getRelativeX(this.getElement());
            int y = event.getRelativeY(this.getElement());

            clearCanvas();
            paint();
            if (x > scaleAxis.verticalScaleWidth + 5 && x < width && y < scaleAxis.getActiveHeight()) {
                PeriodicPaintable periodicPaintable = null;

                int position = 0;
                for (PeriodicPaintable item : paintable) {
                    if (item.getPosition() > x - 10 && item.getPosition() < x + 10) {
                        periodicPaintable = item;
                        break;
                    }
                }

                if (!(periodicPaintable instanceof PeriodicalItem)) {
                    return;
                }

                if (periodicPaintable.getPosition() > x - 10 && periodicPaintable.getPosition() < x + 10) {
                    if (immediate) {
                        int length = ((PeriodicalItem) periodicPaintable).getTypeLength(event.getRelativeY(this.getElement()));
                        //paintTooltip(event.getRelativeX(this.getElement()), event.getRelativeY(this.getElement()), length);
                        paintTooltip((int) periodicPaintable.getPosition(), event.getRelativeY(this.getElement()), length);
                    } else {
                        int length = ((PeriodicalItem) periodicPaintable).getTypeLength(event.getRelativeY(this.getElement()));
                        hold = new DelayedTimer((int) periodicPaintable.getPosition(), event.getRelativeY(this.getElement()), length);
                        hold.schedule(250);
                    }
                }
            }
        }
    }

    private class DelayedTimer extends Timer {
        private int relativeX, relativeY, length;

        public DelayedTimer(int relativeX, int relativeY, int length) {
            this.length = length;
            this.relativeX = relativeX;
            this.relativeY = relativeY;
        }

        @Override
        public void run() {
            paintTooltip(relativeX, relativeY, length);
        }
    }

    private void paintTooltip(int relativeX, int relativeY, int length) {
        int directionX = 1;
        int directionY = 1;

        Context2d tooltip = tooltipCanvas.getContext2d();
        int tooltipTextWidth = (int) tooltip.measureText("Length: " + length).getWidth() + 15;

        if (relativeX >= (width - tooltipTextWidth)) {
            directionX = -1;
        }
        if (relativeY <= 15) {
            directionY = -1;
        }

        tooltip.moveTo(relativeX, relativeY);
        tooltip.beginPath();
        tooltip.lineTo(relativeX + (5 * directionX), relativeY - (5 * directionY));
        tooltip.lineTo(relativeX + (5 * directionX), relativeY - (15 * directionY));
        tooltip.lineTo(relativeX + (tooltipTextWidth * directionX), relativeY - (15 * directionY));
        tooltip.lineTo(relativeX + (tooltipTextWidth * directionX), relativeY - (3 * directionY));
        tooltip.lineTo(relativeX + (5 * directionX), relativeY - (3 * directionY));
        tooltip.lineTo(relativeX, relativeY);
        tooltip.closePath();
        tooltip.save();
        tooltip.setFillStyle(CssColor.make("LIGHTGREEN"));
        tooltip.stroke();
        tooltip.fill();
        tooltip.restore();

        int textOffset = -5;

        if (directionY == -1) {
            textOffset = 11;
        }
        if (directionX == -1) {
            directionX = (int) -(tooltipTextWidth * 0.1);
        }

        tooltip.fillText("Period: " + length, relativeX + (10 * directionX), relativeY + textOffset);
    }

    @Override
    public void onMouseDown(MouseDownEvent event) {
        move = true;
        down = event.getClientX();
    }

    @Override
    public void onMouseOut(MouseOutEvent event) {
        move = false;
        down = 0;
    }


}
