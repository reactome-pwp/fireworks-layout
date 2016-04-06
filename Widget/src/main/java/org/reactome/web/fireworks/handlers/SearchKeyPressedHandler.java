package org.reactome.web.fireworks.handlers;

import com.google.gwt.event.shared.EventHandler;
import org.reactome.web.fireworks.events.SearchKeyPressedEvent;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface SearchKeyPressedHandler extends EventHandler {
    void onSearchKeyPressed(SearchKeyPressedEvent event);
}
