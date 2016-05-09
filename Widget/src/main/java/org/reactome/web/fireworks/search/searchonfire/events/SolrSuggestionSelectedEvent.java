package org.reactome.web.fireworks.search.searchonfire.events;

import com.google.gwt.event.shared.GwtEvent;
import org.reactome.web.fireworks.search.searchonfire.handlers.SolrSuggestionSelectedHandler;
import org.reactome.web.fireworks.search.searchonfire.solr.model.Entry;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class SolrSuggestionSelectedEvent extends GwtEvent<SolrSuggestionSelectedHandler> {
    public static Type<SolrSuggestionSelectedHandler> TYPE = new Type<>();

    private Entry selectedEntry;

    public SolrSuggestionSelectedEvent(Entry selectedEntry) {
        this.selectedEntry = selectedEntry;
    }

    @Override
    public Type<SolrSuggestionSelectedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(SolrSuggestionSelectedHandler handler) {
        handler.onSuggestionSelected(this);
    }

    public Entry getSelectedEntry() {
        return selectedEntry;
    }
}
