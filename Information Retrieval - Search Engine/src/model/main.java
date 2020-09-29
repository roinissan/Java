package model;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
//import junit.framework.Test;

import java.io.*;
import java.net.URLDecoder;


public class main extends Application{

    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        FileInputStream fxmlStream = new FileInputStream( "src\\view\\GuiFXML.fxml");
        Pane rootPane = (Pane) fxmlLoader.load(fxmlStream);
        Scene scene = new Scene(rootPane,800,500);
        stage.setScene(scene);
        stage.setTitle("Search Engine");
        stage.show();


    }

    public static void main(String args[]) throws IOException {
        launch(args);
    }
}
