package Model;
import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
import javafx.scene.input.KeyCode;

public interface IModel {

    void initM();

    void mazeGenerator(int rows,int columns);

    void solutionResolver(Boolean check);

    void exit();

    void save();

    void load();

    void move(KeyCode move);

    void reset();

    int getCharacterRow();

    int getCharacterColumn();

    Maze getMaze();

    Solution getSolution();

    boolean getSolved();


}
