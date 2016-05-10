package org.reactome.web.fireworks.search.searchonfire.infopanel;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.shared.EventBus;
import org.reactome.web.fireworks.events.GraphEntryHoveredEvent;
import org.reactome.web.fireworks.events.GraphEntrySelectedEvent;
import org.reactome.web.fireworks.search.searchonfire.graph.model.GraphEntry;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */

abstract class InfoActionsHelper {

    static ClickHandler getLinkClickHandler(final GraphEntry entry, final EventBus eventBus, final Object source) {
        return event -> eventBus.fireEventFromSource(new GraphEntrySelectedEvent(entry), source);
    }

    static MouseOutHandler getLinkMouseOut(final EventBus eventBus, final Object source) {
        return event -> eventBus.fireEventFromSource(new GraphEntryHoveredEvent(null), source);
    }

    static MouseOverHandler getLinkMouseOver(final GraphEntry entry, final EventBus eventBus, final Object source) {
        return event -> eventBus.fireEventFromSource(new GraphEntryHoveredEvent(entry), source);
    }
}