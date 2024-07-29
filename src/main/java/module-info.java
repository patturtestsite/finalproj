module com.example.finalproj {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.swing;

    requires org.kordamp.bootstrapfx.core;
    requires org.json;
    requires okhttp3;
    requires com.fasterxml.jackson.databind;
    requires java.datatransfer;
    requires com.github.vlsi.mxgraph.jgraphx;
    requires org.jgrapht.ext;
    requires java.desktop;
    requires org.eclipse.jgit;

    opens com.example.finalproj to javafx.fxml;
    exports com.example.finalproj;
}