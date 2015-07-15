package org.reactome.web.fireworks.launcher.search.events;

import com.google.gwt.event.shared.GwtEvent;
import org.reactome.web.fireworks.launcher.search.handlers.SuggestionSelectedHandler;
import org.reactome.web.fireworks.model.Node;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class SuggestionSelectedEvent extends GwtEvent<SuggestionSelectedHandler> {
    public static Type<SuggestionSelectedHandler> TYPE = new Type<SuggestionSelectedHandler>();

    private Node node;

    public SuggestionSelectedEvent(Node databaseObject) {
        this.node = databaseObject;
    }

    @Override
    public Type<SuggestionSelectedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(SuggestionSelectedHandler handler) {
        handler.onSuggestionSelected(this);
    }

    public Node getSelectedObject() {
        return node;
    }

    @Override
    public String toString() {
        return "SuggestionSelectedEvent{" +
                ", selected=" + node +
                '}';
    }
}
