package org.reactome.web.fireworks.events;

import com.google.gwt.event.shared.GwtEvent;
import org.reactome.web.fireworks.handlers.AnalysisResetEventHandler;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class AnalysisResetEvent extends GwtEvent<AnalysisResetEventHandler> {
    public static Type<AnalysisResetEventHandler> TYPE = new Type<AnalysisResetEventHandler>();

    @Override
    public Type<AnalysisResetEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(AnalysisResetEventHandler handler) {
        handler.onAnalysisReset();
    }

    @Override
    public String toString(){
        return "AnalysisResetEvent{}";
    }
}
