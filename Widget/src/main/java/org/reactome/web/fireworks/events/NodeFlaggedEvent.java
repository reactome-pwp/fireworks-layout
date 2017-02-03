package org.reactome.web.fireworks.events;

import com.google.gwt.event.shared.GwtEvent;
import org.reactome.web.fireworks.handlers.NodeFlaggedHandler;
import org.reactome.web.fireworks.model.Node;

import java.util.Collection;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class NodeFlaggedEvent extends GwtEvent<NodeFlaggedHandler> {
    public static final Type<NodeFlaggedHandler> TYPE = new Type<>();

    String term;
    Collection<Node> flagged;

    public NodeFlaggedEvent(String term, Collection<Node> flagged) {
        this.term = term;
        this.flagged = flagged;
    }

    @Override
    public Type<NodeFlaggedHandler> getAssociatedType() {
        return TYPE;
    }

    public Collection<Node> getFlagged() {
        return flagged;
    }

    public String getTerm() {
        return term;
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
