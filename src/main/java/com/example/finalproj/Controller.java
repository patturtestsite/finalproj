package com.example.finalproj;

import com.mxgraph.swing.mxGraphComponent;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class Controller {

    @FXML
    private VBox fileViewer;

    @FXML
    private TextField insertLink;

    @FXML
    private TextArea commitHistoryArea;

    @FXML
    private Button insertLinkButton;

    @FXML
    private Button uploadButton;

    @FXML
    private VBox Visualization;

    @FXML
    private ScrollPane commitHistoryScrollPane;

    @FXML
    private SwingNode dGraph;

    @FXML
    private TableView<Commit> commitTableView;

    @FXML
    private TableColumn<Commit, String> commitIdColumn;

    @FXML
    private TableColumn<Commit, String> commitMessageColumn;

    @FXML
    private Text downloading;

    @FXML
    private void initialize() {
        // Set default visibility
        dGraph.setVisible(true);
        commitHistoryScrollPane.setVisible(false);

        // Configure commitTableView columns
        commitIdColumn.setCellValueFactory(cellData -> cellData.getValue().commitIdProperty());
        commitMessageColumn.setCellValueFactory(cellData -> cellData.getValue().commitMessageProperty());

        // Initialize data and visibility based on application state
        updateTextVisibility();
        MainApplication.getDownloading().addListener((observable, oldValue, newValue) -> updateTextVisibility());
        MainApplication.getFolderPathProperty().addListener((observable, oldValue, newValue) -> {
            GitResources.getFileList().clear();
            GitResources.addFilesToList(newValue);
            refreshFileViewer();
        });
    }

    @FXML
    protected void onInsertButtonClick() throws IOException {
        GitResources.getFileList().clear();
        GitResources.downloadRepo(insertLink.getText().trim(), "./downloaded-files");
        MainApplication.setFolderPath("./downloaded-files");
    }

    @FXML
    protected void updateTextVisibility() {
        downloading.setVisible(MainApplication.isDownloading());
    }

    @FXML
    protected void showCommitHistory() {
        try {
            List<String> commits = GitResources.getCommitHistory();
            commitTableView.getItems().clear(); // Clear existing items
            for (String commit : commits) {
                String[] details = commit.split(" "); // Modify this based on your commit format
                commitTableView.getItems().add(new Commit(details[0], details[1]));
            }

            // Show commit history table and hide visualization
            dGraph.setVisible(false);
            commitHistoryScrollPane.setVisible(true);
        } catch (IOException e) {
            e.printStackTrace();
            commitHistoryArea.setText("Error fetching commit history.");
        }
    }

    @FXML
    protected void showVisualization() {
        // Show existing visualization and hide commit history table
        dGraph.setVisible(true);
        commitHistoryScrollPane.setVisible(false);
    }

    @FXML
    protected void displayDependencyGraph() {
        DependencyGraph generator = new DependencyGraph();
        try {
            mxGraphComponent graphComponent = generator.generateGraph(MainApplication.getFolderPath());
            createAndSetSwingContent(graphComponent);
        } catch (IOException e) {
            e.printStackTrace();
            Text errorText = new Text("Error generating dependency graph.");
            Visualization.getChildren().clear();
            Visualization.getChildren().add(errorText);
        }
    }

    private void createAndSetSwingContent(mxGraphComponent graphComponent) {
        Visualization.getChildren().clear();
        SwingNode swingNode = new SwingNode();
        Visualization.getChildren().add(swingNode);

        SwingUtilities.invokeLater(() -> swingNode.setContent(graphComponent));
    }

    @FXML
    private void openFileChooser() {
        DirectoryChooser dirChooser = new DirectoryChooser();
        dirChooser.setTitle("Select Project Directory");

        File file = dirChooser.showDialog(uploadButton.getScene().getWindow());

        if (file != null) {
            MainApplication.setFolderPath(file.getAbsolutePath());
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
                    fileViewer.getChildren().add(dirButton);
                } else if (isSupportedFile(file)) {
                    Button fileButton = new Button(file.getName());
                    fileButton.setOnAction(e -> handleFileSelection(file));
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

    private String getFileExtension(File file) {
        String name = file.getName();
        int dotIndex = name.lastIndexOf('.');
        return (dotIndex == -1) ? "" : name.substring(dotIndex + 1).toLowerCase();
    }

    private void handleDirectoryNavigation(File directory) {
        MainApplication.setFolderPath(directory.getAbsolutePath());
        refreshFileViewer();
    }

    private void handleFileSelection(File file) {
        try {
            showFileContent(file);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void showFileContent(File file) throws IOException {
        Visualization.getChildren().clear();

        if (file.isDirectory()) {
            displayDirectoryContents(file);
        } else {
            String fileExtension = getFileExtension(file);

            switch (fileExtension) {
                case "txt":
                case "java":
                case "md":
                case "xml":
                    displayTextFile(file);
                    break;
                case "png":
                case "jpg":
                case "jpeg":
                    displayImageFile(file);
                    break;
                default:
                    Text unknownType = new Text("Unsupported file type");
                    Visualization.getChildren().add(unknownType);
            }
        }
    }

    private void displayTextFile(File file) throws IOException {
        TextArea textArea = new TextArea(new String(Files.readAllBytes(file.toPath())));
        textArea.setWrapText(true);
        textArea.setEditable(false);
        textArea.setPrefSize(600, 400);
        Visualization.getChildren().add(textArea);
    }

    private void displayImageFile(File file) {
        Image image = new Image(file.toURI().toString());
        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(600);
        Visualization.getChildren().add(imageView);
    }

    private void displayDirectoryContents(File directory) {
        VBox directoryBox = new VBox();
        for (File file : directory.listFiles()) {
            if (file.isDirectory() && !isExcludedDirectory(file)) {
                Button dirButton = new Button("📁 " + file.getName());
                dirButton.setOnAction(e -> handleDirectoryNavigation(file));
                directoryBox.getChildren().add(dirButton);
            } else if (isSupportedFile(file)) {
                Button fileButton = new Button(file.getName());
                fileButton.setOnAction(e -> handleFileSelection(file));
                directoryBox.getChildren().add(fileButton);
            }
        }
        ScrollPane scrollPane = new ScrollPane(directoryBox);
        scrollPane.setFitToWidth(true);
        Visualization.getChildren().add(scrollPane);
    }
}
