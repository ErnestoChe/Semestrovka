package sample.MainWindow;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sample.ChartHandler.ChartController;
import sample.FileSelection.Parser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainWindowController implements Initializable {

    @FXML
    private Button localFileButton;

    @FXML
    private Button webFileButton;

    @FXML
    private TextField urlText;

    @FXML
    private Button useDataButton;

    @FXML
    private Label currentUserLabel;

    @FXML
    private Button logoutButton;

    @FXML
    void initialize() {
    }

    public void setUserLogin(String login)
    {
        currentUserLabel.setText(login);
        System.out.println(login);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        logoutButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Parent root;
                Stage stage = (Stage) logoutButton.getScene().getWindow();
                logoutButton.getScene().getWindow().hide();
                System.out.println("main back to login button");
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
        useDataButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String currentUser = currentUserLabel.getText();
                System.out.println(currentUser);
                //Parent root;
                Stage stage = (Stage) useDataButton.getScene().getWindow();
                useDataButton.getScene().getWindow().hide();
                System.out.println("main to charts button");
                try {

                    FXMLLoader loader = new FXMLLoader(
                            getClass().
                                    getResource("/sample/ChartHandler/charts.fxml")
                    );
                    Parent root = (Parent) loader.load();
                    ChartController chc = loader.getController();
                    chc.setUserLogin(currentUser);
                    //stage = new Stage();
                    stage.setScene(new Scene(root));
                    stage.setMaximized(true);
                    stage.show();
                    /*root = FXMLLoader.load(getClass().getResource("/sample/ChartHandler/charts.fxml"));
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();*/
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        localFileButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String currentUser = currentUserLabel.getText();
                FileChooser file_chooser = new FileChooser();
                File file = file_chooser.showOpenDialog(localFileButton.getScene().getWindow());
                String curr = currentUserLabel.getText();
                if (file != null) {
                    System.out.println("хочю файл");
                    Parser parser = new Parser();
                    parser.readData(file);
                    parser.sendData(currentUser);
                }
            }
        });

    }
}
