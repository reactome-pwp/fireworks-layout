package org.reactome.web.fireworks.profiles;

import org.reactome.web.fireworks.util.ColorGradient;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class StandardFireworksProfile extends FireworksProfile {

    ColorGradient nodeEnrichmentGradient;
    ColorGradient edgeEnrichmentGradient;

    ColorGradient nodeExpressionGradient;
    ColorGradient edgeExpressionGradient;

    public StandardFireworksProfile() {
        try {
            this.nodeEnrichmentGradient = new ColorGradient("#934A00", "#FFAD33");
            this.edgeEnrichmentGradient = new ColorGradient("#A96E33", "#FFB547");

            this.nodeExpressionGradient = new ColorGradient("#934A00", "#FFAD33");
            this.edgeExpressionGradient = new ColorGradient("#A96E33", "#FFB547");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getNodeEnrichmentColour(double p) {
        if(p<=0.05) {
            return this.nodeEnrichmentGradient.getColor(p/0.05);
        }else{
            return this.getNodeEnrichmentColour();
        }
    }

    @Override
    public String getNodeEnrichmentColour() {
        return "#FFB2B2";
    }

    @Override
    public String getEdgeEnrichmentColour(double p) {
        if(p<=0.05) {
            return this.edgeEnrichmentGradient.getColor(p/0.05);
        }else{
            return getEdgeEnrichmentColour();
        }
    }

    @Override
    public String getEdgeEnrichmentColour() {
        return "#FFBABA";
    }

    @Override
    public String getNodeExpressionColour(double expression, double min, double max) {
        return this.nodeExpressionGradient.getColor(expression, min, max);
    }

    @Override
    public String getEdgeExpressionColour(double expression, double min, double max) {
        return this.edgeExpressionGradient.getColor(expression, min, max);
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
        return "#FFE4E1";
    }

    @Override
    public String getEdgeFadeoutColour() {
        return "#FFECEC";
    }

    @Override
    public String getThumbnailEnrichmentColour(double p) {
        if(p<=0.05) {
            return this.edgeEnrichmentGradient.getColor(p/0.05);
        }else{
            return "#F0D4D1";
        }
    }

    @Override
    public String getThumbnailFadeoutColour() {
        return "#FFECEC";
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
