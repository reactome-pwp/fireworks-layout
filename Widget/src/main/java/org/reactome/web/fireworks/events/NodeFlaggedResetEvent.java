package org.reactome.web.fireworks.events;

import com.google.gwt.event.shared.GwtEvent;
import org.reactome.web.fireworks.handlers.NodeFlaggedResetHandler;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class NodeFlaggedResetEvent  extends GwtEvent<NodeFlaggedResetHandler> {
    public final static Type<NodeFlaggedResetHandler> TYPE = new Type<>();

    @Override
    public Type<NodeFlaggedResetHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(NodeFlaggedResetHandler event) {
        event.onNodeFlaggedReset();
    }

    @Override
    public String toString() {
        return "NodeFlaggedResetEvent{}";
    }
}
