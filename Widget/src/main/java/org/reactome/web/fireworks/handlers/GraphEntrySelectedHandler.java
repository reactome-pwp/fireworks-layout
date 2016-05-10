package org.reactome.web.fireworks.handlers;

import com.google.gwt.event.shared.EventHandler;
import org.reactome.web.fireworks.events.GraphEntrySelectedEvent;

/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public interface GraphEntrySelectedHandler extends EventHandler{
    void onGraphEntrySelected(GraphEntrySelectedEvent event);
}
