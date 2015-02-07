package org.reactome.web.fireworks.profiles.model;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface ProfileColour {

    public String getInitial();
    public String getHighlight();
    public String getSelection();
    public String getFadeout();
    public String getHit();

    public ProfileGradient getEnrichment();
    public ProfileGradient getExpression();
}
