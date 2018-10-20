package View;

import Model.*;
import ViewModel.MyViewModel;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.*;
import java.util.Optional;

public class Main extends Application {

    public static Stage primaryStage;
    String tempDirectoryPath = System.getProperty("java.io.tmpdir");
    String path = tempDirectoryPath + File.separator + "SavedMaze.txt";
    File savedMaze = null;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        MyModel myModel = new MyModel();
        myModel.startServers();
        MyViewModel viewModel = new MyViewModel(myModel);
        myModel.addObserver(viewModel);
        //--------------

        primaryStage.setTitle("Welcome to Chen & Nadav Maze Application!");
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(getClass().getResource("MyView.fxml").openStream());
        Scene scene = new Scene(root, 780, 630);
        primaryStage.setMinHeight(675);
        primaryStage.setMinWidth(800);
        scene.getStylesheets().add(getClass().getResource("ViewStyle.css").toExternalForm());
        primaryStage.setScene(scene);
        //--------------
        MyViewController myViewController = fxmlLoader.getController();
        myViewController.initView();
        myViewController.setViewModel(viewModel);
        viewModel.addObserver(myViewController);

        //-------------
        primaryStage.setOnCloseRequest((WindowEvent exit) -> {
            myViewController.closeHandler.handle(new ActionEvent());
        });
        myViewController.setAboutDevHandler(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage stage = new Stage();
                stage.setTitle("About Developers");
                FXMLLoader fxmlLoader = new FXMLLoader();
                Parent root = null;
                try {
                    root = fxmlLoader.load(getClass().getResource("AboutDEV.fxml").openStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Scene scene = new Scene(root, 400, 350);
                scene.getStylesheets().add(getClass().getResource("MenuStyle.css").toExternalForm());

                stage.setScene(scene);
                stage.initModality(Modality.APPLICATION_MODAL); //Lock the window until it closes
                stage.show();
            }
        });
        scene.setOnScroll(event1 -> {
            myViewController.scroll(event1);
        });

        myViewController.setAboutMazeHandler(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage stage = new Stage();
                stage.setTitle("About Maze");
                FXMLLoader fxmlLoader = new FXMLLoader();
                Parent root = null;
                try {
                    root = fxmlLoader.load(getClass().getResource("AboutMaze.fxml").openStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Scene scene = new Scene(root, 400, 350);
                scene.getStylesheets().add(getClass().getResource("MenuStyle.css").toExternalForm());

                stage.setScene(scene);
                stage.initModality(Modality.APPLICATION_MODAL); //Lock the window until it closes
                stage.show();
            }
        });

        scene.setOnMouseDragged(event -> {
            myViewController.mouseDrag(event);
        });

        myViewController.setHelpInstructionsHandler(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage stage = new Stage();
                stage.setMinWidth(400);
                stage.setMinHeight(500);
                stage.setTitle("How to Get Started?");
                FXMLLoader fxmlLoader = new FXMLLoader();
                Parent root = null;
                try {
                    root = fxmlLoader.load(getClass().getResource("HelpInstructions.fxml").openStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Scene scene = new Scene(root, 450, 500);
                scene.getStylesheets().add(getClass().getResource("MenuStyle.css").toExternalForm());

                stage.setScene(scene);
                stage.initModality(Modality.APPLICATION_MODAL); //Lock the window until it closes
                stage.show();
            }
        });

        myViewController.setHelpSaveAndLoadHandler(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage stage = new Stage();
                stage.setTitle("Save or Load?");
                FXMLLoader fxmlLoader = new FXMLLoader();
                Parent root = null;
                try {
                    root = fxmlLoader.load(getClass().getResource("HelpSaveOrLoad.fxml").openStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Scene scene = new Scene(root, 400, 350);
                scene.getStylesheets().add(getClass().getResource("MenuStyle.css").toExternalForm());

                stage.setScene(scene);
                stage.initModality(Modality.APPLICATION_MODAL); //Lock the window until it closes
                stage.show();
            }
        });

        myViewController.setPropertiesHandler(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage stage = new Stage();
                stage.setTitle("Properties");
                FXMLLoader fxmlLoader = new FXMLLoader();
                Parent root = null;
                try {
                    root = fxmlLoader.load(getClass().getResource("Properties.fxml").openStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Scene scene = new Scene(root, 400, 350);
                scene.getStylesheets().add(getClass().getResource("ViewStyle.css").toExternalForm());
                stage.setScene(scene);
                stage.initModality(Modality.APPLICATION_MODAL); //Lock the window until it closes
                stage.show();
            }
        });

        myViewController.setNewHandler(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                myViewController.generateMaze();
            }
        });

        myViewController.setSaveHandler(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                SaveController saveController = new SaveController();
                savedMaze = saveController.SaveMaze(myModel, path);
            }
        });

        myViewController.setLoadHandler(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                LoadController loadController = new LoadController();
                loadController.LoadMaze(myViewController, savedMaze);
            }
        });

        myViewController.setCloseHandler(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                alertClose(myViewController);
            }
        });

        scene.widthProperty().addListener((observableValue, oldSceneWidth, newSceneWidth) -> myViewController.redraw());
        scene.heightProperty().addListener((observableValue, oldSceneHeight, newSceneHeight) -> myViewController.redraw());

        primaryStage.show();

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent windowEvent) {
                System.exit(0);
            }
        });
    }

    private void alertClose(MyViewController myViewController) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("are you sure? To exit and stop all this fun?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            closeAll(alert, result, myViewController);
        }
        else
            alert.close();
    }

    private void closeAll(Alert alert, Optional<ButtonType> result, MyViewController myViewController) {
        if (result.get() == ButtonType.OK) {
            myViewController.orderlyExit();
            System.exit(0);
            Platform.exit();
        } else {
            // ... user chose CANCEL or closed the dialog
            alert.close();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
