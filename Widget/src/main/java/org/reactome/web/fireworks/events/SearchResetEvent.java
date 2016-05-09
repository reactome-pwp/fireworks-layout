package org.reactome.web.fireworks.events;

import com.google.gwt.event.shared.GwtEvent;
import org.reactome.web.fireworks.handlers.SearchResetHandler;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class SearchResetEvent extends GwtEvent<SearchResetHandler> {
    public static final Type<SearchResetHandler> TYPE = new Type<>();

    @Override
    public Type<SearchResetHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(SearchResetHandler handler) {
        handler.onSearchReset(this);
    }

    @Override
    public String toString() {
        return "SearchResetEvent{}";
    }
}
