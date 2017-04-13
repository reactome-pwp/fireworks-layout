package org.reactome.server.fireworks.output;

import org.reactome.server.fireworks.model.GraphNode;

import java.util.Collection;
import java.util.HashSet;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class Graph {

    Long speciesId;
    String speciesName;

    Collection<Node> nodes;
    Collection<Edge> edges;

    public Graph(GraphNode graph){
        this.speciesId = graph.getDbId();
        this.speciesName = graph.getName();

        this.nodes = new HashSet<>(graph.getNodes());
        this.edges = new HashSet<>(graph.getEdges());
    }

    public Long getSpeciesId() {
        return speciesId;
    }

    public String getSpeciesName() {
        return speciesName;
    }

    public Collection<Node> getNodes() {
        return nodes;
    }

    public Collection<Edge> getEdges() {
        return edges;
    }


}
