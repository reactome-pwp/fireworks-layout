package org.reactome.web.fireworks.model;

import org.reactome.web.fireworks.analysis.PathwayBase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class FireworksData {

    private Graph graph;
    private Map<Long, Node> id2Node;
    private Map<String, Node> stId2Node;

    public FireworksData(Graph graph) {
        this.graph = graph;
        this.id2Node = new HashMap<Long, Node>();
        this.stId2Node = new HashMap<String, Node>();
        for (Node node : graph.getNodes()) {
            this.id2Node.put(node.getDbId(), node);
            this.stId2Node.put(node.getStId(), node);
        }
    }

    public Node getNode(Long identifier){
        if(identifier==null) return null;
        return this.id2Node.get(identifier);
    }

    public Node getNode(String identifier){
        if(identifier==null) return null;
        return this.stId2Node.get(identifier);
    }

    public Long getSpeciesId(){
        return this.graph.getSpeciesId();
    }

    public void resetPathwaysAnalysisResult(){
        for (Node node : this.graph.getNodes()) {
            node.setStatistics(null);
        }
    }

    public void setPathwaysAnalysisResult(List<PathwayBase> pathways) {
        for (PathwayBase pathway : pathways) {
            Node node = id2Node.get(pathway.getDbId());
            if(node!=null){
                node.setStatistics(pathway.getEntities());
            }
        }
    }

}
