package org.reactome.web.fireworks.data;

import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface RawGraph {
    Long getSpeciesId();
    String getSpeciesName();
    List<RawNode> getNodes();
    List<RawEdge> getEdges();
}
