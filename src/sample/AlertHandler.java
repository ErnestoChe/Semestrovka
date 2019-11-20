package sample;

import javafx.scene.control.Alert;

public class AlertHandler {
    public static void showAlert(String alertText, String alertTitle, boolean isError){
        Alert alert_info;
        if(isError){
            alert_info = new Alert(Alert.AlertType.WARNING);
            alert_info.setContentText(alertText);
            alert_info.setTitle(alertTitle);
        }else{
            alert_info = new Alert(Alert.AlertType.INFORMATION);
            alert_info.setContentText(alertText);
            alert_info.setTitle(alertTitle);
        }
        alert_info.showAndWait();
    }
}
