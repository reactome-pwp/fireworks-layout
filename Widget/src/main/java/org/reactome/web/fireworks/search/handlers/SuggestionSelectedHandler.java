package org.reactome.web.fireworks.search.handlers;

import com.google.gwt.event.shared.EventHandler;
import org.reactome.web.fireworks.search.events.SuggestionSelectedEvent;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface SuggestionSelectedHandler extends EventHandler {
    void onSuggestionSelected(SuggestionSelectedEvent event);
}
