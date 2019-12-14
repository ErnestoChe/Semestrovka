package sample;

import javafx.scene.paint.Color;

import java.time.LocalDate;
import java.util.Date;

public class Formatter {
    public static Date convertToDateViaSqlDate(LocalDate dateToConvert) {
        return java.sql.Date.valueOf(dateToConvert);
    }
    public static String colorToString(Color color){
        String rgb = String.format("%d, %d, %d",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
        return rgb;
    }
}
