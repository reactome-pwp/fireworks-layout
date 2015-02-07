package org.reactome.web.fireworks.profiles.model;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface Profile {

    public String getName();
    public ProfileColour getNode();
    public ProfileColour getEdge();
    public ProfileColour getThumbnail();

}
