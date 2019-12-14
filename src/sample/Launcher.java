package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.awt.image.ImageObserver;


public class Launcher extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("UserController/login.fxml"));
        primaryStage.setTitle("Kursa4");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
        Image image = new Image("file:maxiqqq.png");
        primaryStage.getIcons().add(image);

    }
    public static void main(String[] args) {
        launch(args);
    }
}
