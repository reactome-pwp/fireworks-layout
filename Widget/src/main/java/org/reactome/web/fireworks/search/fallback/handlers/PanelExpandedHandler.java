package org.reactome.web.fireworks.search.fallback.handlers;

import com.google.gwt.event.shared.EventHandler;
import org.reactome.web.fireworks.search.fallback.events.PanelExpandedEvent;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface PanelExpandedHandler extends EventHandler {
    void onPanelExpanded(PanelExpandedEvent event);
}
