package org.reactome.web.fireworks.model;

import org.reactome.web.analysis.client.model.PathwayBase;
import org.reactome.web.analysis.client.model.SpeciesFilteredResult;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class FireworksData {

    private Graph graph;
    private Map<Long, Node> id2Node;
    private Map<String, Node> stId2Node;

    private SpeciesFilteredResult analysisResult;

    public FireworksData(Graph graph) {
        this.graph = graph;
        this.id2Node = new HashMap<>();
        this.stId2Node = new HashMap<>();
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

    public String getSpeciesName(){
        return this.graph.getSpeciesName();
    }

    public void resetPathwaysAnalysisResult(){
        this.analysisResult = null;
        for (Node node : this.graph.getNodes()) {
            node.initStatistics();
        }
    }

    public void setPathwaysAnalysisResult(SpeciesFilteredResult result) {
        this.analysisResult = result;

        for (Node node : this.graph.getNodes()) {
            node.setFadeoutColour();
        }
        for (Edge edge : this.graph.edges) {
            edge.setFadeoutColour();
        }
        for (PathwayBase pathway : result.getPathways()) {
            Node node = id2Node.get(pathway.getDbId());
            if(node!=null){
                node.setAnalysisResultData(result, pathway.getEntities());
            }
        }
    }

    public void setPathwaysFilteredResult(Collection<Node> nodes){
        for (Node node : graph.getNodes()) {
            node.setTransparency(0.1);
        }
        for (Edge edge : graph.getEdges()) {
            edge.setTransparency(0.1);
        }
        for (Node node : nodes) {
            node.setTransparency(1.0);
            for (Edge edge : node.getEdgesTo()) {
                edge.setTransparency(1.0);
            }
            for (Node ancestor : node.getAncestors()) {
                ancestor.setTransparency(1.0);
                for (Edge edge : ancestor.getEdgesTo()) {
                    edge.setTransparency(1.0);
                }
            }
        }
    }

    public void resetPathwaysFiltered(){
        for (Node node : graph.getNodes()) {
            node.setTransparency(1.0);
        }
        for (Edge edge : graph.getEdges()) {
            edge.setTransparency(1.0);
        }
    }

    public void updateColours(){
        if(this.analysisResult!=null){
            setPathwaysAnalysisResult(this.analysisResult);
        }
    }
}
