package sample;

import Model.IModel;
import Model.MyModel;
import View.MyViewController;
import ViewModel.MyViewModel;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import javafx.event.Event;
import javafx.event.EventHandler;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.File;
import javafx.stage.WindowEvent;

import java.util.Optional;


public class Main extends Application {
    public static Stage pStage;

    @Override
    public void start(Stage primaryStage) throws Exception{

        pStage = primaryStage;
        IModel m = new MyModel();
        m.initM();
        MyViewModel vm = new MyViewModel(m);
        ((MyModel) m).addObserver(vm);
        //---------
        //FXMLLoader fxml = new FXMLLoader(getClass().getResource("../View/MyView.fxml"));
        FXMLLoader fxml = new FXMLLoader(getClass().getResource("../View/MyView.fxml"));
        Parent root = fxml.load();

        primaryStage.setTitle("Main");
        Scene scene = new Scene(root, 800, 500);
        scene.getStylesheets().add(getClass().getResource("../View/MyView.css").toExternalForm());
        primaryStage.setScene(scene);

        // ----------
        MyViewController view = fxml.getController();
        view.setVModel(vm);
        view.resizeScreen(scene);
        //view.setMaxMinEvent(primaryStage);
        vm.addObserver(view);
        // ----------
        primaryStage.setOnCloseRequest(e -> {
           e.consume();
           closeApp(vm);
        } );
        primaryStage.show();
    }

    private void closeApp(MyViewModel vm) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Are you sure you want to leave?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {// ... user chose OK
            vm.exit();
            pStage.close();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
