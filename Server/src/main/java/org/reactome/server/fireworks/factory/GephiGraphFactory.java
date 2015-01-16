package org.reactome.server.fireworks.factory;

import org.gephi.graph.api.*;
import org.gephi.project.api.ProjectController;
import org.openide.util.Lookup;
import org.reactome.server.fireworks.exporter.GraphExporter;
import org.reactome.server.fireworks.model.GraphNode;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
@Deprecated
public class GephiGraphFactory {

    private GraphModel graphModel;
    private Graph graph;

    public GephiGraphFactory(GraphNode reactomeGraph) {
        ProjectController pc = Lookup.getDefault().lookup(ProjectController.class);
        pc.newProject();
        pc.getCurrentWorkspace();
        this.graphModel = Lookup.getDefault().lookup(GraphController.class).getModel();
        this.graph = graphModel.getGraph();
        for (GraphNode child : reactomeGraph.getChildren()) {
            getNode(child);
        }

        String file = "/Users/amundo/fireworks/Fireworks_test"; //No extension here
        GraphExporter.export2PDF(file);
//        GraphExporter.export2SVG(file);

        pc.closeCurrentWorkspace();
        pc.closeCurrentProject();
    }

    public Graph getGraph() {
        return graph;
    }

    private Node getNode(GraphNode graph){
        Node node = graphModel.factory().newNode(graph.getName());
        node.getNodeData().setColor(1f, 0f, 0f);
        node.getNodeData().setX((float) graph.getX());
        node.getNodeData().setY((float) graph.getY());
        node.getNodeData().setSize(graph.getSize().floatValue());
        this.graph.addNode(node);
        for (GraphNode child : graph.getChildren()) {
            Node target = getNode(child);
            Edge edge = this.graphModel.factory().newEdge(node, target, 0.1f, true);
            edge.getEdgeData().setColor(0f, 0f, 0f);
            this.graph.addEdge(edge);
        }
        return node;
    }
}
