package org.reactome.web.fireworks.events;

import com.google.gwt.event.shared.GwtEvent;
import org.reactome.web.fireworks.handlers.NodeSelectedResetEventHandler;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class NodeSelectedResetEvent extends GwtEvent<NodeSelectedResetEventHandler> {
    public static Type<NodeSelectedResetEventHandler> TYPE = new Type<NodeSelectedResetEventHandler>();

    @Override
    public Type<NodeSelectedResetEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(NodeSelectedResetEventHandler handler) {
        handler.onNodeSelectionReset();
    }

    @Override
    public String toString(){
        return "NodeSelectedResetEvent";
    }
}
