package com.example.finalproj;

public class Metrics {
    private final int linesOfCode;
    private final int effectiveLinesOfCode;
    private final int logicalLinesOfCode;
    private final int cyclomaticComplexity;

    public Metrics(int linesOfCode, int effectiveLinesOfCode, int logicalLinesOfCode, int cyclomaticComplexity) {
        this.linesOfCode = linesOfCode;
        this.effectiveLinesOfCode = effectiveLinesOfCode;
        this.logicalLinesOfCode = logicalLinesOfCode;
        this.cyclomaticComplexity = cyclomaticComplexity;
    }

    public int getLinesOfCode() {
        return linesOfCode;
    }

    public int getEffectiveLinesOfCode() {
        return effectiveLinesOfCode;
    }

    public int getLogicalLinesOfCode() {
        return logicalLinesOfCode;
    }

    public int getCyclomaticComplexity() {
        return cyclomaticComplexity;
    }
}