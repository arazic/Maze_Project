package Model;

import View.MazeDisplayer;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import algorithms.search.Solution;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;


/**
 * Created by Aviadjo on 6/14/2017.
 */
public interface IModel {
    void generateMaze(int row, int column);
    void moveCharacter(KeyCode movement);
    void moveCharacter(MouseEvent m, MazeDisplayer mz);
    Maze getMaze();
    Position getCharacterPosition();
    boolean ifSolved();
    Solution getMazeSolution();
    void LoadMaze(Maze maze);
    void stopServers();
    void SetProperties(String GenerateAlgorithm, String SolveAlgorithm);

    void scroll(ScrollEvent event, MazeDisplayer mazeDisplayer);
}
