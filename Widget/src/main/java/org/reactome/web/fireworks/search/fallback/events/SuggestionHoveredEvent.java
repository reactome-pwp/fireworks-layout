package org.reactome.web.fireworks.search.fallback.events;

import com.google.gwt.event.shared.GwtEvent;
import org.reactome.web.fireworks.model.Node;
import org.reactome.web.fireworks.search.fallback.handlers.SuggestionHoveredHandler;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class SuggestionHoveredEvent extends GwtEvent<SuggestionHoveredHandler> {
    public static Type<SuggestionHoveredHandler> TYPE = new Type<>();

    private Node node;
    private Boolean toFocus = Boolean.FALSE;

    public SuggestionHoveredEvent(Node databaseObject) {
        this.node = databaseObject;
    }

    public SuggestionHoveredEvent(Node databaseObject, Boolean toFocus) {
        this.node = databaseObject;
        this.toFocus = toFocus;
    }

    @Override
    public Type<SuggestionHoveredHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(SuggestionHoveredHandler handler) {
        handler.onSuggestionHovered(this);
    }

    public Node getHoveredObject() {
        return node;
    }

    public Boolean getToFocus() {
        return toFocus;
    }

    @Override
    public String toString() {
        return "SuggestionHoveredEvent{" +
                "node=" + node +
                ", toFocus=" + toFocus +
                '}';
    }
}
