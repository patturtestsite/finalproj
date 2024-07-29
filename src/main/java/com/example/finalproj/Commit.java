package com.example.finalproj;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Commit {
    private final StringProperty commitId;
    private final StringProperty commitMessage;

    public Commit(String commitId, String commitMessage) {
        this.commitId = new SimpleStringProperty(commitId);
        this.commitMessage = new SimpleStringProperty(commitMessage);
    }

    public String getCommitId() {
        return commitId.get();
    }

    public void setCommitId(String commitId) {
        this.commitId.set(commitId);
    }

    public StringProperty commitIdProperty() {
        return commitId;
    }

    public String getCommitMessage() {
        return commitMessage.get();
    }

    public void setCommitMessage(String commitMessage) {
        this.commitMessage.set(commitMessage);
    }

    public StringProperty commitMessageProperty() {
        return commitMessage;
    }
}
