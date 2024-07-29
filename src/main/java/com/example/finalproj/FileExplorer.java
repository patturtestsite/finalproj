package com.example.finalproj;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*Parses through src files to display java functions
* @author: Celine Ha
* */
public class FileExplorer {

    public static List<String> getMethods(String fileName) throws IOException {
        List<String> methods = new ArrayList<>();

        // Check if the file is a .java file
        if (fileName.endsWith(".java")) {
            Path filePath = Paths.get(fileName);
            List<String> lines = Files.readAllLines(filePath);

            // Regex pattern to match method signatures
            String methodPattern = "\\b(public|private|protected)\\b\\s+\\w+\\s+\\w+\\s*\\([^\\)]*\\)\\s*\\{?";
            Pattern pattern = Pattern.compile(methodPattern);

            for (String line : lines) {
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    methods.add(line.trim());
                }
            }
        } else {
            return null;
        }
        return methods;
    }
}