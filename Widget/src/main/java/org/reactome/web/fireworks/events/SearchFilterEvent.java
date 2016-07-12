package org.reactome.web.fireworks.events;

import com.google.gwt.event.shared.GwtEvent;
import org.reactome.web.fireworks.handlers.SearchFilterHandler;
import org.reactome.web.fireworks.search.searchonfire.graph.model.GraphEntry;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class SearchFilterEvent extends GwtEvent<SearchFilterHandler> {
    public static final Type<SearchFilterHandler> TYPE = new Type<>();

    private GraphEntry[] graphEntries;

    public SearchFilterEvent(GraphEntry[] graphEntries) {
        this.graphEntries = graphEntries;
    }

    @Override
    public Type<SearchFilterHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(SearchFilterHandler handler) {
        handler.onSearchFilterEvent(this);
    }

    public GraphEntry[] getResult() {
        return graphEntries;
    }

    @Override
    public String toString() {
        return "SearchFilterEvent{" +
                "numberOfElementsFiltered=" + graphEntries.length +
                '}';
    }
}
