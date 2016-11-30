package org.reactome.web.fireworks.events;

import com.google.gwt.event.shared.GwtEvent;
import org.reactome.web.fireworks.handlers.NodeFlaggedHandler;
import org.reactome.web.fireworks.model.Node;

import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class NodeFlaggedEvent extends GwtEvent<NodeFlaggedHandler> {
    public static final Type<NodeFlaggedHandler> TYPE = new Type<>();

    List<Node> flagged;

    public NodeFlaggedEvent(List<Node> flagged) {
        this.flagged = flagged;
    }

    @Override
    public Type<NodeFlaggedHandler> getAssociatedType() {
        return TYPE;
    }

    public List<Node> getFlagged() {
        return flagged;
    }

    @Override
    protected void dispatch(NodeFlaggedHandler event) {
        event.onNodeFlagged(this);
    }

    @Override
    public String toString() {
        return "NodeFlaggedEvent{" +
                "flagged=" + flagged.size() +
                '}';
    }
}
