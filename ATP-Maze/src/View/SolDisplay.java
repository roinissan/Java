package View;

import algorithms.mazeGenerators.Maze;
import algorithms.search.AState;
import algorithms.search.MazeState;
import algorithms.search.Solution;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;



public class SolDisplay extends Canvas{

    private Maze maze;
    private Solution sol;

    public void redraw(Maze maze, Solution solution){
        if(maze!=null && solution!=null) {

            GraphicsContext graphicsContext = getGraphicsContext2D();
            graphicsContext.clearRect(0, 0, getWidth(), getHeight());
            Image imageFileNameSolution = new Image("solution/solution.png");
            this.maze = maze;
            this.sol = solution;
            double cellHeight = getHeight() / maze.getMaze().length;
            double cellWidth = getWidth() / maze.getMaze()[0].length;

            for (AState state : sol.getSolutionPath()) {
                MazeState ms = (MazeState) state;
                if (checkIfGoalStart(ms.getCurrPosition().getRowIndex(),ms.getCurrPosition().getColumnIndex())) {
                    graphicsContext.drawImage(imageFileNameSolution, ms.getCurrPosition().getColumnIndex() * cellWidth, ms.getCurrPosition().getRowIndex() * cellHeight, cellWidth, cellHeight);
                }
            }
        }

    }
    private boolean checkIfGoalStart(int row, int col){
        if(row==maze.getStartPosition().getRowIndex() && col==maze.getStartPosition().getColumnIndex())
            return false;
        if(row==maze.getGoalPosition().getRowIndex() && col==maze.getGoalPosition().getColumnIndex() )
            return false;
        return true;
    }
    public void clearCanvas(){
       GraphicsContext gc = getGraphicsContext2D();
       gc.clearRect(0,0,getWidth(),getHeight());
    }
}
