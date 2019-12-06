package sample.ChartHandler;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.paint.Color;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ChartController {

    @FXML
    private LineChart<Number, Number> fxChart;

    @FXML
    NumberAxis xAxis;
    @FXML
    NumberAxis yAxis;
    @FXML
    Button openChart;
    @FXML
    Button closeChart;
    @FXML
    Button highChart;
    @FXML
    Button lowChart;
    @FXML
    Button loadData;
    @FXML
    Button clearButton;
    @FXML
    DatePicker dateFrom;
    @FXML
    DatePicker dateTo;
    @FXML
    ColorPicker openColor;
    @FXML
    ColorPicker closeColor;
    @FXML
    ColorPicker highColor;
    @FXML
    ColorPicker lowColor;
    @FXML
    CheckBox checkOpen;
    @FXML
    CheckBox checkClose;
    @FXML
    CheckBox checkHigh;
    @FXML
    CheckBox checkLow;
    @FXML
    Label labelUser;

    ResultSet rs;

    double max;
    double min;
    int i;
    double[] open;
    double[] close;
    double[] high;
    double[] low;

    @FXML
    void initialize() {
        //these are finals for sure
        xAxis.setAutoRanging(false);
        yAxis.setAutoRanging(false);
        yAxis.setLowerBound(0);
        yAxis.setTickUnit(1);
        clearButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                fxChart.getData().clear();
                max = 1;
                min = 2;
            }
        });
        final int count = 0;
        loadData.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                int count = 0;
                i = 0;
                max = 1;
                min = 2;
                try {
                    rs = dataValues.values(dateFrom.getValue(), dateTo.getValue(), labelUser.getText());
                    rs.last();
                    count = rs.getRow();
                    rs.first();
                    open = new double[count];
                    close = new double[count];
                    high = new double[count];
                    low = new double[count];
                    open[i] = Double.parseDouble(rs.getString("open_value"));
                    System.out.println(open[i] + ",");
                    close[i] = Double.parseDouble(rs.getString("close_value"));
                    //System.out.println(close[i]);
                    high[i] = Double.parseDouble(rs.getString("high_value"));
                    //System.out.println(high[i]);
                    low[i] = Double.parseDouble(rs.getString("low_value"));
                    while(rs.next()){
                        i++;
                        open[i] = Double.parseDouble(rs.getString("open_value"));
                        System.out.println(open[i] + ",");
                        close[i] = Double.parseDouble(rs.getString("close_value"));
                        //System.out.println(close[i]);
                        high[i] = Double.parseDouble(rs.getString("high_value"));
                        //System.out.println(high[i]);
                        low[i] = Double.parseDouble(rs.getString("low_value"));
                        //System.out.println(low[i]);

                        max = Math.max(max, max(open[i], close[i], high[i], low[i]));
                        //System.out.println(max + " max");
                        min = Math.min(min, min(open[i], close[i], high[i], low[i]));
                        //System.out.println(min + " min");
                        //i++;
                        xAxis.setLowerBound(min - 0.002);
                        xAxis.setUpperBound(max + 0.002);
                    }
                    xAxis.setTickUnit(0.01);
                    yAxis.setUpperBound(i-1);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                fxChart.getData().clear();
            }
        });

        openChart.setOnAction(event -> {
            buildGraph(fxChart, open, openColor, checkOpen, "open", count);
        });
        closeChart.setOnAction(event -> {
            buildGraph(fxChart, close, closeColor, checkClose, "close", count);
        });
        highChart.setOnAction(event -> {
            buildGraph(fxChart, high, highColor, checkHigh, "high", count);
        });
        lowChart.setOnAction(event -> {
            buildGraph(fxChart, low, lowColor, checkLow, "low", count);
        });
    }

    public static double max(double a, double b, double c, double d) {

        double max = a;

        if (b > max)
            max = b;
        if (c > max)
            max = c;
        if (d > max)
            max = d;

        return max;
    }

    public static double min(double a, double b, double c, double d) {

        double min = a;

        if (b < min)
            min = b;
        if (c < min)
            min = c;
        if (d < min)
            min = d;

        return min;
    }

    public static ArrayList<XYChart.Series<Number, Number>> seriesUni(double[] data, boolean mnk){
        //XYChart.Series<Number, Number> series2 = new XYChart.Series<>();
        ArrayList<XYChart.Series<Number, Number>> r = new ArrayList<>();
        XYChart.Series<Number, Number> series1 = new XYChart.Series<>();
        //доавление точек
        for (int j = 0; j < data.length; j++) {
            series1.getData().add(new XYChart.Data<>(j, data[j]));
        }
        r.add(series1);
        //MNK
        if(mnk){
            XYChart.Series<Number, Number> series_mnk = new XYChart.Series<>();
            //series2.setName("mnk");
            double[] method = dataValues.mnk(data);
            series_mnk.getData().add(new XYChart.Data<>(-1, (-1) * method[0] + method[1]));
            series_mnk.getData().add(new XYChart.Data<>(data.length, (data.length) * method[0] + method[1]));
            System.out.println(method[0]);
            System.out.println(method[1]);
            //fxChart.getData().add(series2);
            r.add(series_mnk);
        }
        return r;
    }

    public static void paint(String color, XYChart.Series<Number, Number> series, boolean isMnk){
        Node line = series.getNode().lookup(".chart-series-line");
        if(isMnk){
            line.setStyle("-fx-stroke: rgba(" + color + ", 1.0); -fx-stroke-width: 2px;");
        }else{
            line.setStyle("-fx-stroke: rgba(" + color + ", 1.0); -fx-stroke: transparent;");
            //красим каждую точку в серии
            for(int index = 0; index<series.getData().size(); index++){
                XYChart.Data dataPoint = series.getData().get(index);
                Node lineSymbol = dataPoint.getNode().lookup(".chart-line-symbol");
                lineSymbol.setStyle("-fx-background-color:rgba(" + color + ", 1.0); /*-fx-background-radius: 0;*/ -fx-padding: 2px ;");
                //lineSymbol.setStyle("-fx-background-color: rgba(" + rgb + ", 1.0);");
            }

        }
    }

    public static String colorToString(Color color){
        String rgb = String.format("%d, %d, %d",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
        return rgb;
    }

    public static void buildGraph(LineChart chart, double[] data, ColorPicker color, CheckBox check, String name, int count){
        ArrayList<XYChart.Series<Number, Number>> list = seriesUni(data, check.isSelected());
        chart.getData().add(list.get(0));
        paint(colorToString(Color.valueOf(color.getValue().toString())), list.get(0), false);
        list.get(0).setName(name);
        if(list.size()!=1){
            chart.getData().add(list.get(1));
            paint(colorToString(Color.valueOf(color.getValue().toString())), list.get(1), true);
            list.get(1).setName(name + " mnk");
        }
    }

    public void setUserLogin(String text) {
        labelUser.setText(text);
    }
}
