package org.reactome.web.fireworks.search.searchonfire.handlers;

import com.google.gwt.event.shared.EventHandler;
import org.reactome.web.fireworks.search.searchonfire.events.IncludeAllFormsEvent;

/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public interface IncludeAllFormsHandler extends EventHandler {
    void onIncludeAllForms(IncludeAllFormsEvent event);
}
