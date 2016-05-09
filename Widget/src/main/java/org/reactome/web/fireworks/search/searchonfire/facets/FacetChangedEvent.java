package org.reactome.web.fireworks.search.searchonfire.facets;

import com.google.gwt.event.shared.GwtEvent;
import org.reactome.web.fireworks.search.searchonfire.solr.model.SolrSearchResult;

/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public class FacetChangedEvent extends GwtEvent<FacetChangedHandler> {
    public static Type<FacetChangedHandler> TYPE = new Type<>();
    private SolrSearchResult results;

    public FacetChangedEvent(SolrSearchResult results) {
        this.results = results;
    }

    @Override
    public Type<FacetChangedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(FacetChangedHandler handler) {
        handler.onFacetChanged(this);
    }

    public SolrSearchResult getResults() {
        return results;
    }
}
