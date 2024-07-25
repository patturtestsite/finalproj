package com.example.finalproj;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Scanner;

import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.swing.mxGraphComponent;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

/**
 * @author: Celine Ha
 **/


public class DependencyGraph {

    public mxGraphComponent generateGraph(String directoryPath) throws IOException
    {

        // create a Path object for the specified directory
        Path directory = Paths.get(directoryPath);

        //create a simple graph object
        SimpleGraph<String, DefaultEdge> graph = new SimpleGraph<>(DefaultEdge.class);

        //create a visited dictionary
        HashMap<String, String> visited = new HashMap<>();

        // use DirectoryStream to list files which are present in specific
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(directory)) {

            //with forEach loop get all the path of files present in directory
            for (Path file : stream) {
                System.out.println(file.getFileName());
                graph.addVertex(file.getFileName().toString());
                //obtain the import statements and call makeNode for each import statement (?)
                try (Scanner scanner = new Scanner(file)){
                    while (scanner.hasNextLine()){
                        String line = scanner.nextLine().trim();

                        if (line.length() >= 6){
                            String checkString = line.substring(0,6);
                            if (checkString.equals("import")){
                                String library = line.substring(6);
                                if (visited.containsKey(library)){
                                    graph.addEdge(file.getFileName().toString(),library);
                                }
                                else{
                                    //add the key to the dict
                                    visited.put(library, file.getFileName().toString());
                                    //add vertex
                                    graph.addVertex(library);
                                    //add edge
                                    graph.addEdge(file.getFileName().toString(),library);

                                }
                                System.out.println(line); //call makeNode
                            }
                        }
                    }
                }
            }

        }
        // Create a visualization using JGraphX
        JGraphXAdapter<String, DefaultEdge> graphAdapter = new JGraphXAdapter<>(graph);

        // Setup a layout for the graph
        mxCircleLayout layout = new mxCircleLayout(graphAdapter);
        layout.execute(graphAdapter.getDefaultParent());

        // Create a graph component
        mxGraphComponent graphComponent = new mxGraphComponent(graphAdapter);

        return graphComponent;

    }
}