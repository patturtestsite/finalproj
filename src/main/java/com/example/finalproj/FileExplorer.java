package com.example.finalproj;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;

/*Parses through src files to display java functions
* @author: Celine Ha
* */
public class FileExplorer {

    public static ArrayList<String> function(Path directoryPath) throws IOException {
        ArrayList<String> functions = new ArrayList<>();
        try (Scanner scanner = new Scanner(directoryPath)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.length() >= 6) {
                    String isAFunction = line.substring(0, 6).trim();
                    if (isAFunction.equals("public") | isAFunction.equals("private")) {
                        line=line.replace("{","");
                        functions.add(line);
                    }
                }
            }
        }
        return functions;
    }
}