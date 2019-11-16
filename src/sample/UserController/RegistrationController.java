package sample.UserController;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class RegistrationController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField userNameText;

    @FXML
    private TextField userPassText;

    @FXML
    private TextField userSurnameText;

    @FXML
    private TextField userPassCheck;

    @FXML
    private TextField userId;

    @FXML
    private Button userRegButton;

    @FXML
    private Button backButton;

    @FXML
    void initialize() {
        backButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                Stage stage;
                Parent root;
                stage = (Stage) backButton.getScene().getWindow();
                backButton.getScene().getWindow().hide();
                System.out.println("register back to login button");
                try {
                    root = FXMLLoader.load(getClass().getResource("/sample/UserController/login.fxml"));
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
