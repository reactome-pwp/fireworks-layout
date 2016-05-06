package org.reactome.web.fireworks.controls.common.pager;

import com.google.gwt.event.shared.EventHandler;

/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public interface PageChangedHandler extends EventHandler {
    void onPageChanged(PageChangedEvent event);
}

