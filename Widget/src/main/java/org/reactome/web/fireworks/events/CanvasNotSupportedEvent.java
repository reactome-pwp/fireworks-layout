package org.reactome.web.fireworks.events;

import com.google.gwt.event.shared.GwtEvent;
import org.reactome.web.fireworks.handlers.CanvasNotSupportedEventHandler;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class CanvasNotSupportedEvent extends GwtEvent<CanvasNotSupportedEventHandler> {
    public static Type<CanvasNotSupportedEventHandler> TYPE = new Type<CanvasNotSupportedEventHandler>();

    final String message = "Your browser does not support the HTML5 Canvas. Please upgrade your browser.";

    @Override
    public Type<CanvasNotSupportedEventHandler> getAssociatedType() {
        return TYPE;
    }

    public String getMessage() {
        return message;
    }

    @Override
    protected void dispatch(CanvasNotSupportedEventHandler canvasNotSupportedEventHandler) {
        canvasNotSupportedEventHandler.onCanvasNotSupported(this);
    }

    @Override
    public String toString() {
        return "CanvasNotSupportedEvent{" +
                "message='" + message + '\'' +
                '}';
    }
}
