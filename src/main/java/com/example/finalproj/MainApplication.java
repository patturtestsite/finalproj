package com.example.finalproj;

import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

public class MainApplication extends Application {
    static final BooleanProperty downloading = new SimpleBooleanProperty(false);
    private static final StringProperty folderPath = new SimpleStringProperty();
    private static String rootFolder;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("main.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 850, 800);
        stage.setTitle("CSC 307 Final Project");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) throws IOException {
        launch();
    }

    public static BooleanProperty getDownloading() {
        return downloading;
    }

    public static boolean isDownloading() {
        return downloading.get();
    }

    public static void setDownloading(boolean newVal) {
        downloading.set(newVal);
    }

    public static void setFolderPath(String newPath) {
        folderPath.set(newPath);
    }

    public static void setRootFolder(String rFolder) {
        rootFolder = rFolder;
    }

    public static String getRootFolder() {
        return rootFolder;
    }

    public static String getFolderPath() {
        return folderPath.get();
    }

    public static StringProperty getFolderPathProperty() {
        return folderPath;
    }
}
