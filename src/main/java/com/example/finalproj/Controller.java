package com.example.finalproj;

import com.mxgraph.swing.mxGraphComponent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.stage.DirectoryChooser;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.SwingUtilities;
import javafx.embed.swing.SwingNode;


public class Controller {
    @FXML
    private VBox fileViewer;

    @FXML
    private Button insertLinkButton;

    @FXML
    private TextField insertLink;
    @FXML
    private Text downloading;
    @FXML
    private Button uploadButton;
    @FXML
    private SwingNode dGraph;

    public void initialize() {
        updateTextVisibility();

        MainApplication.getDownloading().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                updateTextVisibility();
            }
        });
        MainApplication.getFolderPathProperty().addListener((observable, oldValue, newValue) -> {
            GitResources.getFileList().clear();
            GitResources.addFilesToList(MainApplication.getFolderPath());
            addToFolder();
        });
    }


    @FXML
    protected void onInsertButtonClick() throws IOException {
        GitResources.getFileList().clear();
        GitResources.downloadRepo(insertLink.getText().trim(), "./downloaded-files");
        MainApplication.setFolderPath("./downloaded-files");

//        HashMap<String, ArrayList<String>> dict = FileExplorer.functiondictionary("./downloaded-files");
//        for (String key : dict.keySet()) {
//            System.out.println(key + ": " + dict.get(key));
//        }
    }

    @FXML
    protected void updateTextVisibility() {
        System.out.println(MainApplication.isDownloading());
        downloading.setVisible(MainApplication.isDownloading());
    }

    @FXML
    private void openFileChooser() {
        DirectoryChooser dirChooser = new DirectoryChooser();
        dirChooser.setTitle("Select File");

        java.io.File file = dirChooser.showDialog(uploadButton.getScene().getWindow());

        if (file != null) {
            MainApplication.getFolderPathProperty().set(file.getAbsolutePath());
        }
    }

    @FXML
    protected void addToFolder() {
        fileViewer.getChildren().clear();
        for (String fileName : GitResources.getFileList()) {
            Button button = new Button(fileName);
            button.setPrefWidth(200);
            button.setOnAction(e -> handleButtonAction(fileName));
            fileViewer.getChildren().add(button);
        }
    }

    private void handleButtonAction(String label) {
        //fill in later! (Based on need)
    }

    public void displayDependencyGraph() {
        DependencyGraph generator = new DependencyGraph();
        try {
            mxGraphComponent graphComponent = generator.generateGraph(MainApplication.getFolderPath()+"/src");
            System.out.println("Graph generated successfully");

            createAndSetSwingContent(dGraph, graphComponent);

            System.out.println("Graph added to central pane");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createAndSetSwingContent(SwingNode swingNode, mxGraphComponent graphComponent) {
        SwingUtilities.invokeLater(() -> swingNode.setContent(graphComponent));
    }

}