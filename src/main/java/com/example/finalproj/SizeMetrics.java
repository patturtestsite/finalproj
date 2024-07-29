package com.example.finalproj;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SizeMetrics {
    public static int countLinesOfCode(String filePath) throws IOException {
        int linesOfCode = 0;
        boolean inBlockComment = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (inBlockComment) {
                    if (line.endsWith("*/")) {
                        inBlockComment = false;
                    }
                } else if (line.startsWith("/*")) {
                    inBlockComment = true;
                } else if (!line.isEmpty() && !line.startsWith("//")) {
                    linesOfCode++;
                }
            }
        }

        return linesOfCode;
    }

    public static int countEffectiveLinesOfCode(String filePath) throws IOException {
        int effectiveLinesOfCode = 0;
        boolean inBlockComment = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (inBlockComment) {
                    if (line.endsWith("*/")) {
                        inBlockComment = false;
                    }
                } else if (line.startsWith("/*")) {
                    inBlockComment = true;
                } else if (!line.isEmpty() && !line.startsWith("//")) {
                    // Exclude lines with only braces
                    if (!line.equals("{") && !line.equals("}")) {
                        effectiveLinesOfCode++;
                    }
                }
            }
        }

        return effectiveLinesOfCode;
    }

    public static int countLogicalLinesOfCode(String filePath) throws IOException {
        int logicalLinesOfCode = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if(line.endsWith(";") || line.contains(";")) {
                    logicalLinesOfCode++;
                }
            }
        }

        return logicalLinesOfCode;
    }

    public static int calculateCyclomaticComplexity(String filePath) throws IOException {
        int complexity = 1;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();

                String[] patterns = {
                        "\\bif\\b",                // if statements
                        "\\bfor\\b",               // for loops
                        "\\bwhile\\b",             // while loops
                        "\\bdo\\b",                // do-while loops
                        "\\bswitch\\b",            // switch statements
                        "\\bcase\\b",              // case statements
                        "\\bbreak\\b",             // break statements
                        "\\bcontinue\\b",          // continue statements
                        "\\bcatch\\b",             // catch blocks
                        "\\bthrow\\b",             // throw statements
                        "\\breturn\\b"             // return statements
                };

                for (String pattern : patterns) {
                    Pattern regex = Pattern.compile(pattern);
                    Matcher matcher = regex.matcher(line);
                    while (matcher.find()) {
                        complexity++;
                    }
                }
            }
        }

        return complexity;
    }

    public static Metrics calculateMetrics(String filePath) throws IOException {
        int loc = SizeMetrics.countLinesOfCode(filePath);
        int eloc = SizeMetrics.countEffectiveLinesOfCode(filePath);
        int lloc = SizeMetrics.countLogicalLinesOfCode(filePath);
        int cc = SizeMetrics.calculateCyclomaticComplexity(filePath);
        return new Metrics(loc, eloc, lloc, cc);
    }

    public static String compareMetrics(Metrics metrics1, Metrics metrics2) {
        String comparisonResult = "Comparison Results:\n";

        if (metrics1.getLinesOfCode() + metrics1.getEffectiveLinesOfCode() + metrics1.getLogicalLinesOfCode()
                + metrics1.getCyclomaticComplexity() < metrics2.getLinesOfCode() + metrics2.getEffectiveLinesOfCode()
                + metrics2.getLogicalLinesOfCode() + metrics2.getCyclomaticComplexity()) {
            comparisonResult += "File 1 is less complex than File 2 (and potentially better). \n";
        } else if (metrics1.getLinesOfCode() > metrics2.getLinesOfCode()) {
            comparisonResult += "File 2 is less complex than File 1 (and potentially better). \n";
        }

        return comparisonResult;
    }
}
