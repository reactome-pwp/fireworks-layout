package org.reactome.web.fireworks.events;

import com.google.gwt.event.shared.GwtEvent;
import org.reactome.web.fireworks.handlers.NodeHoverResetEventHandler;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class NodeHoverResetEvent extends GwtEvent<NodeHoverResetEventHandler> {
    public static Type<NodeHoverResetEventHandler> TYPE = new Type<NodeHoverResetEventHandler>();

    @Override
    public Type<NodeHoverResetEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(NodeHoverResetEventHandler handler) {
        handler.onNodeHoverReset();
    }

    @Override
    public String toString(){
        return "NodeHoverResetEvent";
    }
}
