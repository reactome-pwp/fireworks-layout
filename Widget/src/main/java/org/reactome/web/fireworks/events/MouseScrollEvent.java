package org.reactome.web.fireworks.events;

import com.google.gwt.event.shared.GwtEvent;
import org.reactome.web.fireworks.handlers.MouseScrollEventHandler;
import org.reactome.web.fireworks.util.Coordinate;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class MouseScrollEvent extends GwtEvent<MouseScrollEventHandler> {
    public static Type<MouseScrollEventHandler> TYPE = new Type<MouseScrollEventHandler>();

    private int delta;
    Coordinate mouseCoordinate;

    public MouseScrollEvent(int delta, Coordinate mouseCoordinate) {
        this.delta = delta;
        this.mouseCoordinate = mouseCoordinate;
    }

    @Override
    public Type<MouseScrollEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(MouseScrollEventHandler mouseScrollEventHandler) {
        mouseScrollEventHandler.onMouseScrolled(this);
    }

    public Coordinate getMouseCoordinate() {
        return mouseCoordinate;
    }

    public int getDelta() {
        return delta;
    }

    @Override
    public String toString() {
        return "MouseScrollEvent{" +
                "delta=" + delta +
                ", mouseCoordinate=" + mouseCoordinate +
                '}';
    }
}
