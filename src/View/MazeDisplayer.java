package View;

import algorithms.mazeGenerators.Maze;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;

public class MazeDisplayer extends Canvas {

    private Maze maze;
    private int characterPositionRow = 1;
    private int characterPositionColumn = 1;
    private double cellHeight;
    private double cellWidth;

    public double getCellHeight() {
        return cellHeight;
    }

    public double getCellWidth() {
        return cellWidth;
    }

    public void setMaze(Maze maze) {
        this.maze = maze;
        redraw();
    }

    public void setCharacterPosition(int row, int column) {
        characterPositionRow = row;
        characterPositionColumn = column;
        redraw();
    }

    public int getCharacterPositionRow() {
        return characterPositionRow;
    }

    public int getCharacterPositionColumn() {
        return characterPositionColumn;
    }

    public void redraw() {
        if (maze != null) {
            if(this.getScene().getHeight() < this.getScene().getWidth()) {
                this.setHeight(this.getScene().getHeight()-50);
                this.setWidth(this.getScene().getHeight()-50);
            }
            else{
                this.setHeight(this.getScene().getWidth()-50);
                this.setWidth(this.getScene().getWidth()-50);
            }
            double canvasHeight = getHeight();
            double canvasWidth = getWidth();
            cellHeight = canvasHeight / (maze.getNumOfRows()+2);
            cellWidth = canvasWidth / (maze.getNumOfColumns()+2);

            try {
                Image wallImage = new Image(new FileInputStream(ImageFileNameWall.get()));
                Image characterImage = new Image(new FileInputStream(ImageFileNameCharacter.get()));
                Image goalImage = new Image(new FileInputStream(ImageFileNameGoal.get()));
                //Image homeImage = new Image(new FileInputStream(ImageFileNameHome.get()));

                GraphicsContext gc = getGraphicsContext2D();
                gc.clearRect(0, 0, getWidth(), getHeight());

                for (int i = 0; i < maze.getNumOfColumns()+2 ;i++) {
                    for (int j = 0; j < maze.getNumOfRows() + 2; j++) {
                        if(i==0 || j==0|| i==maze.getNumOfColumns()+1|| j==  maze.getNumOfRows() + 1)
                            gc.drawImage(wallImage, i * cellHeight, j * cellWidth, cellHeight, cellWidth);
                    }
                }

                //Draw Maze
                for (int i = 1; i < maze.getNumOfColumns()+1 ;i++) {
                    for (int j = 1; j < maze.getNumOfRows()+1; j++) {
                        if (!maze.isPositionFree(j-1, i-1)) {
                            //gc.fillRect(i * cellHeight, j * cellWidth, cellHeight, cellWidth);
                            gc.drawImage(wallImage, i * cellHeight, j * cellWidth, cellHeight, cellWidth);
                        }
                    }
                }

                gc.drawImage(characterImage, (characterPositionColumn+1) * cellHeight, (characterPositionRow+1) * cellWidth, cellHeight, cellWidth);
                gc.drawImage(goalImage, (maze.getGoalPosition().getColumnIndex()+1) * cellHeight, (maze.getGoalPosition().getRowIndex()+1)* cellWidth, cellHeight, cellWidth);

            } catch (FileNotFoundException e) {
            }
        }
    }

    //region Properties
    private StringProperty ImageFileNameWall = new SimpleStringProperty();
    private StringProperty ImageFileNameCharacter = new SimpleStringProperty();
    private StringProperty ImageFileNameGoal = new SimpleStringProperty();
    private StringProperty ImageFileNameHome = new SimpleStringProperty();

    public String getImageFileNameWall() {
        return ImageFileNameWall.get();
    }

    public void setImageFileNameWall(String imageFileNameWall) {
        this.ImageFileNameWall.set(imageFileNameWall);
    }

    public String getImageFileNameCharacter() {
        return ImageFileNameCharacter.get();
    }

    public void setImageFileNameCharacter(String imageFileNameCharacter) {
        this.ImageFileNameCharacter.set(imageFileNameCharacter);
    }

    public String getImageFileNameHome() {return ImageFileNameHome.get();}
    public void setImageFileNameHome(String imageFileNameHome){this.ImageFileNameHome.set(imageFileNameHome);}

    public String getImageFileNameGoal() {return ImageFileNameGoal.get();}
    public void setImageFileNameGoal(String imageFileNameGoal) {
        this.ImageFileNameGoal.set(imageFileNameGoal);
    }

    public void reset() {
        characterPositionColumn = -1;
        characterPositionRow = -1;
    }
    //endregion

}
