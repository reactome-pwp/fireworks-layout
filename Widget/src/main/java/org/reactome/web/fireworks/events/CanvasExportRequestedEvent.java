package org.reactome.web.fireworks.events;

import com.google.gwt.event.shared.GwtEvent;
import org.reactome.web.fireworks.handlers.CanvasExportRequestedHandler;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class CanvasExportRequestedEvent extends GwtEvent<CanvasExportRequestedHandler> {
    public static Type<CanvasExportRequestedHandler> TYPE = new Type<>();

    @Override
    public Type<CanvasExportRequestedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(CanvasExportRequestedHandler handler) {
        handler.onDiagramExportRequested(this);
    }

    @Override
    public String toString() {
        return "DiagramRequestEvent{}";
    }
}
