package org.reactome.web.fireworks.search.events;


import com.google.gwt.event.shared.GwtEvent;
import org.reactome.web.fireworks.search.handlers.PanelCollapsedHandler;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class PanelCollapsedEvent extends GwtEvent<PanelCollapsedHandler> {
    public static Type<PanelCollapsedHandler> TYPE = new Type<>();

    @Override
    public Type<PanelCollapsedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(PanelCollapsedHandler handler) {
        handler.onPanelCollapsed(this);
    }

}
