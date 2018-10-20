package View;

import algorithms.mazeGenerators.Maze;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.util.EventListener;

/**
 * Created by Aviadjo on 6/14/2017.
 */
public interface IView {
    void displayMaze(Maze maze);

    void setAboutDevHandler(EventHandler<ActionEvent> aboutDevHandler);

    void setAboutMazeHandler(EventHandler<ActionEvent> aboutMazeHandler);

    void setPropertiesHandler(javafx.event.EventHandler<ActionEvent> propertiesHandler);

    void setNewHandler(javafx.event.EventHandler<ActionEvent> NewHandler);

     void setCloseHandler (EventHandler<ActionEvent> closeHandler);

     void setSaveHandler (EventHandler<ActionEvent> saveHandler);

     void setLoadHandler (EventHandler<ActionEvent> loadHandler);

     void orderlyExit();



    }
