package org.reactome.web.fireworks.events;

import com.google.gwt.event.shared.GwtEvent;
import org.reactome.web.fireworks.analysis.AnalysisType;
import org.reactome.web.fireworks.analysis.PathwayBase;
import org.reactome.web.fireworks.analysis.SpeciesFilteredResult;
import org.reactome.web.fireworks.handlers.AnalysisPerformedHandler;

import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class AnalysisPerformedEvent extends GwtEvent<AnalysisPerformedHandler> {
    public static Type<AnalysisPerformedHandler> TYPE = new Type<AnalysisPerformedHandler>();

    private String analysisType;
    private List<PathwayBase> pathways;

    public AnalysisPerformedEvent(SpeciesFilteredResult result) {
        this.analysisType = result.getType();
        this.pathways = result.getPathways();
    }

    @Override
    public Type<AnalysisPerformedHandler> getAssociatedType() {
        return TYPE;
    }

    public AnalysisType getAnalysisType() {
        return AnalysisType.getType(analysisType);
    }

    public List<PathwayBase> getPathways() {
        return pathways;
    }

    @Override
    protected void dispatch(AnalysisPerformedHandler handler) {
        handler.onAnalysisPerformed(this);
    }

    @Override
    public String toString() {
        return "AnalysisPerformedEvent{" +
                "analysisType='" + analysisType + '\'' +
                ", pathwaysHit=" + pathways.size() +
                '}';
    }
}
