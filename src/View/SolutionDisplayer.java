package View;

import algorithms.mazeGenerators.Maze;
import algorithms.search.AState;
import algorithms.search.Solution;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.canvas.Canvas;

import java.awt.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class SolutionDisplayer extends Canvas {
    private Solution solution;
    int mazeRowSize;
    int mazeColumnSize;
    private GraphicsContext gc;


    public void setSolution(Solution solution, Maze maze) {
        this.solution = solution;
        mazeRowSize = maze.getNumOfRows();
        mazeColumnSize = maze.getNumOfColumns();
        redraw();
    }

    public void redraw() {
        if (solution != null) {
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
            double cellHeight = canvasHeight / (mazeRowSize+2);
            double cellWidth = canvasWidth / (mazeColumnSize+2);

            try {
                Image solutionPath = new Image(new FileInputStream(solutionPathImage.get()));

                gc = getGraphicsContext2D();
                gc.clearRect(0, 0, getWidth(), getHeight());

                System.out.println(String.format("Solution steps: %s", solution.getSolutionPathSize()));
                ArrayList<AState> mazeSolutionSteps = solution.getSolutionPath();

                for (int i = 1; i < mazeSolutionSteps.size()-1; ++i) {
                    int[] Poistion = getPosition(mazeSolutionSteps.get(i));
                    System.out.println(String.format("%s. %s", i, ((AState) mazeSolutionSteps.get(i)).toString()));
                    gc.drawImage(solutionPath,  (Poistion[1]+1)* cellHeight, (Poistion[0]+1) * cellWidth, cellHeight, cellWidth);
                }


            } catch (FileNotFoundException e) {
                //e.printStackTrace();
            }
        }
    }

    private int[] getPosition(AState state) {
        String string = state.toString();
        System.out.println(string);
        string = string.substring(1, string.length() - 1);
        System.out.println(string);
        String[] parts = string.split(",");
        String rowString = parts[0];
        String columnString = parts[1];
        columnString = columnString.substring(1);
        int[] ans = new int[2];
        ans[0] = Integer.parseInt(rowString);
        ans[1] = Integer.parseInt(columnString);
        return ans;
    }


    //region Properties
    private StringProperty solutionPathImage = new SimpleStringProperty();

    public String getSolutionPathImage() {
        return solutionPathImage.get();
    }

    public void setSolutionPathImage(String imageFileNameWall) {
        this.solutionPathImage.set(imageFileNameWall);
    }

    public void reset() {
        if (gc != null){
            gc.clearRect(0, 0, getWidth(), getHeight());
            solution = null; // NOTICE: Solved the problem with the solution redrew when resizing.
        }
    }

    //endregion
}
