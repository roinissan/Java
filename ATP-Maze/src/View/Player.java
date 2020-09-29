package View;

import javafx.scene.image.Image;

public class Player {
    private String name;
    private String imagePath;

    public Player() {
        name = "";
        imagePath = "player/kofiko.png";
    }

    public Player(String name, String image) {
        this.name = name;
        this.imagePath = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return imagePath;
    }

    public void setImage(String image) {
        this.imagePath = image;
    }
}
