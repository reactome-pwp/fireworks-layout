package org.reactome.web.fireworks.events;

import com.google.gwt.event.shared.GwtEvent;
import org.reactome.web.fireworks.handlers.NodeSelectedEventHandler;
import org.reactome.web.fireworks.model.Node;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class NodeSelectedEvent extends GwtEvent<NodeSelectedEventHandler> {
    public static Type<NodeSelectedEventHandler> TYPE = new Type<NodeSelectedEventHandler>();

    private Node node;

    public NodeSelectedEvent(Node node) {
        this.node = node;
    }

    @Override
    public Type<NodeSelectedEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(NodeSelectedEventHandler handler) {
        handler.onNodeSelected(this);
    }

    public Node getNode() {
        return node;
    }

    @Override
    public String toString() {
        return "NodeSelectedEvent{" +
                "node=" + node +
                '}';
    }
}
