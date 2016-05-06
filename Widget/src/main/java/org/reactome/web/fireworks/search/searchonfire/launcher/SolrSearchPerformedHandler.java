package org.reactome.web.fireworks.search.searchonfire.launcher;

import com.google.gwt.event.shared.EventHandler;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface SolrSearchPerformedHandler extends EventHandler {
    void onSolrSearchPerformed(SolrSearchPerformedEvent event);
}
