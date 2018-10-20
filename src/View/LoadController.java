package View;

import algorithms.mazeGenerators.Maze;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Optional;

public class LoadController {
    public void LoadMaze(MyViewController myViewController, File savedMaze) {
        if(savedMaze!=null) {
            Maze maze = null;
            ObjectInputStream in = null;
            try {
                in = new ObjectInputStream(new FileInputStream(savedMaze));
                maze = (Maze) in.readObject();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            myViewController.LoadMaze(maze);
        }
        else{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("To load maze, you have to save first");
            Optional<ButtonType> result = alert.showAndWait();
            alert.close();
            }
        }

    }


