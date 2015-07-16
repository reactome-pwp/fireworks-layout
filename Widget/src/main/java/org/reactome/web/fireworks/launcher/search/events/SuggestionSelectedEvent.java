package org.reactome.web.fireworks.launcher.search.events;

import com.google.gwt.event.shared.GwtEvent;
import org.reactome.web.fireworks.launcher.search.handlers.SuggestionSelectedHandler;
import org.reactome.web.fireworks.model.Node;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class SuggestionSelectedEvent extends GwtEvent<SuggestionSelectedHandler> {
    public static Type<SuggestionSelectedHandler> TYPE = new Type<>();

    private Node node;
    private Boolean toOpen = Boolean.FALSE;

    public SuggestionSelectedEvent(Node databaseObject) {
        this.node = databaseObject;
    }

    public SuggestionSelectedEvent(Node databaseObject, Boolean toExpand) {
        this.node = databaseObject;
        this.toOpen = toExpand;
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

    public Boolean getToOpen() {
        return toOpen;
    }

    @Override
    public String toString() {
        return "SuggestionSelectedEvent{" +
                "node=" + node +
                ", toOpen=" + toOpen +
                '}';
    }
}
