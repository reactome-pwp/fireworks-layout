package org.reactome.web.fireworks.model;

import java.util.Set;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class Graph {
    Long speciesId;
    Set<Node> nodes;
    Set<Edge> edges;

    public Graph(Long speciesId, Set<Node> nodes, Set<Edge> edges) {
        this.speciesId = speciesId;
        this.nodes = nodes;
        this.edges = edges;
    }

    public Long getSpeciesId() {
        return speciesId;
    }

    public Set<Node> getNodes() {
        return nodes;
    }

    public Set<Edge> getEdges() {
        return edges;
    }

    public double getMaxX(){
        double max = 0;
        for (Node node : nodes) {
            max = Math.max(max, node.getMaxX());
        }
        for (Edge edge : edges) {
            max = Math.max(max, edge.getMaxX());
        }
        return max;
    }

    public double getMaxY(){
        double max = 0;
        for (Node node : nodes) {
            max = Math.max(max, node.getMaxY());
        }
        for (Edge edge : edges) {
            max = Math.max(max, edge.getMaxY());
        }
        return max;
    }

}
