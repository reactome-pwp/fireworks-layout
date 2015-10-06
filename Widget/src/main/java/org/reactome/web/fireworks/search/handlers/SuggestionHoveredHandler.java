package org.reactome.web.fireworks.search.handlers;

import com.google.gwt.event.shared.EventHandler;
import org.reactome.web.fireworks.search.events.SuggestionHoveredEvent;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface SuggestionHoveredHandler extends EventHandler {
    void onSuggestionHovered(SuggestionHoveredEvent event);
}
