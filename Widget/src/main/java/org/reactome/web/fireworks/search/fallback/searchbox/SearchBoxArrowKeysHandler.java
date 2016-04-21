package org.reactome.web.fireworks.search.fallback.searchbox;

import com.google.gwt.event.shared.EventHandler;

/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public interface SearchBoxArrowKeysHandler extends EventHandler {
    void onKeysPressed(SearchBoxArrowKeysEvent event);
}
