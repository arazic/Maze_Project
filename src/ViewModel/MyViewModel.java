package ViewModel;

import Model.IModel;
import View.MazeDisplayer;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import algorithms.search.Solution;
import javafx.beans.property.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by Aviadjo on 6/14/2017.
 */
public class MyViewModel extends Observable implements Observer {

    private IModel model;
    public boolean isSolved= false;
    public Position characterPosition;
    public StringProperty characterPositionRow = new SimpleStringProperty(""); //= new SimpleStringProperty(characterPosition.getRowIndex() + ""); //For Binding
    public StringProperty characterPositionColumn = new SimpleStringProperty(""); //= new SimpleStringProperty(characterPosition.getRowIndex() + ""); //For Binding
    public String generateAlgorithm;
    public BooleanProperty mazeIsSolved = new SimpleBooleanProperty(false);

//    public ObservableValue<? extends SingleSelectionModel<String>> generateMazeAlgorithm;
//    public ObservableValue<? extends SingleSelectionModel<String>> solvingMazeAlgorithm;

    public MyViewModel(IModel model){
        this.model = model;
//        this.characterPosition = model.getCharacterPosition();
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o==model){
            characterPosition = model.getCharacterPosition();
            characterPositionRow.set(characterPosition.getRowIndex()+ "");
            characterPositionColumn.set(characterPosition.getColumnIndex()+ "");
            mazeIsSolved.setValue(model.ifSolved());
            isSolved= model.ifSolved();
            setChanged();
            notifyObservers();
        }
    }

    public void generateMaze(int row, int column){
        model.generateMaze(row, column);
    }

    public void stopServers(){
        model.stopServers();
    }

    public void moveCharacter(KeyCode movement){
        model.moveCharacter(movement);
    }

    public void moveCharacter(MouseEvent m, MazeDisplayer mz){
        model.moveCharacter(m, mz);
    }

    public Maze getMaze() {
        return model.getMaze();
    }

    public Position getCharacterPosition(){
        return characterPosition;
    }

    public Solution getSolution() {
        return model.getMazeSolution();
    }

    public void LoadMaze(Maze maze) {
        model.LoadMaze(maze);
    }

    public void SetProperties(String generateAlgorithm, String solveAlgorithm) {
        model.SetProperties(generateAlgorithm,solveAlgorithm);
    }

    public void scroll(ScrollEvent event, MazeDisplayer mazeDisplayer) {
        model.scroll(event, mazeDisplayer);
    }
}
