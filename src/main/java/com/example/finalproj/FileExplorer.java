package com.example.finalproj;

import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;



public class FileExplorer {

    public static ArrayList<String> function(Path directoryPath) throws IOException {
        ArrayList<String> functions = new ArrayList<>();
                try (Scanner scanner = new Scanner(directoryPath)) {
                    while (scanner.hasNextLine()) {
                        String line = scanner.nextLine().trim();
                        if (line.length() >= 6) {
                            String isAFunction = line.substring(0, 6).trim();
                            if (isAFunction.equals("public") | isAFunction.equals("private")) {

                                System.out.println(line);
                                line=line.replace("{","");
                                functions.add(line);
                            }
                        }
                    }
                }
        return functions;
    }
}
