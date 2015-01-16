package org.reactome.server.fireworks.builder;

import org.gephi.graph.api.*;
import org.gephi.layout.plugin.AutoLayout;
import org.gephi.layout.plugin.forceAtlas.ForceAtlasLayout;
import org.gephi.project.api.ProjectController;
import org.openide.util.Lookup;
import org.reactome.server.analysis.core.data.AnalysisData;
import org.reactome.server.analysis.core.model.*;
import org.reactome.server.fireworks.exporter.GraphExporter;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
@Deprecated
public class GraphBuilder {

    private static final int SIZE_FACTOR = 50;

    private AnalysisData analysisData;
    private GraphModel graphModel;
    private Graph graph;

    public GraphBuilder(AnalysisData analysisData) {
        this.analysisData = analysisData;
        ProjectController pc = Lookup.getDefault().lookup(ProjectController.class);
        pc.newProject();
        pc.getCurrentWorkspace();
        this.graphModel = Lookup.getDefault().lookup(GraphController.class).getModel();
        this.graph = graphModel.getGraph();
        createGraph();
        pc.closeCurrentWorkspace();
        pc.closeCurrentProject();
    }

//    public org.reactome.server.fireworks.output.Graph getGraph(){
//        return new org.reactome.server.fireworks.output.Graph(this.graph);
//    }

    public void print(){
        int i = 0;
        for (Node node : graph.getNodes()) {
            i++;
            System.out.println(node.getNodeData().getLabel() + " -> " + node.getNodeData().x() + ", " + node.getNodeData().y());
        }
        System.out.println("Nodes: " + i);
    }

    private void createGraph(){
        SpeciesNode species = SpeciesNodeFactory.getHumanNode();
        PathwayHierarchy hierarchy = analysisData.getPathwayHierarchies().get(species);

        List<PathwayRoot> tlps =  getSortedPathwayRootList(hierarchy.getChildren());
        Double factor = 0d;
        for (PathwayRoot tlp : tlps) {
            factor += tlp.getPathwayNodeData().getEntitiesRatio();
        }
        factor = 1d / factor;
        System.out.println("Factor: " + factor);

//        Node reactome = graphModel.factory().newNode("Reactome");
//        {
//            NodeData nd = reactome.getNodeData();
//            nd.setLabel("Reactome");
//            nd.setSize(tlps.get(tlps.size() - 1).getPathwayNodeData().getEntitiesRatio().floatValue() * SIZE_FACTOR);
//            nd.setX(0);
//            nd.setY(0);
//            nd.setFixed(true);
//            graph.addNode(reactome);
//        }

        float _2PI_ = (float) (2 * Math.PI);
        Double ratio = 0d;
        for (PathwayRoot pNode : tlps) {
            Double aux = pNode.getPathwayNodeData().getEntitiesRatio() * factor / 2d;
            ratio += aux;
            float angle = _2PI_ * ratio.floatValue();
            ratio += aux;
            Node node = this.getOrCreateNode(pNode);
//            Edge edge = graphModel.factory().newEdge(reactome, node);
//            graph.addEdge(edge);
            float x = SIZE_FACTOR * (float) Math.cos(angle);
            float y = SIZE_FACTOR * (float) Math.sin(angle);
            NodeData nd = node.getNodeData();
            nd.setX(x); nd.setY(y);
            nd.setFixed(true);
            addGraphNode(node, pNode, angle);
        }
        System.out.println(ratio);

        AutoLayout autoLayout = new AutoLayout(40, TimeUnit.MINUTES);
        autoLayout.setGraphModel(graphModel);

        ForceAtlasLayout forceAtlas = new ForceAtlasLayout(null);
        //Next three setting are to avoid node overlapping
        forceAtlas.setAdjustSizes(true);
        forceAtlas.setRepulsionStrength(1000d);
        forceAtlas.setFreezeStrength(500d);
        autoLayout.addLayout(forceAtlas, 1f);

//        forceAtlas.setGravity(0d);
//        forceAtlasLayout.setOutboundAttractionDistribution(true);
//        layout.setSpeed(2d);
//        forceAtlasLayout.setGravity(15d);

//        AutoLayout.DynamicProperty gravity = AutoLayout.createDynamicProperty("Gravity", new Double[]{80., 400.0}, new float[]{0f, 1f}, AutoLayout.Interpolation.LINEAR);
//        AutoLayout.DynamicProperty speed = AutoLayout.createDynamicProperty("Speed", new Double[]{1.2, 0.3}, new float[]{0f, 1f}, AutoLayout.Interpolation.LINEAR);
//        AutoLayout.DynamicProperty repulsion = AutoLayout.createDynamicProperty("Repulsion strength", new Double[]{3000.0, 6000.}, new float[]{0f, 1f}, AutoLayout.Interpolation.LINEAR);
//        AutoLayout.DynamicProperty freeze = AutoLayout.createDynamicProperty("Autostab Strength", new Double(100.0), 0f);
//        autoLayout.addLayout(forceAtlasLayout, 0.8f, new AutoLayout.DynamicProperty[]{gravity, speed, repulsion, freeze});


//        YifanHuProportional builder = new YifanHuProportional();
//        YifanHuLayout yifanHuLayout = builder.buildLayout();
//        autoLayout.addLayout(yifanHuLayout, 1f);

        autoLayout.execute();

        String file = "/Users/amundo/fireworks/StarrySky"; //No extension here
        GraphExporter.export2PDF(file);
        GraphExporter.export2SVG(file);
    }

    private void addGraphNode(Node parent, PathwayNode pNode, float angle){
        for (PathwayNode pChild : pNode.getChildren()) {
            Node child = this.getOrCreateNode(pChild);

            float x = 3 * SIZE_FACTOR * (float) Math.cos(angle);
            float y = 3 * SIZE_FACTOR * (float) Math.sin(angle);
            NodeData nd = child.getNodeData();
            nd.setX(x); nd.setY(y);

            Edge edge = graphModel.factory().newEdge(parent, child);
            graph.addEdge(edge);
            addGraphNode(child, pChild, angle);
        }
    }

    private Node getOrCreateNode(PathwayNode pNode){
        Node node = this.graph.getNode(pNode.getStId());
        if(node==null){
            node = this.graphModel.factory().newNode(pNode.getStId());
            Double ratio = pNode.getPathwayNodeData().getEntitiesRatio();
            node.getAttributes().setValue("dbId", pNode.getPathwayId());
            node.getAttributes().setValue("stId", pNode.getStId());
            node.getAttributes().setValue("ratio", ratio);
            node.getNodeData().setSize(ratio.floatValue() * SIZE_FACTOR);
            node.getNodeData().setLabel(pNode.getName());
            this.graph.addNode(node);
        }
        return node;
    }

    private List<PathwayRoot> getSortedPathwayRootList(Set<PathwayRoot> nodes){
        List<PathwayRoot> rtn = new LinkedList<PathwayRoot>();
        for (PathwayRoot pathwayRoot : nodes) {
            rtn.add(pathwayRoot);
        }
        Collections.sort(rtn, new Comparator<PathwayRoot>() {
            @Override
            public int compare(PathwayRoot o1, PathwayRoot o2) {
                return o1.getName().compareTo(o2.getName());
//                return o1.getPathwayNodeData().getEntitiesRatio().compareTo(o2.getPathwayNodeData().getEntitiesRatio());
            }
        });
        Collections.reverse(rtn);
        return rtn;
    }
}
