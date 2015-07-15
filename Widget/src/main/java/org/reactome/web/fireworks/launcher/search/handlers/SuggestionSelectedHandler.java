package org.reactome.web.fireworks.launcher.search.handlers;

import com.google.gwt.event.shared.EventHandler;
import org.reactome.web.fireworks.launcher.search.events.SuggestionSelectedEvent;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface SuggestionSelectedHandler extends EventHandler {
    void onSuggestionSelected(SuggestionSelectedEvent event);
}
