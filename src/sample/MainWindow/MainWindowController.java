package sample.MainWindow;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import sample.FileSelection.Parser;

import java.io.File;
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

        useDataButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String currentUser = currentUserLabel.getText();
                System.out.println(currentUser);

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
