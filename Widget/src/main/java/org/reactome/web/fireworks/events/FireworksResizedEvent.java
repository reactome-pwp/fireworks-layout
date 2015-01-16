package org.reactome.web.fireworks.events;

import com.google.gwt.event.shared.GwtEvent;
import org.reactome.web.fireworks.handlers.FireworksResizeEventHandler;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class FireworksResizedEvent extends GwtEvent<FireworksResizeEventHandler> {
    public static Type<FireworksResizeEventHandler> TYPE = new Type<FireworksResizeEventHandler>();

    private double width;
    private double height;

    public FireworksResizedEvent(double width, double height) {
//        super(sender);
        this.width = width;
        this.height = height;
    }

    @Override
    public Type<FireworksResizeEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(FireworksResizeEventHandler handler) {
        handler.onFireworksResized(this);
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    @Override
    public String toString() {
        return "FireworksResizedEvent{" +
                "width=" + width +
                ", height=" + height +
                '}';
    }
}
