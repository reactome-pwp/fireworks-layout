package org.reactome.web.fireworks.search.searchonfire.events;

import com.google.gwt.event.shared.GwtEvent;
import org.reactome.web.fireworks.search.searchonfire.handlers.SolrSuggestionSelectedHandler;
import org.reactome.web.pwp.model.client.classes.DatabaseObject;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class SolrSuggestionSelectedEvent extends GwtEvent<SolrSuggestionSelectedHandler> {
    public static Type<SolrSuggestionSelectedHandler> TYPE = new Type<>();

    private DatabaseObject selection;

    public SolrSuggestionSelectedEvent(DatabaseObject selection) {
        this.selection = selection;
    }

    @Override
    public Type<SolrSuggestionSelectedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(SolrSuggestionSelectedHandler handler) {
        handler.onSuggestionSelected(this);
    }

    public DatabaseObject getSelectedEntry() {
        return selection;
    }
}
