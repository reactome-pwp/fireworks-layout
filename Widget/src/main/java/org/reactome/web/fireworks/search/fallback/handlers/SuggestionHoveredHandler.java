package org.reactome.web.fireworks.search.fallback.handlers;

import com.google.gwt.event.shared.EventHandler;
import org.reactome.web.fireworks.search.fallback.events.SuggestionHoveredEvent;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface SuggestionHoveredHandler extends EventHandler {
    void onSuggestionHovered(SuggestionHoveredEvent event);
}
