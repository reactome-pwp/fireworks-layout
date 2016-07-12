package org.reactome.web.fireworks.search.fallback.searchbox;

import com.google.gwt.event.shared.GwtEvent;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class SearchBoxUpdatedEvent extends GwtEvent<SearchBoxUpdatedHandler> {
    public static Type<SearchBoxUpdatedHandler> TYPE = new Type<>();

    private String value;

    public SearchBoxUpdatedEvent(String value) {
        this.value = value != null ? value.trim() : value;
    }

    @Override
    public Type<SearchBoxUpdatedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(SearchBoxUpdatedHandler handler) {
        handler.onSearchUpdated(this);
    }

    public String getValue() {
        return value;
    }
}
