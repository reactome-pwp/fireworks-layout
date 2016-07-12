package org.reactome.web.fireworks.search.searchonfire.handlers;

import com.google.gwt.event.shared.EventHandler;
import org.reactome.web.fireworks.search.searchonfire.events.SolrSuggestionSelectedEvent;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface SolrSuggestionSelectedHandler extends EventHandler {
    void onSuggestionSelected(SolrSuggestionSelectedEvent event);
}
