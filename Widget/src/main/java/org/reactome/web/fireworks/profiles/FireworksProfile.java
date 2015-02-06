package org.reactome.web.fireworks.profiles;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
@SuppressWarnings("UnusedDeclaration")
public abstract class FireworksProfile {
    public abstract String getNodeEnrichmentColour(double p);
    public abstract String getNodeEnrichmentColour();

    public abstract String getEdgeEnrichmentColour(double p);
    public abstract String getEdgeEnrichmentColour();

    public abstract String getNodeExpressionColour(double p, double expression, double min, double max);
    public abstract String getNodeExpressionColour(double p);
    public abstract String getEdgeExpressionColour(double p, double expression, double min, double max);

    public abstract String getNodeHighlightColour();
    public abstract String getEdgeHighlightColour();


    public abstract String getNodeSelectionColour();
    public abstract String getEdgeSelectionColour();

    public abstract String getNodeStandardColour();
    public abstract String getEdgeStandardColour();

    public abstract String getNodeFadeoutColour();
    public abstract String getEdgeFadeoutColour();

    public abstract String getThumbnailHighlightColour();
    public abstract String getThumbnailSelectionColour();
    public abstract String getThumbnailStandardColour();


    private static FireworksProfile PROFILE;
    public static FireworksProfile getCurrentProfile(){
        if(PROFILE==null){
            PROFILE = new FireworksProfile01();
//            PROFILE = new StandardFireworksProfile();
        }
        return PROFILE;
    }
}
