package org.reactome.web.fireworks.handlers;

import com.google.gwt.event.shared.EventHandler;
import org.reactome.web.fireworks.events.CanvasExportRequestedEvent;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface CanvasExportRequestedHandler extends EventHandler {

    void onDiagramExportRequested(CanvasExportRequestedEvent event);

}
