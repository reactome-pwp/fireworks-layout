package org.reactome.server.fireworks;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.martiansoftware.jsap.*;
import org.gk.persistence.MySQLAdaptor;
import org.reactome.core.controller.GKInstance2ModelObject;
import org.reactome.core.factory.DatabaseObjectFactory;
import org.reactome.server.analysis.core.data.AnalysisData;
import org.reactome.server.analysis.core.model.PathwayHierarchy;
import org.reactome.server.analysis.core.model.SpeciesNode;
import org.reactome.server.analysis.core.model.SpeciesNodeFactory;
import org.reactome.server.fireworks.factory.OrthologyGraphFactory;
import org.reactome.server.fireworks.factory.ReactomeGraphNodeFactory;
import org.reactome.server.fireworks.layout.Bursts;
import org.reactome.server.fireworks.layout.FireworksLayout;
import org.reactome.server.fireworks.model.GraphNode;
import org.reactome.server.fireworks.model.Graphs;
import org.reactome.server.fireworks.output.Graph;
import org.reactome.server.fireworks.util.GraphUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class Main {
    private static ObjectMapper mapper = new ObjectMapper();


    public static void main(String[] args) throws Exception {
        SimpleJSAP jsap = new SimpleJSAP(
                Main.class.getName(),
                "Provides a set of tools for the pathway analysis and species comparison",
                new Parameter[] {
                        new FlaggedOption("host", JSAP.STRING_PARSER, "localhost", JSAP.NOT_REQUIRED, 'h', "host",
                                "The database host")
                        , new FlaggedOption("database", JSAP.STRING_PARSER, JSAP.NO_DEFAULT, JSAP.REQUIRED, 'd', "database",
                        "The reactome database name to connect to")
                        , new FlaggedOption("username", JSAP.STRING_PARSER, JSAP.NO_DEFAULT, JSAP.REQUIRED, 'u', "username",
                        "The database user")
                        , new FlaggedOption("password", JSAP.STRING_PARSER, JSAP.NO_DEFAULT, JSAP.REQUIRED, 'p', "password",
                        "The password to connect to the database")
                        ,new FlaggedOption( "structure", JSAP.STRING_PARSER, JSAP.NO_DEFAULT, JSAP.REQUIRED, 's', "structure",
                        "The file containing the data structure for the analysis." )
//                        ,new FlaggedOption( "config", JSAP.STRING_PARSER, JSAP.NO_DEFAULT, JSAP.REQUIRED, 'c', "config",
//                        "The file containing the burst configuration." )
                        ,new FlaggedOption( "output", JSAP.STRING_PARSER, JSAP.NO_DEFAULT, JSAP.REQUIRED, 'o', "output",
                        "The file where the results are written to." )
                        ,new QualifiedSwitch( "verbose", JSAP.STRING_PARSER, JSAP.NO_DEFAULT, JSAP.NOT_REQUIRED, 'v', "verbose",
                        "Requests verbose output." )
                }
        );

        JSAPResult config = jsap.parse(args);
        if( jsap.messagePrinted() ) System.exit( 1 );

        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        MySQLAdaptor dba = new MySQLAdaptor(
                config.getString("host"),
                config.getString("database"),
                config.getString("username"),
                config.getString("password")
        );
        ApplicationContext context = new ClassPathXmlApplicationContext("config.xml");
        GKInstance2ModelObject converter = context.getBean(GKInstance2ModelObject.class);
        DatabaseObjectFactory.initializeFactory(dba, converter);

        String outputDir = config.getString("output");

        //TODO: This is a initial strategy that needs to be changed
        Graphs graphs;
        try {
            graphs = GraphUtils.getReactomeGraphs("/Users/amundo/fireworks/ReactomeGraphs.bin");
        }catch (Exception e){
            //Initializing Analysis Data  *** IMPORTANT ***
            String structure = config.getString("structure");
            AnalysisData analysisData = context.getBean(AnalysisData.class);
            analysisData.setFileName(structure);

            graphs = new Graphs();
            SpeciesNode humanNode = SpeciesNodeFactory.getHumanNode();
            PathwayHierarchy hierarchy = analysisData.getPathwayHierarchies().get(humanNode);
            ReactomeGraphNodeFactory reactomeGraphFactory = new ReactomeGraphNodeFactory(hierarchy);
            graphs.addGraphNode(reactomeGraphFactory.getGraphNode());

            for (SpeciesNode node : analysisData.getPathwayHierarchies().keySet()) {
                if(!node.equals(humanNode)){ // && !node.getSpeciesID().equals(176806L)){
                    hierarchy = analysisData.getPathwayHierarchies().get(node);
                    reactomeGraphFactory = new ReactomeGraphNodeFactory(hierarchy);
                    graphs.addGraphNode(reactomeGraphFactory.getGraphNode());
                }
            }
            GraphUtils.save(graphs, "/Users/amundo/fireworks/ReactomeGraphs.bin");
        }

        List<GraphNode> list = graphs.getGraphNodes();
        GraphNode main = list.remove(0);
        System.out.println(main.getName() + "...");
        Bursts bursts = getBurstsConfiguration(main.getName());
        FireworksLayout layout = new FireworksLayout(bursts, main);
        layout.doLayout();
        saveSerialisedGraphNode(main, outputDir);

        for (GraphNode node : list) {
            System.out.println("\n" + node.getName() + "...");
            bursts = getBurstsConfiguration(node.getName());
            if(bursts!=null){
                layout = new FireworksLayout(bursts, node);
                layout.doLayout();
            }
            OrthologyGraphFactory ogf = new OrthologyGraphFactory(main, node);
            saveSerialisedGraphNode(ogf.getGraph(), outputDir);
        }
        System.out.println("\nFirework layout finished.");

        System.exit( 0 );
    }

    private static Bursts getBurstsConfiguration(String speciesName){
        String fileName = speciesName.replaceAll(" ", "_")  + "_bursts.json";
        try {
            //Get file from resources folder
            ClassLoader classLoader = mapper.getClass().getClassLoader();
            //noinspection ConstantConditions
            File file = new File(classLoader.getResource(fileName).getFile());
            return mapper.readValue(file, Bursts.class);
        }catch (Exception e){
            System.err.println(fileName + " could not be found in 'resources'");
        }
        return null;
    }

    private static void saveSerialisedGraphNode(GraphNode graphNode, String dir){
        dir = dir.endsWith("/") ? dir : dir + "/";
        String fileName = dir + graphNode.getName().replaceAll(" ", "_") + ".json";
        try {
            mapper.writeValue(new File(fileName), new Graph(graphNode));
            System.out.println(fileName + " written.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
