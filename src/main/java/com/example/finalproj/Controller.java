package com.example.finalproj;

import com.mxgraph.swing.mxGraphComponent;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.nio.file.Paths;
import javax.swing.SwingUtilities;
import javafx.embed.swing.SwingNode;


public class Controller {

    @FXML
    private VBox fileViewer;

    @FXML
    private TextField insertLink;

    @FXML
    private Button uploadButton;

    @FXML
    private VBox Visualization;

    @FXML
    private Text downloading;

    @FXML
    private String currMode = "fileExplorer";

    @FXML
    private SwingNode dGraph;

    @FXML
    private SwingNode mGraph;

    @FXML
    private VBox oPattern;

    @FXML
    private VBox cMetrics;

    @FXML
    private VBox cHistory;

    @FXML
    private VBox fExplorer;
    @FXML
    private VBox dGraphBox;
    @FXML
    private VBox mGraphBox;

    private void initialize() {
        MainApplication.getDownloading().addListener((observable, oldValue, newValue) -> showDownloading());
        MainApplication.getFolderPathProperty().addListener((observable, oldValue, newValue) -> {
            GitResources.getFileList().clear();
            GitResources.addFilesToList(newValue);
            refreshFileViewer();
        });
    }

    @FXML
    public void menuButtonHandler(ActionEvent event) throws IOException {
        for(Node n : Visualization.getChildren()) {
            n.setVisible(false);
        }
        Button clickedButton = (Button) event.getSource();
        String buttonId = clickedButton.getId();

        if(MainApplication.getFolderPath() == null || MainApplication.getFolderPath().equals("")) {
            Visualization.getChildren().add(new Label("Please upload a folder for use."));
        } else {
            switch (buttonId) {
                case "fileExplorer":
                    displayFileExplorer();
                    currMode = "fileExplorer";
                    break;
                case "commitHistory":
                    displayCommitHistory();
                    currMode = "commitHistory";
                    break;
                case "dependencyGraph":
                    displayDependencyGraph();
                    currMode = "dependencyGraph";
                    break;
                case "methodCallGraph":
                    displayMethodCallGraph();
                    currMode = "methodCallGraph";
                    break;
                case "observerPattern":
                    displayObserverPattern();
                    currMode = "observerPattern";
                    break;
                case "codeMetrics":
                    displayCodeMetrics();
                    currMode = "codeMetrics";
                    break;
                default:
                    break;
            }
        }
    }

    @FXML
    protected void onInsertButtonClick() throws IOException {
        GitResources.getFileList().clear();
        GitResources.downloadRepo(insertLink.getText().trim(), "./downloaded-files");
        MainApplication.setFolderPath("./downloaded-files");
        MainApplication.setRootFolder("./downloaded-files");
    }

    @FXML
    protected void showDownloading() {
        downloading.setVisible(MainApplication.isDownloading());
    }

    @FXML
    protected void displayCommitHistory() {
        cHistory.setVisible(true);
        cHistory.getChildren().clear();

        TableView<Commit> tableView = new TableView<>();

        TableColumn<Commit, String> commitCol = new TableColumn<>("Commit");
        commitCol.setCellValueFactory(new PropertyValueFactory<>("commit"));

        TableColumn<Commit, String> authorCol = new TableColumn<>("Author");
        authorCol.setCellValueFactory(new PropertyValueFactory<>("author"));

        TableColumn<Commit, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));

        TableColumn<Commit, String> messageCol = new TableColumn<>("Message");
        messageCol.setCellValueFactory(new PropertyValueFactory<>("message"));

        tableView.getColumns().addAll(commitCol, authorCol, dateCol, messageCol);

        try {
            List<String> commitStrings = GitResources.getCommitHistory();
            if (commitStrings.isEmpty()) {
                cHistory.getChildren().add(new Label("No commits found."));
            } else {
                ObservableList<Commit> commitList = FXCollections.observableArrayList();
                for (String commitString : commitStrings) {
                    String[] parts = commitString.split(", ", 4);
                    Commit commit = new Commit(parts[0].split(": ")[1],
                            parts[1].split(": ")[1],
                            parts[2].split(": ")[1],
                            parts[3].split(": ")[1]);
                    commitList.add(commit);
                }
                tableView.setItems(commitList);
                cHistory.getChildren().add(tableView);
            }
        } catch (IOException e) {
            cHistory.getChildren().add(new Label("Error retrieving commit history. " +
                    "Check that the selected directory contains a git file."));
            e.printStackTrace();
        }
    }

    @FXML
    protected void displayDependencyGraph() {
        dGraphBox.setVisible(true);
        dGraph.setVisible(true);
        DependencyGraph generator = new DependencyGraph();
        try {
            mxGraphComponent graphComponent = generator.generateGraph(MainApplication.getFolderPath());
            createAndSetSwingContent(dGraph, graphComponent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void displayFileExplorer() {
        fExplorer.setVisible(true);
        fExplorer.getChildren().clear();
        fExplorer.getChildren().add(new Label("Please select a file."));
    }

    @FXML
    public void displayCodeMetrics() {
        cMetrics.setVisible(true);
        cMetrics.getChildren().clear();
        cMetrics.getChildren().add(new Label("Please select a file."));
    }

    @FXML
    public void displayObserverPattern() throws IOException {
        oPattern.setVisible(true);
        List<File> files = ObserverPatternDetector.detectObserverPattern(new File(MainApplication.getFolderPath()));

        if (files.isEmpty()) {
            oPattern.getChildren().clear();
            oPattern.getChildren().add(new Label("Observer Pattern not detected."));
        } else {
            oPattern.getChildren().clear();
            oPattern.getChildren().add(new Label("Observer Pattern may be in use. These are the classes that " +
                    "may make use of it"));

            TableView<File> fileTable = new TableView<>();
            TableColumn<File, String> fileColumn = new TableColumn<>("Files");
            fileColumn.setCellValueFactory(cellData -> {
                File file = cellData.getValue();
                return new ReadOnlyObjectWrapper<>(file.getPath());
            });

            fileTable.getColumns().add(fileColumn);

            ObservableList<File> observableFiles = FXCollections.observableArrayList(files);
            fileTable.setItems(observableFiles);

            oPattern.getChildren().add(fileTable);
        }
    }

    @FXML
    public void displayMethodCallGraph() {
        mGraphBox.setVisible(true);
        mGraph.setVisible(true);
        try {
            mxGraphComponent graphComponent = MethodCallGraph.makeGraph(Paths.get(MainApplication.getFolderPath()));
            System.out.println("Graph generated successfully");

            createAndSetSwingContent(mGraph, graphComponent);

            System.out.println("Graph added to central pane");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createAndSetSwingContent(SwingNode swingNode, mxGraphComponent graphComponent) {
        SwingUtilities.invokeLater(() -> swingNode.setContent(graphComponent));
    }

    @FXML
    private void openFileChooser() {
        DirectoryChooser dirChooser = new DirectoryChooser();
        dirChooser.setTitle("Select Project Directory");

        File file = dirChooser.showDialog(uploadButton.getScene().getWindow());

        if (file != null) {
            MainApplication.setFolderPath(file.getAbsolutePath());
            MainApplication.setRootFolder(file.getAbsolutePath());
            refreshFileViewer();
        }
    }

    @FXML
    protected void refreshFileViewer() {
        fileViewer.getChildren().clear();
        File rootFolder = new File(MainApplication.getFolderPath());

        if (rootFolder.exists() && rootFolder.isDirectory()) {
            for (File file : rootFolder.listFiles()) {
                if (file.isDirectory() && !isExcludedDirectory(file)) {
                    Button dirButton = new Button("📁 " + file.getName());
                    dirButton.setOnAction(e -> handleDirectoryNavigation(file));
                    dirButton.setPrefWidth(200);
                    fileViewer.getChildren().add(dirButton);
                } else if (isSupportedFile(file)) {
                    Button fileButton = new Button(file.getName());
                    fileButton.setPrefWidth(200);
                    fileButton.setOnAction(e -> {
                        try {
                            handleFileSelection(file);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    });
                    fileViewer.getChildren().add(fileButton);
                }
            }
        }
    }

    private boolean isExcludedDirectory(File directory) {
        String name = directory.getName().toLowerCase();
        return name.equals(".git") || name.equals(".idea");
    }

    private boolean isSupportedFile(File file) {
        String name = file.getName().toLowerCase();
        String[] supportedExtensions = {"txt", "java", "md", "xml"};
        for (String ext : supportedExtensions) {
            if (name.endsWith(ext)) {
                return true;
            }
        }
        return false;
    }

    public void returnToRoot() {
        MainApplication.setFolderPath(MainApplication.getRootFolder());
        refreshFileViewer();
    }

    private void handleDirectoryNavigation(File directory) {
        MainApplication.setFolderPath(directory.getAbsolutePath());
        refreshFileViewer();
    }

    private void handleFileSelection(File file) throws IOException {
        if (currMode.equals("fileExplorer")) {
            fExplorer.getChildren().clear();
            List<String> allMethods = FileExplorer.getMethods(MainApplication.getFolderPath()+"/"+file.getName());

            if (allMethods == null || allMethods.isEmpty()) {
                fExplorer.getChildren().add(new Label("This file is not valid or contains no methods."));
            } else {
                TableView<String> tableView = new TableView<>();

                TableColumn<String, String> methodCol = new TableColumn<>("Methods");
                methodCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue()));

                tableView.getColumns().add(methodCol);
                tableView.getItems().addAll(allMethods);

                fExplorer.getChildren().add(tableView);
            }
        } else if(currMode.equals("codeMetrics")) {
            cMetrics.getChildren().clear();
            String filePath = MainApplication.getFolderPath()+"/"+file.getName();
            if (!filePath.endsWith(".java")) {
                cMetrics.getChildren().add(new Label("Please select a .java file"));
            }
            else {
                TableView<Metrics> tableView = new TableView<>();

                TableColumn<Metrics, Integer> locCol = new TableColumn<>("Lines of Code");
                locCol.setCellValueFactory(new PropertyValueFactory<>("linesOfCode"));

                TableColumn<Metrics, Integer> elocCol = new TableColumn<>("Effective Lines of Code");
                elocCol.setCellValueFactory(new PropertyValueFactory<>("effectiveLinesOfCode"));

                TableColumn<Metrics, Integer> llocCol = new TableColumn<>("Logical Lines of Code");
                llocCol.setCellValueFactory(new PropertyValueFactory<>("logicalLinesOfCode"));

                TableColumn<Metrics, Integer> complexityCol = new TableColumn<>("Cyclomatic Complexity");
                complexityCol.setCellValueFactory(new PropertyValueFactory<>("cyclomaticComplexity"));

                tableView.getColumns().addAll(locCol, elocCol, llocCol, complexityCol);


                int loc = SizeMetrics.countLinesOfCode(filePath);
                int eloc = SizeMetrics.countEffectiveLinesOfCode(filePath);
                int lloc = SizeMetrics.countLogicalLinesOfCode(filePath);
                int cc = SizeMetrics.calculateCyclomaticComplexity(filePath);
                Metrics metrics = new Metrics(loc, eloc, lloc, cc);
                tableView.getItems().add(metrics);

                cMetrics.getChildren().add(tableView);
            }

        }
    }

}
