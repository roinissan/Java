package View;

import ViewModel.MyViewModel;
import algorithms.search.AState;
import algorithms.search.MazeState;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

public class MyViewController implements IView, Observer {


    private MyViewModel viewM;
    @FXML
    private MazeDisplay mDisplay = new MazeDisplay();
    @FXML
    private SolDisplay sDisplay = new SolDisplay();
    @FXML
    private WinnerDisplay wDisplay = new WinnerDisplay();

    private Player player = new Player();
    private Media theme = new Media(Paths.get("resources/themeSong/themeSong.mp3").toUri().toString());
    private MediaPlayer mediaPlayerTheme = new MediaPlayer(theme);


    private About about;
    private Help help;
    private PropertiesView properties;
    private int numOfRow;
    private int numOfcol;
    public javafx.scene.control.Button generatingButton;
    public javafx.scene.control.Button solvingButton;
    public javafx.scene.control.Button reset;
    public javafx.scene.control.TextField rowsText;
    public javafx.scene.control.TextField colsText;
    public javafx.scene.control.Label userRow;
    public javafx.scene.control.Label userColumn;
    public Stage mainStage;


    public void aboutStage(ActionEvent event) {
        try {
            Stage newStage = new Stage();
            newStage.setTitle("About");
            FXMLLoader fxml = new FXMLLoader();
            Parent root = fxml.load(getClass().getResource("A.fxml"));
            Scene newScene = new Scene(root, 800, 600);
            newScene.getStylesheets().add(getClass().getResource("../View/A.css").toExternalForm());
            newStage.setScene(newScene);
            help = fxml.getController();
            newStage.initModality(Modality.APPLICATION_MODAL);
            newStage.setResizable(false);
            newStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void propertiesStage(ActionEvent event) {
        try {
            Stage newStage = new Stage();
            newStage.setTitle("Properties");
            FXMLLoader fxml = new FXMLLoader();
            Parent root = fxml.load(getClass().getResource("P.fxml"));
            Scene newScene = new Scene(root, 800, 600);
            newScene.getStylesheets().add(getClass().getResource("../View/P.css").toExternalForm());
            newStage.setScene(newScene);
            properties = fxml.getController();
            newStage.initModality(Modality.APPLICATION_MODAL);
            newStage.setResizable(false);
            newStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void hintButton(ActionEvent event){
        if(viewM.getMaze()!=null){
            boolean check = false;
            if(viewM.getSol()==null){
                viewM.solutionResolver(false);
            }
            System.out.println(viewM.getUserRowPosition() + " " + viewM.getUserColPosition());
            for (AState state : viewM.getSol().getSolutionPath()) {

                MazeState ms = (MazeState) state;
                System.out.println(ms);
                if(ms.getCurrPosition().getRowIndex()==viewM.getUserColPosition() && ms.getCurrPosition().getColumnIndex()==viewM.getUserRowPosition()){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("You are ON the right Route!");
                    alert.showAndWait();
                    check = true;
                    break;
                }
            }
            if(!check){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("You are NOT on the right Route.");
                alert.showAndWait();
            }
            event.consume();
        }
        else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("You must create maze before trying to get a hint.");
            alert.showAndWait();
        }
    }

    public void helpStage(ActionEvent event) {
        try {
            Stage newStage = new Stage();
            newStage.setTitle("Help");
            FXMLLoader fxml = new FXMLLoader();
            Parent root = fxml.load(getClass().getResource("H.fxml"));
            Scene newScene = new Scene(root, 800, 600);
            newScene.getStylesheets().add(getClass().getResource("../View/H.css").toExternalForm());
            newStage.setScene(newScene);
            about = fxml.getController();
            newStage.initModality(Modality.APPLICATION_MODAL);
            newStage.setResizable(false);
            newStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void resizeScreen(Scene s) {
        s.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                mDisplay.redraw(viewM.getMaze(), viewM.getUserRowPosition(), viewM.getUserColPosition(), player);
                sDisplay.redraw(viewM.getMaze(), viewM.getSol());
            }
        });
        s.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                mDisplay.redraw(viewM.getMaze(), viewM.getUserRowPosition(), viewM.getUserColPosition(), player);
                sDisplay.redraw(viewM.getMaze(), viewM.getSol());
            }
        });
    }

    public void exitGame(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Exit Game?");
        Optional<ButtonType> ok_or_cancel = alert.showAndWait();
        if (ok_or_cancel.get() == ButtonType.OK) {
            viewM.exit();
            System.exit(0);
        } else {
            event.consume();
        }
    }

    public void scrolling(ScrollEvent event) {
        if (event.isControlDown()) {
            double zoomIn = 1.1;
            double zoomOut = event.getDeltaY();
            if (zoomOut < 0) {
                zoomIn = 0.9;
            }
            mDisplay.setScaleY(mDisplay.getScaleY() * zoomIn);
            mDisplay.setScaleX(mDisplay.getScaleX() * zoomIn);
            sDisplay.setScaleY(sDisplay.getScaleY() * zoomIn);
            sDisplay.setScaleX(sDisplay.getScaleX() * zoomIn);
        }
    }

    public void resetButton(ActionEvent event) {
        if (viewM.getMaze() != null) {
            viewM.reset();
            event.consume();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("You must create maze before solving it.");
            alert.showAndWait();
        }
    }

    public void loadButton(ActionEvent event) {
        viewM.load();
        event.consume();
    }

    public void solveButton(ActionEvent event) {
        if (viewM.getMaze() != null && !viewM.getSolved()) {
            viewM.solutionResolver(true);
            event.consume();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText(viewM.getMaze() == null ? "You must create maze before solving it." : "The maze is already solved");
            alert.showAndWait();

        }

    }

    public void generateButton(ActionEvent event) {
        try {
            numOfRow = Integer.valueOf(rowsText.getText());
            numOfcol = Integer.valueOf(colsText.getText());
            viewM.mazeGenerator(numOfRow, numOfcol);
            if(mediaPlayerTheme.getStatus()== MediaPlayer.Status.PLAYING)
                mediaPlayerTheme.stop();
            mediaPlayerTheme.play();
        } catch (NumberFormatException nfe) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("You may enter only numbers");
            alert.showAndWait();
        }
        event.consume();
    }

    public void onPlayerMovement(KeyEvent event) {
        viewM.move(event.getCode());
        event.consume();
    }

    public void save(ActionEvent event) {
        if (viewM.getMaze() != null && !viewM.getSolved()) {
            viewM.save();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(viewM.getMaze() == null ? "You must generate a maze before trying to save" : "You Can't save solved maze");
            alert.showAndWait();
        }
    }

    public void setVModel(MyViewModel mvm) {
        viewM = mvm;
    }


    @Override
    public void update(Observable o, Object arg) {
        String argument = (String) arg;
        boolean replay;
        if (o == viewM) {
            if (argument.equals("MazeDisplay")) {
                sDisplay.clearCanvas();
                mDisplay.redraw(viewM.getMaze(), viewM.getUserRowPosition(), viewM.getUserColPosition(), player);
            }
            if (argument.equals("SolDisplay")) {
                sDisplay.redraw(viewM.getMaze(), viewM.getSol());
            }
            if (argument.equals("WinnerDisplay")) {
                if(mediaPlayerTheme.getStatus()== MediaPlayer.Status.PLAYING)
                    mediaPlayerTheme.stop();
                wDisplay.redraw(mainStage);
                if (wDisplay.isReplay()) {
                    sDisplay.clearCanvas();
                    viewM.mazeGenerator(numOfRow, numOfcol);
                    mediaPlayerTheme.play();
                    wDisplay.setReplay(false);
                }

            }
        }
    }

    public Stage getMainStage() {
        return mainStage;
    }

    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }
}
