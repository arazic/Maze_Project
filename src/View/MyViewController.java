package View;

import ViewModel.MyViewModel;
import algorithms.mazeGenerators.Maze;
import javafx.beans.property.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.File;
import java.util.Observable;
import java.util.Observer;
import java.util.Optional;

public class MyViewController implements Observer, IView {

    @FXML
    public Pane mazePane;
    public MenuItem menuItem_load;
    public ComboBox character_comboBox;
    public ComboBox goal_comboBox;
    private static MyViewModel viewModel;
    public MazeDisplayer mazeDisplayer;
    public SolutionDisplayer solutionDisplayer;
    private boolean mazeIsOnAir;
    private boolean mazeIsSolved;
    public javafx.scene.layout.BorderPane borderPane;
    public javafx.scene.layout.BorderPane mainBoarderPane;
    public javafx.scene.control.TextField txtfld_rowsNum;
    public javafx.scene.control.TextField txtfld_columnsNum;
    public javafx.scene.control.Label lbl_rowsNum;
    public javafx.scene.control.Label lbl_columnsNum;
    public javafx.scene.control.Button btn_generateMaze;
    public javafx.scene.control.Button btn_StopMusic;

    public Button btn_solveMaze;
    private boolean MusicOn = true;
    public EventHandler<ActionEvent> aboutDevHandler;
    public EventHandler<ActionEvent> aboutMazeHandler;
    public EventHandler<ActionEvent> propertiesHandler;
    public EventHandler<ActionEvent> newHandler;
    public EventHandler<ActionEvent> closeHandler;
    public EventHandler<ActionEvent> saveHandler;
    public EventHandler<ActionEvent> loadHandler;
    public EventHandler<ActionEvent> HelpInstructionsHandler;
    public EventHandler<ActionEvent> HelpSaveAndLoadHandler;
    public javafx.scene.control.ChoiceBox<String> chbx_generateAlgorithm;
    public javafx.scene.control.ChoiceBox<String> chbx_solvingAlgorithm;
    public javafx.scene.control.Button btn_SetProp;
    private MediaPlayer mediaPlayer;


    public void setViewModel(MyViewModel viewModel) {
        this.viewModel = viewModel;
        bindProperties(viewModel);

    }


    public void initView() {
        String musicFile = "src/View/BackMusic.mp3";     // For example
        Media sound = new Media(new File(musicFile).toURI().toString());
        mediaPlayer = new MediaPlayer(sound);
    }


    private void bindProperties(MyViewModel viewModel) {
//       lbl_rowsNum.IntegerProperty().bind(viewModel.characterPosition.getRowIndex());
        lbl_rowsNum.textProperty().bind(viewModel.characterPositionRow);
        lbl_columnsNum.textProperty().bind(viewModel.characterPositionColumn);
        //chbx_generateAlgorithm.selectionModelProperty().bind(viewModel.generateAlgorithm);
        //chbx_solvingAlgorithm.selectionModelProperty().bind(viewModel.solvingMazeAlgorithm);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o == viewModel) {
            displayMaze(viewModel.getMaze());
            btn_generateMaze.setDisable(false);
            if (viewModel.isSolved) {
                solved();
            }
        }
    }

    private void solved() {
        SolvedController SolvedController = new SolvedController();
        SolvedController.successTosolvedMaze();
        btn_solveMaze.setDisable(true);
        mazeIsOnAir = false;
        mazeIsSolved = true;
    }
//    //region String Property for Binding
//    public StringProperty characterPositionRowIndex = new SimpleStringProperty();
//    public StringProperty characterPositionColumnIndex = new SimpleStringProperty();
///*
//    public StringProperty characterPositionColumn = new SimpleStringProperty();
//*/

    @Override
    public void displayMaze(Maze maze) {
        mazeDisplayer.setMaze(maze);
        mazeIsOnAir = true;
        int characterPositionRow = viewModel.getCharacterPosition().getRowIndex();
        int characterPositionColumn = viewModel.getCharacterPosition().getColumnIndex();
        mazeDisplayer.setCharacterPosition(characterPositionRow, characterPositionColumn);
        this.characterPositionRow.set(characterPositionRow + "");
        this.characterPositionRow.set(characterPositionRow + "");
        this.characterPositionColumn.set(characterPositionColumn + "");
        //btn_solveMaze.setDisable(false);


    }

    public void setComboBoxHandler(EventHandler<ActionEvent> comboBoxHandler) {

    }

    @Override
    public void setNewHandler(EventHandler<ActionEvent> NewHandler) {
        this.newHandler = NewHandler;
    }

    @Override
    public void setAboutDevHandler(EventHandler<ActionEvent> aboutDevHandler) {
        this.aboutDevHandler = aboutDevHandler;
    }

    @Override
    public void setAboutMazeHandler(EventHandler<ActionEvent> aboutMazeHandler) {
        this.aboutMazeHandler = aboutMazeHandler;
    }


    @Override
    public void setPropertiesHandler(EventHandler<ActionEvent> propertiesHandler) {
        this.propertiesHandler = propertiesHandler;
    }

    @Override
    public void setCloseHandler(EventHandler<ActionEvent> closeHandler) {
        this.closeHandler = closeHandler;
    }

    @Override
    public void setSaveHandler(EventHandler<ActionEvent> saveHandler) {
        this.saveHandler = saveHandler;
    }

    @Override
    public void setLoadHandler(EventHandler<ActionEvent> loadHandler) {
        this.loadHandler = loadHandler;
    }

    public void Load(ActionEvent actionEvent) {
        loadHandler.handle(actionEvent);
    }

    public void Save(ActionEvent actionEvent) {
        saveHandler.handle(actionEvent);
        menuItem_load.setDisable(false);
    }

    public void Exit(ActionEvent actionEvent) {
        closeHandler.handle(actionEvent);
    }

    public void orderlyExit() {
        viewModel.stopServers();
    }

    public void AboutDev(ActionEvent actionEvent) {
        aboutDevHandler.handle(actionEvent);
    }

    public void AboutMaze(ActionEvent actionEvent) {
        aboutMazeHandler.handle(actionEvent);
    }

    public void HelpInstructions(ActionEvent actionEvent) {
        HelpInstructionsHandler.handle(actionEvent);
    }

    public void HelpSaveAndLoad(ActionEvent actionEvent) {
        HelpSaveAndLoadHandler.handle(actionEvent);
    }

    public void setHelpSaveAndLoadHandler(EventHandler<ActionEvent> HelpSaveAndLoadHandler) {
        this.HelpSaveAndLoadHandler = HelpSaveAndLoadHandler;
    }

    public void setHelpInstructionsHandler(EventHandler<ActionEvent> HelpInstructionsHandler) {
        this.HelpInstructionsHandler = HelpInstructionsHandler;
    }

    public void New(ActionEvent actionEvent) {
        newHandler.handle(actionEvent);
    }

    public void Properties(ActionEvent actionEvent) {
        propertiesHandler.handle(actionEvent);
    }

    public void generateMaze() {
        boolean legal=true;
        int row=20, column=20;
        resetGameBoard();
        setMusic(MusicOn);
        if (mazeIsOnAir) {
            ButtonType result = showConfirmationAlert("Are you sure that you want to generate a new maze before you solve this one?! Don't give up!",
                    "Generate new maze confirmation", "DON'T GIVE UP!");
            if (result != ButtonType.OK)
                return;
        }
        try {
            row = Integer.valueOf(txtfld_rowsNum.getText());
            column = Integer.valueOf(txtfld_columnsNum.getText());
        } catch (Exception exception) {
            showAlert("Value need to be between 1 to 1000");
            legal = false;
        }
        if (row <= 5 || column <= 5 || row > 1000 || column > 1000) {
            showAlert("Maze rows and columns need to be between 5 to 1000");
            legal = false;
        }
        if(legal!=false){
            btn_generateMaze.setDisable(true);
            btn_StopMusic.setDisable(false);
            //btn_solveMaze.setDisable(true);
            viewModel.generateMaze(row, column);}

    }
  /*  public void generateMaze() {
        resetGameBoard();
        setMusic(MusicOn);
        if (mazeIsOnAir) {
            ButtonType result = showConfirmationAlert("Are you sure that you want to generate a new maze before you solve this one?! Don't give up!",
                    "Generate new maze confirmation", "DON'T GIVE UP!");
            if (result != ButtonType.OK)
                return;
        }

        int row = Integer.valueOf(txtfld_rowsNum.getText());
        int column = Integer.valueOf(txtfld_columnsNum.getText());

        btn_generateMaze.setDisable(true);
        btn_StopMusic.setDisable(false);
        //btn_solveMaze.setDisable(true);
        viewModel.generateMaze(row, column);
    }*/

    public void Mute(ActionEvent actionEvent) {
        if (btn_StopMusic.getText().equals("Music")) {
            setMusic(true);
        } else {
            setMusic(false);
        }
    }

    private void setMusic(boolean musicOn) {
    /*    String musicFile = "src/MyViewController/BackMusic.mp3";     // For example
        Media sound = new Media(new File(musicFile).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(sound);*/
        if (musicOn) {
            this.mediaPlayer.play();
            btn_StopMusic.setText("Mute");
        } else {
            this.mediaPlayer.stop();
            btn_StopMusic.setText("Music");

        }
    }

    private void resetGameBoard() {
        solutionDisplayer.reset();
        mazeDisplayer.reset();
        mazeIsSolved = false;
        openButtons();
    }

    private void openButtons() {
        btn_solveMaze.setDisable(false);
    }


    public void LoadMaze(Maze maze) {
        viewModel.LoadMaze(maze);
    }


    public void solveMaze(ActionEvent actionEvent) {
//        solutionDisplayer.setSolution(viewModel.getSolution(), viewModel.getMaze().getNumOfRows(), viewModel.getMaze().getNumOfColumns());
        solutionDisplayer.setSolution(viewModel.getSolution(), viewModel.getMaze());
        btn_solveMaze.setDisable(true);
        //resetGameBoard(); //
        mazeIsOnAir = false;
    }


    private ButtonType showConfirmationAlert(String alertMessage, String title, String headerText) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText(alertMessage);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        Optional<ButtonType> result = alert.showAndWait();
        if (!result.isPresent())
            return null;
        else
            return result.get();
    }


    public void SetPROP(ActionEvent actionEvent) {
        boolean GenerateAlgorithmBoolean= true;
        boolean SolveAlgorithmBoolean= true;

        String GenerateAlgorithm= String.valueOf(chbx_generateAlgorithm.getValue());
        if(GenerateAlgorithm.equals("My Maze Generator"))
            GenerateAlgorithm="MyMazeGenerator";
        else if(GenerateAlgorithm.equals("My Simple Generator"))
            GenerateAlgorithm="SimpleMazeGenerator";
        else
            GenerateAlgorithmBoolean=false;

        String SolveAlgorithm= String.valueOf(chbx_solvingAlgorithm.getValue());
        if(SolveAlgorithm.equals("BestFS"))
            SolveAlgorithm= "BestFirstSearch";
        else if(SolveAlgorithm.equals("DFS"))
            SolveAlgorithm= "DepthFirstSearch";
        else if (SolveAlgorithm.equals("BFS"))
            SolveAlgorithm= "BreadthFirstSearch"; //default
        else
            SolveAlgorithmBoolean=false;

        if(!GenerateAlgorithmBoolean&&!SolveAlgorithmBoolean)
            showAlert("Please enter Solve Algorithm and Generate Algorithm");
        else if(!GenerateAlgorithmBoolean)
            showAlert("Please enter Generate Algorithm");
        else if(!SolveAlgorithmBoolean)
            showAlert("Please enter Solve Algorithm");

        else {
            showAlert("Thank you! \nProperties saved successfully\n");

            btn_SetProp.setDisable(false);
            viewModel.SetProperties(GenerateAlgorithm, SolveAlgorithm);
            Node source = (Node) actionEvent.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.close();
        }

    }

    public void KeyPressed(KeyEvent keyEvent) {
        if (mazeIsOnAir) {
            viewModel.moveCharacter(keyEvent.getCode());
            keyEvent.consume();
        } else {
            ButtonType result = showConfirmationAlert("You have solved this maze!! \n" +
                            "You can generate another one with the same dimensions by pressing the OK button. " +
                            "Otherwise you can press on Cancel button and choice new dimension",
                    "This maze isn't challenge you anymore", "You passed this challenge try the next one");
            if (result == ButtonType.OK)
                generateMaze();
        }
    }

    private void showAlert(String alertMessage) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(alertMessage);
        alert.show();
    }

    public void mouseDrag(MouseEvent k) {
        if (mazeIsOnAir) {
            if (k.isDragDetect()) {
                viewModel.moveCharacter(k, mazeDisplayer);
                k.consume();
            }
        }
    }


    //region String Property for Binding
    public StringProperty characterPositionRow = new SimpleStringProperty();

    public StringProperty characterPositionColumn = new SimpleStringProperty();

    public String getCharacterPositionRow() {
        return characterPositionRow.get();
    }

    public StringProperty characterPositionRowProperty() {
        return characterPositionRow;
    }

    public String getCharacterPositionColumn() {
        return characterPositionColumn.get();
    }

    public StringProperty characterPositionColumnProperty() {
        return characterPositionColumn;
    }

    public void redraw() {
        mazeDisplayer.redraw();
        solutionDisplayer.redraw();
    }

    public void scroll(ScrollEvent event) {
        viewModel.scroll(event, mazeDisplayer);
    }


    //endregion

}
