package org.reactome.web.fireworks.events;

import com.google.gwt.event.shared.GwtEvent;
import org.reactome.web.fireworks.handlers.NodeFlagRequestedHandler;

/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public class NodeFlagRequestedEvent extends GwtEvent<NodeFlagRequestedHandler> {
    public static final Type<NodeFlagRequestedHandler> TYPE = new Type<>();

    String term;

    public NodeFlagRequestedEvent(String term) {
        this.term = term;
    }

    @Override
    public Type<NodeFlagRequestedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(NodeFlagRequestedHandler handler) {
        handler.onNodeFlagRequested(this);
    }

    public String getTerm() {
        return term;
    }

    @Override
    public String toString() {
        return "NodeFlagRequestedEvent{" +
                "term='" + term + '\'' +
                '}';
    }
}
