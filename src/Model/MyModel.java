package Model;


import Client.Client;
import Client.IClientStrategy;
import IO.MyDecompressorInputStream;
import Server.ServerStrategyGenerateMaze;
import Server.ServerStrategySolveSearchProblem;
import Server.Server;

import View.Main;
import View.MazeDisplayer;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.MyMazeGenerator;
import algorithms.mazeGenerators.Position;
import algorithms.search.AState;
import algorithms.search.Solution;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.stage.Stage;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Aviadjo on 6/14/2017.
 */
public class MyModel extends Observable implements IModel {

    private ExecutorService threadPool = Executors.newCachedThreadPool();
    private Server mazeGeneratingServer;
    private Server solveSearchProblemServer;
    public static Maze generatedMaze;
    public static Solution mazeSolution;
    private Position characterPosition;
    private boolean isSolved = false;
//    private int characterPositionRow = 1;
//    private int characterPositionColumn = 1;


    public MyModel() {
        mazeGeneratingServer = new Server(5400, 1000, new ServerStrategyGenerateMaze());
        solveSearchProblemServer = new Server(5401, 1000, new ServerStrategySolveSearchProblem());
    }

    public void startServers() {
        mazeGeneratingServer.start();
        solveSearchProblemServer.start();
    }

    public void stopServers() {
        mazeGeneratingServer.stop();
        solveSearchProblemServer.stop();
    }

    @Override
    public void generateMaze(int row, int column) {
        getMazeFromServer(row, column);
        characterPosition = generatedMaze.getStartPosition();
        System.out.println(generatedMaze.getStartPosition());
        setChanged();
        notifyObservers();
    }

    public void SetProperties(String GenerateAlgorithm, String SolveAlgorithm) {
        mazeGeneratingServer.setChangesInConfig(GenerateAlgorithm, SolveAlgorithm);
        setChanged();
        notifyObservers();
    }


    public void LoadMaze(Maze maze){
        characterPosition = maze.getStartPosition();
        generatedMaze=maze;
        System.out.println(maze.getStartPosition());
        setChanged();
        notifyObservers();
    }

    private void getMazeFromServer(int row, int column) {
        try {
            Client client = new Client(InetAddress.getLocalHost(), 5400, new IClientStrategy() {
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        int[] mazeDimensions = new int[]{row, column};
                        toServer.writeObject(mazeDimensions);
                        toServer.flush();
                        byte[] compressedMaze = (byte[]) fromServer.readObject();
                        InputStream is = new MyDecompressorInputStream(new ByteArrayInputStream(compressedMaze));
                        byte[] decompressedMaze = new byte[mazeDimensions[0] * mazeDimensions[1] + 72];
                        is.read(decompressedMaze);
                        Maze maze = new Maze(decompressedMaze);
                        generatedMaze = maze;
                        maze.print();
                    } catch (Exception var10) {
                        var10.printStackTrace();
                    }
                }
            });
            client.communicateWithServer();
        } catch (UnknownHostException var1) {
            var1.printStackTrace();
        }
    }

    private void getSolutionFromServer(){
        try {
            Client client = new Client(InetAddress.getLocalHost(), 5401, new IClientStrategy() {
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        //MyMazeGenerator mg = new MyMazeGenerator();
                        Maze maze = generatedMaze;
                        //maze.print();
                        toServer.writeObject(maze);
                        toServer.flush();
                        mazeSolution = (Solution)fromServer.readObject();
                    } catch (Exception var10) {
                        var10.printStackTrace();
                    }
                }
            });
            client.communicateWithServer();
        } catch (UnknownHostException var1) {
            var1.printStackTrace();
        }
    }

    public Solution getMazeSolution(){
        getSolutionFromServer();
        return mazeSolution;
    }


    @Override
    public Maze getMaze() {
        return generatedMaze;
    }

    @Override
    public void moveCharacter(KeyCode movement) {
        if (movementIsPossible(movement)) {
            switch (movement) {
                case NUMPAD8: // UP
                    characterPosition.setRow(characterPosition.getRowIndex() - 1);
                    break;
                case UP: // UP
                    characterPosition.setRow(characterPosition.getRowIndex() - 1);
                    break;
                case NUMPAD9: // DIAGONAL TOP RIGHT
                    characterPosition.setRow(characterPosition.getRowIndex() - 1);
                    characterPosition.setColumn(characterPosition.getColumnIndex() + 1);
                    break;
                case NUMPAD6: //RIGHT
                    characterPosition.setColumn(characterPosition.getColumnIndex() + 1);
                    break;
                case RIGHT: //RIGHT
                    characterPosition.setColumn(characterPosition.getColumnIndex() + 1);
                    break;
                case NUMPAD3: // DIAGONAL BOTTOM RIGHT
                    characterPosition.setRow(characterPosition.getRowIndex() + 1);
                    characterPosition.setColumn(characterPosition.getColumnIndex() + 1);
                    break;
                case NUMPAD2: // DOWN
                    characterPosition.setRow(characterPosition.getRowIndex() + 1);
                    break;
                case DOWN: // DOWN
                    characterPosition.setRow(characterPosition.getRowIndex() + 1);
                    break;
                case NUMPAD1: // DIAGONAL BOTTOM LEFT
                    characterPosition.setRow(characterPosition.getRowIndex() + 1);
                    characterPosition.setColumn(characterPosition.getColumnIndex() - 1);
                    break;
                case NUMPAD4: // LEFT
                    characterPosition.setColumn(characterPosition.getColumnIndex() - 1);
                    break;
                case LEFT: // LEFT
                    characterPosition.setColumn(characterPosition.getColumnIndex() - 1);
                    break;
                case NUMPAD7: // DIAGONAL TOP LEFT
                    characterPosition.setRow(characterPosition.getRowIndex() - 1);
                    characterPosition.setColumn(characterPosition.getColumnIndex() - 1);
                    break;
            }
            if (characterPosition.equals(generatedMaze.getGoalPosition())) {// The user found a solution
                isSolved = true;
            }
            setChanged();
            notifyObservers();
            isSolved = false;
        }
    }

    public boolean ifSolved(){
        return isSolved;
    }

    private boolean movementIsPossible(KeyCode movement) {
        int characterCurrentRow = characterPosition.getRowIndex();
        int characterCurrentColumn = characterPosition.getColumnIndex();
        if (movement == KeyCode.NUMPAD8 ||movement == KeyCode.UP)
            return generatedMaze.isPositionFree(characterCurrentRow - 1, characterCurrentColumn);
        else if (movement == KeyCode.NUMPAD9)
            return generatedMaze.isPositionFree(characterCurrentRow - 1, characterCurrentColumn + 1) &&
                    (generatedMaze.isPositionFree(characterCurrentRow, characterCurrentColumn + 1) ||
                            generatedMaze.isPositionFree(characterCurrentRow - 1, characterCurrentColumn));
        else if (movement == KeyCode.NUMPAD6 || movement == KeyCode.RIGHT)
            return generatedMaze.isPositionFree(characterCurrentRow, characterCurrentColumn + 1);
        else if (movement == KeyCode.NUMPAD3)
            return generatedMaze.isPositionFree(characterCurrentRow + 1, characterCurrentColumn + 1) &&
        (generatedMaze.isPositionFree(characterCurrentRow, characterCurrentColumn + 1) ||
                generatedMaze.isPositionFree(characterCurrentRow + 1, characterCurrentColumn));
        else if (movement == KeyCode.NUMPAD2 || movement == KeyCode.DOWN)
            return generatedMaze.isPositionFree(characterCurrentRow + 1, characterCurrentColumn);
        else if (movement == KeyCode.NUMPAD1)
            return generatedMaze.isPositionFree(characterCurrentRow + 1, characterCurrentColumn - 1)&&
                    (generatedMaze.isPositionFree(characterCurrentRow+1, characterCurrentColumn) ||
                            generatedMaze.isPositionFree(characterCurrentRow , characterCurrentColumn-1));

        else if (movement == KeyCode.NUMPAD4 || movement == KeyCode.LEFT)
            return generatedMaze.isPositionFree(characterCurrentRow, characterCurrentColumn - 1);
        else if (movement == KeyCode.NUMPAD7)
            return generatedMaze.isPositionFree(characterCurrentRow - 1, characterCurrentColumn - 1)&&
                    (generatedMaze.isPositionFree(characterCurrentRow, characterCurrentColumn - 1) ||
                            generatedMaze.isPositionFree(characterCurrentRow - 1, characterCurrentColumn));
        else
            return false;
    }

    public void scroll(ScrollEvent event, MazeDisplayer mz) {
        Double direction = event.getDeltaY();
        Stage stage  = Main.primaryStage;
        if(direction > 0) {
            stage.setHeight(stage.getHeight()+5);
            stage.setWidth(stage.getWidth()+5);
        }else{
            stage.setHeight(stage.getHeight()-5);
            stage.setWidth(stage.getWidth()-5);
        }
        mz.redraw();
    }

    public void moveCharacter(MouseEvent m, MazeDisplayer mz) {
        int mouseY = (int) Math.floor(m.getSceneY() / (mz.getCellHeight()));
        int mouseX = (int) Math.floor(m.getSceneX() / (mz.getCellWidth()));
        if (mouseY < mz.getCharacterPositionRow())
            moveCharacter(KeyCode.NUMPAD8);
        if (mouseY > mz.getCharacterPositionRow())
            moveCharacter(KeyCode.NUMPAD2);
        if (mouseX < mz.getCharacterPositionColumn())
            moveCharacter(KeyCode.NUMPAD4);
        if (mouseX > mz.getCharacterPositionColumn())
            moveCharacter(KeyCode.NUMPAD6);
    }


    @Override
    public Position getCharacterPosition() {
        return characterPosition;
    }
}
