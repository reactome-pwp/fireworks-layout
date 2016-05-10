package org.reactome.web.fireworks.handlers;

import com.google.gwt.event.shared.EventHandler;
import org.reactome.web.fireworks.events.GraphEntryHoveredEvent;

/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public interface GraphEntryHoveredHandler extends EventHandler {
    void onGraphEntryHovered(GraphEntryHoveredEvent event);
}
