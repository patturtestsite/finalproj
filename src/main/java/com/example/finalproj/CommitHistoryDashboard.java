package com.example.finalproj;

import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.revwalk.RevCommit;

import java.io.File;

public class CommitHistoryDashboard {
    private VBox visualization;

    public CommitHistoryDashboard(VBox visualization) {
        this.visualization = visualization;
    }

    public void showCommitHistory(String srcPath) {
        visualization.getChildren().clear();
        TextArea commitTextArea = new TextArea();
        commitTextArea.setEditable(false);

        File srcDir = new File(srcPath);
        File gitDir = findGitDirectory(srcDir);

        if (gitDir == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Repository Error");
            alert.setHeaderText(null);
            alert.setContentText("The selected directory does not contain a Git repository.");
            alert.showAndWait();
            return;
        }

        try (Git git = Git.open(gitDir)) {
            Iterable<RevCommit> commits = git.log().call();
            StringBuilder commitHistory = new StringBuilder();
            for (RevCommit commit : commits) {
                commitHistory.append("Commit: ").append(commit.getName()).append("\n")
                        .append("Author: ").append(commit.getAuthorIdent().getName()).append("\n")
                        .append("Date: ").append(commit.getAuthorIdent().getWhen()).append("\n")
                        .append("Message: ").append(commit.getFullMessage()).append("\n\n");
            }
            commitTextArea.setText(commitHistory.toString());
        } catch (Exception e) {
            e.printStackTrace();
            commitTextArea.setText("Error fetching commit history.");
        }

        visualization.getChildren().add(commitTextArea);
    }

    private File findGitDirectory(File startDir) {
        File currentDir = startDir.getAbsoluteFile();
        while (currentDir != null && !currentDir.equals(currentDir.getParentFile())) {
            File gitDir = new File(currentDir, ".git");
            if (gitDir.exists() && gitDir.isDirectory()) {
                return gitDir;
            }
            currentDir = currentDir.getParentFile();
        }
        return null;
    }
}
