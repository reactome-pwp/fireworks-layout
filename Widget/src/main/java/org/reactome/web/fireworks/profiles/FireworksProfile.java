package org.reactome.web.fireworks.profiles;

import net.auroris.ColorPicker.client.Color;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public abstract class FireworksProfile {
    public abstract String getNodeEnrichmentColour(double p);
    public abstract String getNodeEnrichmentColour();
    public abstract String getEdgeEnrichmentColour(double p);
    public abstract String getEdgeEnrichmentColour();

    public abstract String getNodeHighlightColour();
    public abstract String getEdgeHighlightColour();


    public abstract String getNodeSelectionColour();
    public abstract String getEdgeSelectionColour();

    public abstract String getNodeStandardColour();
    public abstract String getEdgeStandardColour();

    public abstract String getNodeFadeoutColour();
    public abstract String getEdgeFadeoutColour();

    public abstract String getThumbnailEnrichmentColour(double p);
    public abstract String getThumbnailFadeoutColour();
    public abstract String getThumbnailHighlightColour();
    public abstract String getThumbnailSelectionColour();
    public abstract String getThumbnailStandardColour();

    @Deprecated
    String getColor(String color, double p){
        Color rtn = new Color();
        try {
            rtn.setHex(color.replace("#", ""));
        } catch (Exception e) {
            return color;
        }
        try {
            int s = (int) Math.round( (0.5 + p) * 100 );
            int v = (int) Math.round( (0.75 + p) * 100 );
//            rtn.setHSV(rtn.getHue(), s, v);
            rtn.setHSV(rtn.getHue(), s, rtn.getValue());
        } catch (Exception e) {
            return color;
        }
        return "#" + rtn.getHex();
    }
}
