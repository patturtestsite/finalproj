package com.example.finalproj;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ObserverPatternDetector {
    public static List<File> detectObserverPattern(File directory) throws IOException {
        List<File> result = new ArrayList<>();
        Pattern observerPattern = Pattern.compile("\\b(Observer|Subject|PropertyChangeListener|PropertyChangeSupport|PropertyChangeEvent)\\b");

        Files.walk(directory.toPath())
                .filter(Files::isRegularFile)
                .forEach(file -> {
                    try {
                        String content = new String(Files.readAllBytes(file.toFile().toPath()));
                        Matcher matcher = observerPattern.matcher(content);
                        if (matcher.find()) {
                            result.add(file.toFile());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

        return result;
    }
}
