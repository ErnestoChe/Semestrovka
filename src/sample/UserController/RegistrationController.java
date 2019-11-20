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

public class RegistrationController {

    @FXML
    private TextField userNameText;

    @FXML
    private TextField userPassText;

    @FXML
    private TextField userSurnameText;

    @FXML
    private TextField userPassCheck;

    @FXML
    private TextField userLogin;

    @FXML
    private Button userRegButton;

    @FXML
    private Button backButton;

    @FXML
    private Label errorLabel;

    @FXML
    void initialize() {
        //кнопка возвращения на окно входа в систему
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
        userRegButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String login = userLogin.getText();
                String name = userNameText.getText();
                String lastname = userSurnameText.getText();
                String password;
                password = userPassText.getText();
                String report = dbHandler.addUser(login, password, name, lastname);
                if(!login.equals("") && !name.equals("") && !lastname.equals("") && !password.equals("")){
                    //проверка совпадения пароля в двух полях
                    if(userPassText.getText().equals(userPassCheck.getText())){
                        if(report.equals("23505")){
                            showAlert("Пользователь с таким логином уже существует",
                                    "Ошибка!",
                                    true);
                        }else if(report.equals("1")){
                            //System.out.println("uspeh");
                            showAlert("Пользователь успешно создан" ,
                                    "Информация",
                                    false);
                        }
                    }else {
                        showAlert("Пароли должны совпадать",
                                "Ошибка!",
                                true);
                    }
                }else{
                    showAlert("Заполните все поля",
                            "Ошибка!",
                            true);
                }
            }
        });
    }


}
