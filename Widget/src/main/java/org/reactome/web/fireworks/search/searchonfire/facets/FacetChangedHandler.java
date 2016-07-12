package org.reactome.web.fireworks.search.searchonfire.facets;

import com.google.gwt.event.shared.EventHandler;

/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public interface FacetChangedHandler extends EventHandler {
    void onFacetChanged(FacetChangedEvent event);
}
