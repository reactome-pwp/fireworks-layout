package org.reactome.web.fireworks.profiles;

import org.reactome.web.fireworks.util.gradient.ThreeColorGradient;
import org.reactome.web.fireworks.util.gradient.TwoColorGradient;

/**
 * Profile suggested by Henning Hermjakob
 *
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
class FireworksProfile01 extends FireworksProfile {

    private static final Double THRESHOLD = 0.05;

    TwoColorGradient nodeEnrichmentGradient;
    TwoColorGradient edgeEnrichmentGradient;

    ThreeColorGradient nodeExpressionGradient;
    ThreeColorGradient edgeExpressionGradient;

    public FireworksProfile01() {
        try {
            this.nodeEnrichmentGradient = new TwoColorGradient("#A0A0A0", "#000000");
            this.edgeEnrichmentGradient = new TwoColorGradient("#B0B0B0", "#303030");

            this.nodeExpressionGradient = new ThreeColorGradient("#00FF00", "#000000", "#FF0000");
            this.edgeExpressionGradient = new ThreeColorGradient("#6FFF6F", "#6F6F6F", "#FF6F6F");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getNodeEnrichmentColour(double p) {
        if(p<=THRESHOLD) {
            return this.nodeEnrichmentGradient.getColor(p/0.05);
        }else{
            return this.getNodeEnrichmentColour();
        }
    }

    @Override
    public String getNodeEnrichmentColour() {
        return "#E0E0E0";
    }

    @Override
    public String getEdgeEnrichmentColour(double p) {
        if(p<=THRESHOLD) {
            return this.edgeEnrichmentGradient.getColor(p/0.05);
        }else{
            return getEdgeEnrichmentColour();
        }
    }

    @Override
    public String getEdgeEnrichmentColour() {
        return "#E0E0E0";
    }

    @Override
    public String getNodeExpressionColour(double p, double expression, double min, double max) {
        if(p<=THRESHOLD) {
            return this.nodeExpressionGradient.getColor(expression, min, max);
        }else{
            return this.getNodeEnrichmentColour();
        }
    }

    @Override
    public String getNodeExpressionColour(double p) {
        return this.nodeExpressionGradient.getColor(p);
    }

    @Override
    public String getEdgeExpressionColour(double p, double expression, double min, double max) {
        if(p<=THRESHOLD) {
            return this.edgeExpressionGradient.getColor(expression, min, max);
        }else{
            return getEdgeEnrichmentColour();
        }
    }

    @Override
    public String getNodeHighlightColour() {
        return "#E1ED55";
    }

    @Override
    public String getEdgeHighlightColour() {
        return "#E1ED55";
    }

    @Override
    public String getNodeSelectionColour() {
        return "#BBBBFF";
    }

    @Override
    public String getEdgeSelectionColour() {
        return "#EEEEFF";
    }

    @Override
    public String getNodeStandardColour() {
        return "#FF0000";
    }

    @Override
    public String getEdgeStandardColour() {
        return "#FFCCCC";
    }

    @Override
    public String getNodeFadeoutColour() {
        return "#F8F8F8";
    }

    @Override
    public String getEdgeFadeoutColour() {
        return "#F8F8F8";
    }

    @Override
    public String getThumbnailHighlightColour() {
        return "#0A0A0A";
    }

    @Override
    public String getThumbnailSelectionColour() {
        return "#0000FF";
    }

    @Override
    public String getThumbnailStandardColour() {
        return "#FFCCCC";
    }

}
