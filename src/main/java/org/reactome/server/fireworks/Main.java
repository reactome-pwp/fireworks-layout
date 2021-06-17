package org.reactome.server.fireworks;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.martiansoftware.jsap.*;
import org.reactome.server.fireworks.config.ReactomeNeo4jConfig;
import org.reactome.server.fireworks.layout.Bursts;
import org.reactome.server.fireworks.layout.FireworksLayout;
import org.reactome.server.fireworks.output.Graph;
import org.reactome.server.fireworks.utils.GraphNode;
import org.reactome.server.fireworks.utils.OrthologyGraphFactory;
import org.reactome.server.fireworks.utils.ReactomeGraphNodeFactory;
import org.reactome.server.graph.domain.model.Species;
import org.reactome.server.graph.service.SpeciesService;
import org.reactome.server.graph.utils.ReactomeGraphCore;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Main {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) throws JSAPException {

        // Program Arguments -h, -p, -u, -k
        SimpleJSAP jsap = new SimpleJSAP(Main.class.getName(), "Connect to Reactome Graph Database",
                new Parameter[]{
                        new FlaggedOption("host", JSAP.STRING_PARSER, "bolt://localhost:7687", JSAP.NOT_REQUIRED, 'h', "host", "The neo4j host")
                        , new FlaggedOption("user", JSAP.STRING_PARSER, "neo4j", JSAP.NOT_REQUIRED, 'u', "user", "The neo4j user")
                        , new FlaggedOption("password", JSAP.STRING_PARSER, "neo4j", JSAP.REQUIRED, 'k', "password", "The neo4j password")
                        , new FlaggedOption("folder", JSAP.STRING_PARSER, JSAP.NO_DEFAULT, JSAP.REQUIRED, 'f', "folder", "The folder where the configuration file are stored.")
                        , new FlaggedOption("output", JSAP.STRING_PARSER, JSAP.NO_DEFAULT, JSAP.REQUIRED, 'o', "output", "The folder where the results are written to.")
                        , new FlaggedOption("species", JSAP.STRING_PARSER, "ALL", JSAP.NOT_REQUIRED, 's', "species", "Species Fireworks to layout (default: all). Use '_' instead of ' ' (example: homo_sapiens)")
                        , new QualifiedSwitch("verbose", JSAP.STRING_PARSER, JSAP.NO_DEFAULT, JSAP.NOT_REQUIRED, 'v', "verbose", "Requests verbose output.")
                }
        );

        JSAPResult config = jsap.parse(args);
        if (jsap.messagePrinted()) System.exit(1);

        //Initialising ReactomeCore Neo4j configuration
        ReactomeGraphCore.initialise(config.getString("host"),
                config.getString("user"),
                config.getString("password"),
                ReactomeNeo4jConfig.class);

        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        String outputDir = config.getString("output");

        File directory = new File(config.getString("folder"));
        if (!directory.exists()) {
            System.err.println(config.getString("folder") + " is not a valid folder");
            System.exit(1);
        }

        SpeciesService speciesService = ReactomeGraphCore.getService(SpeciesService.class);
        List<Species> speciesList = speciesService.getSpecies();
        Species mainSpecies = speciesList.remove(0);

        String homoSapiens = getBurstsFileName(directory, mainSpecies.getDisplayName());
        if (!(new File(homoSapiens)).exists()) {
            System.err.println(homoSapiens + " is MANDATORY");
            System.exit(1);
        }

        System.out.println(mainSpecies.getDisplayName() + "...");
        ReactomeGraphNodeFactory graphNodeFactory = new ReactomeGraphNodeFactory(mainSpecies);
        GraphNode main = graphNodeFactory.getGraphNode();
        Bursts bursts = getBurstsConfiguration(directory, mainSpecies.getDisplayName());
        FireworksLayout layout = new FireworksLayout(bursts, main);
        layout.doLayout();
        saveSerialisedGraphNode(main, outputDir);

        boolean speciesSpecified = !config.getString("species").equals("ALL");
        for (Species species : speciesList) {
            if (speciesSpecified) {
                String aux = species.getDisplayName().replaceAll(" ", "_").toLowerCase();
                if (!aux.equals(config.getString("species").toLowerCase())) continue;
            }
            System.out.println("\n" + species.getDisplayName() + "...");
            graphNodeFactory = new ReactomeGraphNodeFactory(species);
            GraphNode node = graphNodeFactory.getGraphNode();

            if (speciesSpecified) {
                String aux = node.getName().replaceAll(" ", "_").toLowerCase();
                if (!aux.equals(config.getString("species").toLowerCase())) continue;
            }

            bursts = getBurstsConfiguration(directory, node.getName());
            if (bursts != null) {
                layout = new FireworksLayout(bursts, node);
                layout.doLayout();
            }
            OrthologyGraphFactory ogf = new OrthologyGraphFactory(main, node);
            saveSerialisedGraphNode(ogf.getGraph(), outputDir);
        }

        System.out.println("\nFirework layout finished.");

        System.exit(0);
    }

    private static Bursts getBurstsConfiguration(File directory, String speciesName) {
        String fileName = getBurstsFileName(directory, speciesName);
        try {
            return mapper.readValue(new File(fileName), Bursts.class);
        } catch (Exception e) {
            System.out.println(fileName + " could not be found");
        }
        return null;
    }

    private static String getBurstsFileName(File directory, String speciesName) {
        String fileName = speciesName.replaceAll(" ", "_") + "_bursts.json";
        return directory.getAbsolutePath() + File.separator + fileName;
    }

    private static void saveSerialisedGraphNode(GraphNode graphNode, String dir) {
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