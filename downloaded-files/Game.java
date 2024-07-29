// package GameOfLifeMain;

public class Game {
    private static final int SIZE = 30;
    private boolean[][] gameGrid = new boolean[SIZE][SIZE];
    private int[][] numNeighborsGrid = new int[SIZE][SIZE];

    public Game() {
        for(int i = 0; i < SIZE; i++) {
            for(int j = 0; j < SIZE; j++) {
                gameGrid[i][j] = false;
            }
        }
    }

    public boolean[][] getGameGrid() {
        return gameGrid;
    }

    public void changeState(int x, int y) {
        gameGrid[x][y] = !gameGrid[x][y];
    }

    public void checkNeighbors() {
        for(int i = 0; i < SIZE; i++) {
            for(int j = 0; j < SIZE; j++) {
                numNeighborsGrid[i][j] = countNeighbors(i, j);
            }
        }
    }

    private int countNeighbors(int i, int j) {
        int rowLimit = gameGrid.length;
        int count = 0;

        if (rowLimit > 0) {
            int columnLimit = gameGrid[0].length;

            for (int x = Math.max(0, i - 1); x <= Math.min(i + 1, rowLimit - 1); x++) {
                for (int y = Math.max(0, j - 1); y <= Math.min(j + 1, columnLimit - 1); y++) {
                    if (x != i || y != j) {
                        if (gameGrid[x][y] == true) {
                            count++;
                        }
                    }
                }
            }
        }

        return count;
    }

    public void nextMove() {
        for(int i = 0; i < SIZE; i++) {
            for(int j = 0; j < SIZE; j++) {
                if(gameGrid[i][j]) {
                    if(numNeighborsGrid[i][j] == 0 || numNeighborsGrid[i][j] == 1 || numNeighborsGrid[i][j] == 4) {
                        gameGrid[i][j] = false;
                    } else if (numNeighborsGrid[i][j] == 2 || numNeighborsGrid[i][j] == 3) {
                        gameGrid[i][j] = true;
                    }
                } else {
                    if (numNeighborsGrid[i][j] == 3) {
                        gameGrid[i][j] = true;
                    }
                }
            }
        }
    }
}