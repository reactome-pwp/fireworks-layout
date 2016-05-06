package org.reactome.web.fireworks.search.fallback.launcher;

import com.google.gwt.event.shared.GwtEvent;
import org.reactome.web.fireworks.model.Node;

import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class SearchPerformedEvent extends GwtEvent<SearchPerformedHandler> {
    public static Type<SearchPerformedHandler> TYPE = new Type<>();

    private String term;
    private List<Node> suggestions;

    public SearchPerformedEvent(String term, List<Node> suggestions) {
        this.term = term;
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

    public String getTerm() {
        return term;
    }

    public List<Node> getSuggestions() {
        return suggestions;
    }
}
