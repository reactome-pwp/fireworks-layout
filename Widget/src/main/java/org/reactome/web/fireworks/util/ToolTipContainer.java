package org.reactome.web.fireworks.util;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.AbsolutePanel;
import org.reactome.web.fireworks.events.FireworksZoomEvent;
import org.reactome.web.fireworks.events.NodeHoverEvent;
import org.reactome.web.fireworks.events.NodeHoverResetEvent;
import org.reactome.web.fireworks.handlers.FireworksZoomHandler;
import org.reactome.web.fireworks.handlers.NodeHoverHandler;
import org.reactome.web.fireworks.handlers.NodeHoverResetHandler;
import org.reactome.web.fireworks.model.FireworksStatus;
import org.reactome.web.fireworks.model.Node;

import java.util.Objects;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class ToolTipContainer extends AbsolutePanel implements NodeHoverHandler, NodeHoverResetHandler,
        FireworksZoomHandler {

    private static final int DELAY = 100;
    private static final double ZOOM_THRESHOLD = 17.5;
    private static FireworksStyleFactory.FireworksStyle style = FireworksStyleFactory.getFireworksStyle();


    private EventBus eventBus;

    private Object hovered;
    private double factor;

    private Timer hoveredTimer;
    private Timer infoTimer; //For messages that don't have to last long on the viewport

    private int width;
    private int height;

    public ToolTipContainer(EventBus eventBus, int width, int height) {
        this.eventBus = eventBus;
        this.hoveredTimer = new Timer() {
            @Override
            public void run() {
                showTooltipExecute();
            }
        };
        setWidth(width);
        setHeight(height);
        initHandlers();
    }

    private void initHandlers() {
        this.eventBus.addHandler(NodeHoverEvent.TYPE, this);
        this.eventBus.addHandler(NodeHoverResetEvent.TYPE, this);
        this.eventBus.addHandler(FireworksZoomEvent.TYPE, this);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    @Override
    public void onFireworksZoomChanged(FireworksZoomEvent event) {
        FireworksStatus status =  event.getStatus();
        if(status!=null){
            factor = status.getFactor();
            showTooltip();
        }
    }

    @Override
    public void onNodeHover(NodeHoverEvent event) {
        Object node =  event.getNode();
        if(node!=null) {
            setHovered(node);
        }
    }


    @Override
    public void onNodeHoverReset() {
        setHovered(null);
    }

    public void setWidth(int width) {
        setWidth(width + "px");
        this.width = width;
    }

    public void setHeight(int height) {
        setHeight(height + "px");
        this.height = height;
    }

    private void setHovered(Object hovered){
        if(Objects.equals(this.hovered, hovered)) return;
        if (this.hoveredTimer.isRunning()) {
            this.hoveredTimer.cancel();
        }
        this.hovered = hovered;
        if (this.hovered == null) {
            showTooltipExecute(); //this will quickly hide the tooltip ;)
        } else {
            this.hoveredTimer.schedule(DELAY);
        }
    }

    private void showTooltip() {
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                showTooltipExecute();
            }
        });
    }


    private void showTooltipExecute() {
        final Tooltip tooltip = Tooltip.getTooltip();

        if(infoTimer !=null) infoTimer.cancel();

        if (hovered == null || factor > ZOOM_THRESHOLD) {
            tooltip.hide();
        } else {
            if (hovered instanceof Node) {
                Node node = (Node) hovered;
                if (node.isTopLevel()) return;

                tooltip.setText(node.getName());

                int left = 0 ; int top = 0;
                int offsetX = (int) node.getX(); int offsetY = (int) node.getY();
                int size = (int) Math.ceil(node.getSize()) + 4;

                int dialogWidth = tooltip.getOffsetWidth();
                int dialogHeight = tooltip.getOffsetHeight();

                // Horizontal positioning
                if (offsetX > dialogWidth) {
                    left = offsetX - dialogWidth;                  // Place it left
                } else {
                    left = offsetX;                                // Place it right
                }

                // Vertical positioning
                if (offsetY > dialogHeight) {
                    top = offsetY - dialogHeight;                   // Place it top
                } else {
                    top = offsetY;                                  // Place it bottom
                }

                tooltip.setPositionAndShow(this, left, top, size);
            }
        }
    }
}
