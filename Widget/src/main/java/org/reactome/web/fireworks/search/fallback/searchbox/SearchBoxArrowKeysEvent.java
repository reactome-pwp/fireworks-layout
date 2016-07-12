package org.reactome.web.fireworks.search.fallback.searchbox;

import com.google.gwt.event.shared.GwtEvent;

/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public class SearchBoxArrowKeysEvent extends GwtEvent<SearchBoxArrowKeysHandler> {
    public static Type<SearchBoxArrowKeysHandler> TYPE = new Type<>();

    private Integer value;

    public SearchBoxArrowKeysEvent(Integer value) {
        this.value = value;
    }

    @Override
    public Type<SearchBoxArrowKeysHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(SearchBoxArrowKeysHandler handler) {
        handler.onKeysPressed(this);
    }

    public Integer getValue() {
        return value;
    }
}
