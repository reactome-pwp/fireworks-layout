package org.reactome.web.fireworks.events;

import com.google.gwt.event.shared.GwtEvent;
import org.reactome.web.fireworks.handlers.NodeHoverEventHandler;
import org.reactome.web.fireworks.model.Node;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class NodeHoverEvent extends GwtEvent<NodeHoverEventHandler> {
    public static Type<NodeHoverEventHandler> TYPE = new Type<NodeHoverEventHandler>();

    private Node node;

    public NodeHoverEvent(Node node) {
        this.node = node;
    }

    @Override
    public Type<NodeHoverEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(NodeHoverEventHandler handler) {
        handler.onNodeHover(this);
    }

    public Node getNode() {
        return node;
    }

    @Override
    public String toString() {
        return "NodeHoverEvent{" +
                "node=" + node +
                '}';
    }
}
