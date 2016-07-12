package org.reactome.web.fireworks.search.searchonfire.events;

import com.google.gwt.event.shared.GwtEvent;
import org.reactome.web.fireworks.search.searchonfire.handlers.IncludeAllFormsHandler;

/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public class IncludeAllFormsEvent extends GwtEvent<IncludeAllFormsHandler> {
    public static Type<IncludeAllFormsHandler> TYPE = new Type<>();

    private boolean includeAllForms;

    public IncludeAllFormsEvent(boolean includeAllForms) {
        this.includeAllForms = includeAllForms;
    }

    public boolean getIncludeAllForms() {
        return includeAllForms;
    }

    @Override
    public Type<IncludeAllFormsHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(IncludeAllFormsHandler handler) {
        handler.onIncludeAllForms(this);
    }
}
