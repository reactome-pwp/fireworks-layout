package org.reactome.web.fireworks.handlers;

import com.google.gwt.event.shared.EventHandler;
import org.reactome.web.fireworks.events.SearchFilterEvent;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface SearchFilterHandler extends EventHandler {
    void onSearchFilterEvent(SearchFilterEvent event);
}
