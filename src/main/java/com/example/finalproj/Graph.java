package com.example.finalproj;

import com.mxgraph.swing.mxGraphComponent;

import java.io.IOException;

public interface Graph {
    public mxGraphComponent generateGraph(String directoryPath) throws IOException;
}


