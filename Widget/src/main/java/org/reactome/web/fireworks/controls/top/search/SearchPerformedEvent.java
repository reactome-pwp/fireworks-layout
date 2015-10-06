package org.reactome.web.fireworks.controls.top.search;

import com.google.gwt.event.shared.GwtEvent;
import org.reactome.web.fireworks.model.Node;

import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class SearchPerformedEvent extends GwtEvent<SearchPerformedHandler> {
    public static Type<SearchPerformedHandler> TYPE = new Type<>();

    private List<Node> suggestions;

    public SearchPerformedEvent(List<Node> suggestions) {
        this.suggestions = suggestions;
    }

    @Override
    public Type<SearchPerformedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(SearchPerformedHandler handler) {
        handler.onSearchPerformed(this);
    }

    public List<Node> getSuggestions() {
        return suggestions;
    }
}
