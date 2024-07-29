package com.example.finalproj;
import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.swing.mxGraphComponent;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/*
* Displays a graph of method calls
* @author: Celine Ha */

public class MethodCallGraph {

    public static HashMap<String, ArrayList<String>> populateGraphDct(Path directoryPath) throws IOException {
        ArrayList<Path> arraypath = new ArrayList<>();
        DependencyGraph.getfiles(directoryPath, arraypath);
        String MethodName = null;

        HashMap<String, ArrayList<String>> methodcalls = new HashMap<>();

        for (Path path : arraypath) {

            try (Scanner scanner = new Scanner(path)) {
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine().trim();
                    if (line.length() >= 6) {
                        String isAFunction = line.substring(0, 6).trim();
                        if (isAFunction.equals("public") | isAFunction.equals("private")) {
                            ArrayList<String> methodNames = new ArrayList<>();
                            MethodName = getMethodName(line);
                            methodcalls.put(MethodName, methodNames);
                        } else if (MethodName != null) {
                            ArrayList<String> internalMethods = methodcalls.get(MethodName);
                            if (getInternalMethod(line) != null) {
                                internalMethods.add(getInternalMethod(line));
                            }
                        }

                    }
                }
            }
        }
        return methodcalls;
    }

    public static String getMethodName(String line) {
        String[] list = line.split(" ");
        for (String str : list) {
            if (str.contains("(")) {
                return str.substring(0, str.indexOf("(")).trim();
            }
        }
        return null;
    }

    public static String getInternalMethod(String line) {
        if (line.contains(".") && !line.contains("import") && !line.contains("*")) {
            String[] strLst = line.split("\\.");
            if (strLst.length != 0) {
                line = strLst[strLst.length - 1];
            }
            if (line.contains("(")) {
                return line.substring(0, line.indexOf("(")).trim();
            }
        }
        return null;
    }

    public static mxGraphComponent makeGraph(Path directoryPath) throws IOException {
        HashMap<String, ArrayList<String>> dct = populateGraphDct(directoryPath);

        SimpleGraph<String, DefaultEdge> graph = new SimpleGraph<>(DefaultEdge.class);

        HashMap<String, String> visited = new HashMap<>();

        for (String key : dct.keySet()) {
            if (!visited.containsKey(key) && key != null) {
                graph.addVertex(key);
                visited.put(key, key);
            }
            ArrayList<String> methodNames = dct.get(key);
            for (String methodName : methodNames) {
                if (!visited.containsKey(methodName)) {
                    graph.addVertex(methodName);
                    visited.put(methodName, null);
                }
                if (!key.equals(methodName)) { // Check for self-loops
                    graph.addEdge(key, methodName);
                }
            }
        }

        JGraphXAdapter<String, DefaultEdge> graphAdapter = new JGraphXAdapter<>(graph);
        mxCircleLayout layout = new mxCircleLayout(graphAdapter);
        layout.execute(graphAdapter.getDefaultParent());
        mxGraphComponent graphComponent = new mxGraphComponent(graphAdapter);
        return graphComponent;
    }

}

