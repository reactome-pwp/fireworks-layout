package org.reactome.web.fireworks.handlers;

import com.google.gwt.event.shared.EventHandler;
import org.reactome.web.fireworks.events.IllustrationSelectedEvent;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface IllustrationSelectedHandler extends EventHandler {
    void onIllustrationSelected(IllustrationSelectedEvent event);
}
