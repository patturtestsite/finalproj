//package com.example.finalproj;
//
//import java.io.IOException;
//import java.lang.reflect.Array;
//import java.nio.file.DirectoryStream;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Scanner;
//
//public class FileExplorer {
//
//    public static HashMap<String, ArrayList<String>> functiondictionary(String directoryPath) {
//        HashMap<String, ArrayList<String>> functions = new HashMap<>();
//        Path directory = Paths.get(directoryPath);
//
//        try (
//                DirectoryStream<Path> stream = Files.newDirectoryStream(directory)) {
//
//            //with forEach loop get all the path of files present in directory
//            for (Path file : stream) {
//                functions.put(file.getFileName().toString(), new ArrayList<>());
//                try (Scanner scanner = new Scanner(file)) {
//                    while (scanner.hasNextLine()) {
//                        String line = scanner.nextLine().trim();
//                        if (line.length() >= 6) {
//                            String functionName = line.substring(0, 6).trim();
//                            if (functionName.equals("public") | functionName.equals("private")) {
//                                System.out.println(line);
//                                functions.get(functionName).add(line);
//                            }
//                        }
//                    }
//                }
//
//
//            }
//
//        }
//        return functions;
//    }
//
//}
