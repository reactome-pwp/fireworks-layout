package org.reactome.web.fireworks.launcher.search.events;

import com.google.gwt.event.shared.GwtEvent;
import org.reactome.web.fireworks.launcher.search.handlers.PanelExpandedHandler;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class PanelExpandedEvent extends GwtEvent<PanelExpandedHandler> {
    public static Type<PanelExpandedHandler> TYPE = new Type<PanelExpandedHandler>();

    @Override
    public Type<PanelExpandedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(PanelExpandedHandler handler) {
        handler.onPanelExpanded(this);
    }

}
