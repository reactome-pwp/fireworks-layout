package org.reactome.web.fireworks.search.searchonfire.launcher;

import com.google.gwt.event.shared.GwtEvent;
import org.reactome.web.fireworks.search.searchonfire.solr.model.SolrSearchResult;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class SolrSearchPerformedEvent extends GwtEvent<SolrSearchPerformedHandler> {
    public static Type<SolrSearchPerformedHandler> TYPE = new Type<>();

    private SolrSearchResult suggestions;

    public SolrSearchPerformedEvent(SolrSearchResult suggestions) {
        this.suggestions = suggestions;
    }

    @Override
    public Type<SolrSearchPerformedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(SolrSearchPerformedHandler handler) {
        handler.onSolrSearchPerformed(this);
    }

    public SolrSearchResult getSuggestions() {
        return suggestions;
    }

}
