package com.example.finalproj;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.swing.mxGraphComponent;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

/** displays a graph of dependencies
 * @author: Celine Ha
 **/


public class DependencyGraph {

    public mxGraphComponent generateGraph(String directoryPath) throws IOException {
        Path directory = Paths.get(directoryPath);

        SimpleGraph<String, DefaultEdge> graph = new SimpleGraph<>(DefaultEdge.class);

        HashMap<String, String> visited = new HashMap<>();

        ArrayList<Path> arraypath = new ArrayList<>();
        getfiles(directory, arraypath);

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(directory)) {
            for (Path file : arraypath) {
                graph.addVertex(file.getFileName().toString());
                try (Scanner scanner = new Scanner(file)) {
                    while (scanner.hasNextLine()) {
                        String line = scanner.nextLine().trim();
                        if (line.length() >= 6) {
                            String checkString = line.substring(0, 6);
                            if (checkString.equals("import")) {
                                String library = line.substring(6);
                                if (visited.containsKey(library)) {
                                    graph.addEdge(file.getFileName().toString(), library);
                                } else {
                                    visited.put(library, file.getFileName().toString());
                                    graph.addVertex(library);
                                    graph.addEdge(file.getFileName().toString(), library);

                                }
                            }
                        }
                    }
                }
            }

        }
        JGraphXAdapter<String, DefaultEdge> graphAdapter = new JGraphXAdapter<>(graph);
        mxCircleLayout layout = new mxCircleLayout(graphAdapter);
        layout.execute(graphAdapter.getDefaultParent());
        mxGraphComponent graphComponent = new mxGraphComponent(graphAdapter);
        return graphComponent;

    }


    public static void getfiles(Path directoryPath, ArrayList<Path> arraypath) throws IOException {
        DirectoryStream<Path> stream = Files.newDirectoryStream(directoryPath);

        for (Path path : stream) {
            if (path.getFileName().toString().endsWith(".java")) {
                arraypath.add(path);
            } else {
                if (Files.isDirectory(path)) {
                    getfiles(path, arraypath);
                }
            }
        }
    }
}