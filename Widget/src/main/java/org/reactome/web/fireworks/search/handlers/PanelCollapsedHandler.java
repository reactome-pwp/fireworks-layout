package org.reactome.web.fireworks.search.handlers;

import com.google.gwt.event.shared.EventHandler;
import org.reactome.web.fireworks.search.events.PanelCollapsedEvent;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface PanelCollapsedHandler extends EventHandler {
    void onPanelCollapsed(PanelCollapsedEvent event);
}
