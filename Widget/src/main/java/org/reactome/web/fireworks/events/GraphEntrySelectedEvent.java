package org.reactome.web.fireworks.events;

import com.google.gwt.event.shared.GwtEvent;
import org.reactome.web.fireworks.handlers.GraphEntrySelectedHandler;
import org.reactome.web.fireworks.search.searchonfire.graph.model.GraphEntry;

/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public class GraphEntrySelectedEvent extends GwtEvent<GraphEntrySelectedHandler> {
    public static Type<GraphEntrySelectedHandler> TYPE = new Type<>();

    private GraphEntry selectedEntry;

    public GraphEntrySelectedEvent(GraphEntry selectedEntry) {
        this.selectedEntry = selectedEntry;
    }

    @Override
    public Type<GraphEntrySelectedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(GraphEntrySelectedHandler handler) {
        handler.onGraphEntrySelected(this);
    }

    public GraphEntry getSelectedEntry() {
        return selectedEntry;
    }

    @Override
    public String toString() {
        return "GraphEntrySelectedEvent{" +
                "selectedEntry=" + selectedEntry.getStId() +
                '}';
    }
}
