package sample.ChartHandler;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.DatePicker;
import javafx.scene.paint.Color;

import java.sql.ResultSet;
import java.sql.SQLException;

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

    ResultSet rs;

    double max;
    double min;

    @FXML
    void initialize() {
        //these are finals for sure
        xAxis.setAutoRanging(false);
        yAxis.setAutoRanging(false);
        yAxis.setLowerBound(0);
        yAxis.setTickUnit(1);
        double[] open;
        clearButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                fxChart.getData().clear();
                max = 1;
                min = 2;
            }
        });

        loadData.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                int count = 0;
                try {
                    rs = dataValues.values(/*dateFrom.getValue(), dateTo.getValue()*/);
                    rs.last();
                    count = rs.getRow();
                    rs.first();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                fxChart.getData().clear();
                max = 1;
                min = 2;
            }
        });

        openChart.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //open values
                XYChart.Series<Number, Number> series1 = new XYChart.Series<>();
                int i = 0;
                try {
                    while(rs.next()) {
                        System.out.println(rs.getString("open_value"));
                        double open_tmp =Double.parseDouble(rs.getString("open_value"));
                        max = Math.max(open_tmp, max);
                        min = Math.min(open_tmp, min);
                        series1.getData().add(new XYChart.Data<>(i, open_tmp));
                        i++;
                    }
                    yAxis.setUpperBound(i-1);
                    fxChart.getData().add(series1);
                    Node line = series1.getNode().lookup(".chart-series-line");
                    Color color = Color.valueOf(openColor.getValue().toString() );
                    String rgb = String.format("%d, %d, %d",
                            (int) (color.getRed() * 255),
                            (int) (color.getGreen() * 255),
                            (int) (color.getBlue() * 255));

                    line.setStyle("-fx-stroke: rgba(" + rgb + ", 1.0);/*-fx-background-color: white, blue;*/");
                    fxChart.setCreateSymbols(false);
                    xAxis.setTickUnit(0.01);
                    xAxis.setLowerBound(min - 0.002);
                    xAxis.setUpperBound(max + 0.002);
                    series1.setName("open");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

        closeChart.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //open values
                XYChart.Series<Number, Number> series2 = new XYChart.Series<>();
                int i = 0;
                try {
                    while(rs.next()) {
                        System.out.println(rs.getString("close_value"));
                        double close_tmp =Double.parseDouble(rs.getString("close_value"));
                        max = Math.max(close_tmp, max);
                        min = Math.min(close_tmp, min);
                        series2.getData().add(new XYChart.Data<>(i, close_tmp));
                        i++;
                    }
                    yAxis.setUpperBound(i-1);
                    fxChart.getData().add(series2);
                    Node line2 = series2.getNode().lookup(".chart-series-line");
                    Color color = Color.valueOf(closeColor.getValue().toString() );
                    String rgb = String.format("%d, %d, %d",
                            (int) (color.getRed() * 255),
                            (int) (color.getGreen() * 255),
                            (int) (color.getBlue() * 255));

                    line2.setStyle("-fx-stroke: rgba(" + rgb + ", 1.0);/*-fx-background-color: white, blue;*/");
                    fxChart.setCreateSymbols(false);
                    xAxis.setTickUnit(0.01);
                    xAxis.setLowerBound(min - 0.002);
                    xAxis.setUpperBound(max + 0.002);
                    series2.setName("close");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
