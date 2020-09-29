package View;

import algorithms.mazeGenerators.Maze;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

enum MOVE_STATE {
    start, goal, between
}

public class MazeDisplay extends Canvas {

    private Maze maze;
    private int characterPositionRow = 1;
    private int characterPositionColumn = 1;
    private StringProperty ImageFileNameWall = new SimpleStringProperty();
    private StringProperty ImageFileNameCharacter = new SimpleStringProperty();
    private StringProperty ImageFileNameWay = new SimpleStringProperty();
    private StringProperty ImageFileNameGoal = new SimpleStringProperty();

    public void redraw(Maze maze, int charRow, int charCol, Player player) {

        if (maze != null) {
            this.maze = maze;
            double canvasHeight = getHeight();
            double canvasWidth = getWidth();
            double cellHeight = canvasHeight / maze.getMaze().length;
            double cellWidth = canvasWidth / maze.getMaze()[0].length;
            characterPositionRow = charRow;
            characterPositionColumn = charCol;

            Image characterImage = new Image(player.getImage());
            Image wayImage = new Image("way/grass.jpg");
            Image wallImage = new Image("wall/wall.png");
            Image goalImage = new Image("goal/goal.png");

            GraphicsContext gc = getGraphicsContext2D();
            gc.clearRect(0, 0, getWidth(), getHeight());
            for (int y = 0; y < maze.getMaze().length; y++) {
                for (int x = 0; x < maze.getMaze()[y].length; x++) {
                    gc.drawImage(wayImage, x * cellWidth, y * cellHeight, cellWidth, cellHeight);
                    if (checkIfGoalStart(x, y) == MOVE_STATE.between) {
                        if (maze.getMaze()[y][x] == 1) {
                            gc.drawImage(wallImage, x * cellWidth, y * cellHeight, cellWidth, cellHeight);
                        } else
                            gc.drawImage(wayImage, x * cellWidth, y * cellHeight, cellWidth, cellHeight);
                    } else if (checkIfGoalStart(x, y) == MOVE_STATE.start) {
                        gc.drawImage(characterImage, x * cellWidth, y * cellHeight, cellWidth, cellHeight);
                    }
                }

            }
            int goal_y_index = maze.getGoalPosition().getColumnIndex();
            int goal_x_index = maze.getGoalPosition().getRowIndex();

            gc.drawImage(goalImage, goal_y_index * cellWidth, goal_x_index * cellHeight, cellWidth, cellHeight);
        }
    }


    private MOVE_STATE checkIfGoalStart(int row, int col) {
        if (row == characterPositionRow && col == characterPositionColumn)
            return MOVE_STATE.start;
        if (row == maze.getGoalPosition().getRowIndex() && col == maze.getGoalPosition().getColumnIndex())
            return MOVE_STATE.goal;
        return MOVE_STATE.between;
    }


    public String getImageFileNameWall() {
        return ImageFileNameWall.get();
    }

    public void setImageFileNameWall(String imageFileNameWall) {
        this.ImageFileNameWall.set(imageFileNameWall);
    }

    public String getImageFileNameWay() {
        return ImageFileNameWay.get();
    }

    public void setImageFileNameWay(String imageFileNameWay) {
        this.ImageFileNameWay.set(imageFileNameWay);
    }

    public String getImageFileNameCharacter() {
        return ImageFileNameCharacter.get();
    }

    public void setImageFileNameCharacter(String imageFileNameCharacter) {
        this.ImageFileNameCharacter.set(imageFileNameCharacter);
    }
}
