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
import sample.DataBaseController.dbHandler;
import java.io.IOException;

import static sample.AlertHandler.showAlert;

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
        // TODO логи в базу о том, что зашел в систему
        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String login = nameText.getText();
                String pass = passText.getText();

                String user = dbHandler.loginUser(login, pass);
                System.out.println(user);

                if(!user.equals("")){
                    showAlert("Welcome " + user,
                            "login succesful",
                            false);
                    //переход к окну
                    Stage stage;
                    Parent root;
                    stage = (Stage) loginButton.getScene().getWindow();
                    loginButton.getScene().getWindow().hide();
                    System.out.println("login to file chooser");
                    try {
                        root = FXMLLoader.load(getClass().getResource("/sample/FileSelection/filer.fxml"));
                        Scene scene = new Scene(root);
                        stage.setScene(scene);
                        stage.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }else{
                    errorLabel.setText("Неверное имя пользователя или пароль");
                }
            }
        });
    }
}

