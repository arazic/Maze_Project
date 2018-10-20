package View;


import Model.MyModel;
import algorithms.mazeGenerators.Maze;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Optional;

public class SaveController {

    public File SaveMaze(MyModel myModel, String path) {
        File savedMaze = new File(path);
        if(myModel.getMaze()!=null){
            Maze maze= myModel.getMaze();
            ObjectOutputStream out = null;
            try {
                out = new ObjectOutputStream(new FileOutputStream(savedMaze));
                out.writeObject(maze);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Save succeeded!\n Now you can load this maze again\n");
                Optional<ButtonType> result = alert.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("To save maze, you have to generate first");
            Optional<ButtonType> result = alert.showAndWait();
            alert.close();
        }
        return savedMaze;
    }
}
