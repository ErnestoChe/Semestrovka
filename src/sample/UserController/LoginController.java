package sample.UserController;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.DataBaseController.dbConnection;

import java.io.IOException;
import java.sql.SQLException;

public class LoginController {

    @FXML
    private TextField nameText;

    @FXML
    private TextField passText;

    @FXML
    private Button loginButton;

    @FXML
    private Button registerButton;

    @FXML
    private Label errorLabel;

    @FXML
    void initialize() throws IOException {
        //переход к окну регистрации
        registerButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                Stage stage;
                Parent root;
                stage = (Stage) registerButton.getScene().getWindow();
                registerButton.getScene().getWindow().hide();
                System.out.println("login to register button");
                try {
                    root = FXMLLoader.load(getClass().getResource("/sample/UserController/register.fxml"));
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        //проверка подключения к базе
        // TODO вход в систему пользователя
        // TODO логи в базу о том, что зашел в систему
        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    if(dbConnection.getConnection().isValid(0)){
                        System.out.println("connection valid");
                    }else System.out.println("connection is invalid");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}

