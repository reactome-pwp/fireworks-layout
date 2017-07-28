package org.reactome.web.fireworks.handlers;

import com.google.gwt.event.shared.EventHandler;
import org.reactome.web.fireworks.events.NodeFlagRequestedEvent;

/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public interface NodeFlagRequestedHandler extends EventHandler {
    void onNodeFlagRequested(NodeFlagRequestedEvent event);
}
