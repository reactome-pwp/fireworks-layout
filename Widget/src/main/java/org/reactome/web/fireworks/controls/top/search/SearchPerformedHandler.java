package org.reactome.web.fireworks.controls.top.search;

import com.google.gwt.event.shared.EventHandler;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface SearchPerformedHandler extends EventHandler {
    void onSearchPerformed(SearchPerformedEvent event);
}
