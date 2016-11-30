package org.reactome.web.fireworks.handlers;

import com.google.gwt.event.shared.EventHandler;
import org.reactome.web.fireworks.events.NodeFlaggedEvent;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface NodeFlaggedHandler extends EventHandler {

    void onNodeFlagged(NodeFlaggedEvent event);

}
