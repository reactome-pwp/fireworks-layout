package org.reactome.web.fireworks.handlers;

import com.google.gwt.event.shared.EventHandler;
import org.reactome.web.fireworks.events.MouseScrollEvent;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface MouseScrollEventHandler extends EventHandler {

    void onMouseScrolled(MouseScrollEvent event);

}
