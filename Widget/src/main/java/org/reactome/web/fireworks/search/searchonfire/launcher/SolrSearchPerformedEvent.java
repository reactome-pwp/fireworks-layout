package org.reactome.web.fireworks.search.searchonfire.launcher;

import com.google.gwt.event.shared.GwtEvent;
import org.reactome.web.fireworks.search.searchonfire.solr.model.FireworksResult;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class SolrSearchPerformedEvent extends GwtEvent<SolrSearchPerformedHandler> {
    public static Type<SolrSearchPerformedHandler> TYPE = new Type<>();

    private FireworksResult suggestions;

    public SolrSearchPerformedEvent(FireworksResult suggestions) {
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

    public FireworksResult getSuggestions() {
        return suggestions;
    }

}
