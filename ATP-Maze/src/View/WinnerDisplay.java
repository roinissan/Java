package View;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.control.Button;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;



import java.nio.file.Paths;
import java.util.Optional;


public class WinnerDisplay extends Canvas {
    private Stage new_stage = new Stage();
    boolean replay = false;
    public void redraw(Stage mainStage)  {
        new_stage.setTitle("You Are The Winner");

        HBox hbox_pane = new HBox();
        Button button = new Button();
        button.setText("New Game");

        hbox_pane.getChildren().add(button);

        Pane new_pane = new Pane();
        Image winner = new Image("winner/winner.gif");
        ImageView iv = new ImageView();
        iv.setImage(winner);
        iv.setFitHeight(getHeight());
        iv.setFitWidth(getWidth());
        new_pane.getChildren().add(iv);
        Media win = new Media(Paths.get("resources/winnerSound/winnerSong.mp3").toUri().toString());
        MediaPlayer mediaPlayerWinner = new MediaPlayer(win);
        mediaPlayerWinner.play();

        button.setOnAction(e -> {
            replay = true;
            e.consume();
            closeApp(mediaPlayerWinner);
        });

        new_stage.setOnCloseRequest(e -> {
            e.consume();
            closeApp(mediaPlayerWinner);
        } );

        BorderPane border_pane = new BorderPane();
        border_pane.setTop(hbox_pane);
        border_pane.setCenter(new_pane);

        Scene s = new Scene(border_pane,getWidth(),getHeight());
        new_stage.setScene(s);
        new_stage.showAndWait();

    }

    public boolean isReplay() {
        return replay;
    }

    public void setReplay(boolean replay) {
        this.replay = replay;
    }

    private void closeApp(MediaPlayer md) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Are you sure you want to leave?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {// ... user chose OK
            md.stop();
            new_stage.close();
        }
    }
}
