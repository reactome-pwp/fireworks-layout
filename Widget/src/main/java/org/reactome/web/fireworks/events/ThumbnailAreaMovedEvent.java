package org.reactome.web.fireworks.events;

import com.google.gwt.event.shared.GwtEvent;
import org.reactome.web.fireworks.handlers.ThumbnailAreaMovedEventHandler;
import org.reactome.web.fireworks.util.Coordinate;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class ThumbnailAreaMovedEvent extends GwtEvent<ThumbnailAreaMovedEventHandler> {
    public static Type<ThumbnailAreaMovedEventHandler> TYPE = new Type<ThumbnailAreaMovedEventHandler>();

    private Coordinate coordinate;

    public ThumbnailAreaMovedEvent(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    @Override
    public Type<ThumbnailAreaMovedEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(ThumbnailAreaMovedEventHandler handler) {
        handler.onThumbnailAreaMoved(this);
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    @Override
    public String toString() {
        return "ThumbnailAreaMovedEvent{" +
                "coordinate=" + coordinate +
                '}';
    }
}
