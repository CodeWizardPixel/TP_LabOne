package all.Utils;

import all.Map.Field;

import java.util.*;

public class Dijkstra {

    static class Cell implements Comparable<Cell> {
        int i, j;
        double cost;

        public Cell(int i, int j, double cost) {
            this.i = i;
            this.j = j;
            this.cost = cost;
        }

        @Override
        public int compareTo(Cell other) {
            return Double.compare(this.cost, other.cost);
        }
    }

    public static class PathStep {
        public int x;
        public int y;
        public double stepCost;

        public PathStep(int x, int y, double stepCost) {
            this.x = x;
            this.y = y;
            this.stepCost = stepCost;
        }
    }

    private static final int[][] DIRECTIONS = {
            { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 },
            { -1, -1 }, { -1, 1 }, { 1, -1 }, { 1, 1 }
    };

    private static final double DIAGONAL_COST = 1.5;

    public static double[][] convertForDijkstra(Field[][] gameGrid) {
        double[][] grid = new double[gameGrid.length][gameGrid[0].length];
        for (int i = 0; i < gameGrid.length; i++) {
            for (int j = 0; j < gameGrid[0].length; j++) {
                if (gameGrid[i][j].getUnit() != null || gameGrid[i][j].getHero() != null) {
                    grid[i][j] = Double.POSITIVE_INFINITY;
                } else {
                    grid[i][j] = gameGrid[i][j].getCost();
                }
            }
        }
        return grid;
    }

    public static PathStep[] dijkstra(double[][] grid, int[] start, int[] goal) {
        int rows = grid.length;
        int cols = grid[0].length;

        double[][] dist = new double[rows][cols];
        int[][][] prev = new int[rows][cols][2];
        for (double[] row : dist) {
            Arrays.fill(row, Double.POSITIVE_INFINITY);
        }
        dist[start[0]][start[1]] = 0;

        PriorityQueue<Cell> pq = new PriorityQueue<>();
        pq.add(new Cell(start[0], start[1], 0));

        prev[start[0]][start[1]] = null;

        while (!pq.isEmpty()) {
            Cell current = pq.poll();

            if (current.i == goal[0] && current.j == goal[1]) {
                List<PathStep> pathSteps = new ArrayList<>();
                List<int[]> path = new ArrayList<>();
                int[] currentCell = { current.i, current.j };

                while (currentCell != null) {
                    path.add(currentCell);
                    currentCell = prev[currentCell[0]][currentCell[1]];
                }
                Collections.reverse(path);

                for (int i = 1; i < path.size(); i++) {
                    int[] cell = path.get(i);
                    int[] prevCell = path.get(i - 1);
                    int dx = cell[0] - prevCell[0];
                    int dy = cell[1] - prevCell[1];
                    boolean isDiagonal = (Math.abs(dx) + Math.abs(dy)) == 2;
                    double moveCost = isDiagonal ? DIAGONAL_COST : 1.0;
                    boolean isGoal = (cell[0] == goal[0] && cell[1] == goal[1]);
                    double cellCost = isGoal ? 0 : grid[cell[0]][cell[1]];
                    double stepCost = moveCost * cellCost;
                    pathSteps.add(new PathStep(cell[1], cell[0], stepCost));
                }

                return pathSteps.toArray(new PathStep[0]);
            }

            for (int[] dir : DIRECTIONS) {
                int ni = current.i + dir[0];
                int nj = current.j + dir[1];

                if (ni >= 0 && ni < rows && nj >= 0 && nj < cols) {
                    boolean isGoal = (ni == goal[0] && nj == goal[1]);
                    double cellCost = isGoal ? 0 : grid[ni][nj];
                    double moveCost = (Math.abs(dir[0]) + Math.abs(dir[1]) == 2) ? DIAGONAL_COST : 1.0;
                    double newCost = current.cost + moveCost * cellCost;

                    if (newCost < dist[ni][nj]) {
                        dist[ni][nj] = newCost;
                        prev[ni][nj] = new int[] { current.i, current.j };
                        pq.add(new Cell(ni, nj, newCost));
                    }
                }
            }
        }

        return new PathStep[0];
    }
}