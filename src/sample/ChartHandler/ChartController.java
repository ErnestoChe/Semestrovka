package sample.ChartHandler;

import javafx.application.Platform;
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
    @FXML
    ColorPicker highColor;
    @FXML
    ColorPicker lowColor;

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

        loadData.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                int count = 0;
                i = 0;
                max = 1;
                min = 2;
                try {
                    rs = dataValues.values(/*dateFrom.getValue(), dateTo.getValue()*/);
                    rs.last();
                    count = rs.getRow();
                    rs.first();
                    open = new double[count];
                    close = new double[count];
                    high = new double[count];
                    low = new double[count];
                    while(rs.next()){
                        open[i] = Double.parseDouble(rs.getString("open_value"));
                        System.out.println(open[i]);
                        close[i] = Double.parseDouble(rs.getString("close_value"));
                        System.out.println(close[i]);
                        high[i] = Double.parseDouble(rs.getString("high_value"));
                        System.out.println(high[i]);
                        low[i] = Double.parseDouble(rs.getString("low_value"));
                        System.out.println(low[i]);
                        max = Math.max(max, max(open[i], close[i], high[i], low[i]));
                        System.out.println(max + " max");
                        min = Math.min(min, min(open[i], close[i], high[i], low[i]));
                        System.out.println(min + " min");
                        i++;
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
        //закомментировано будет только одно построение так как остальные аналогичны
        openChart.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //open values
                //"серия" данных, в которую добавляются все координаты точек
                XYChart.Series<Number, Number> series1 = new XYChart.Series<>();
                //доавление точек
                for (int j = 0; j < i; j++) {
                    series1.getData().add(new XYChart.Data<>(j, open[j]));
                }
                //добавление серии в построение графика
                fxChart.getData().add(series1);
                //обращение к серии в обход CSS
                Node line = series1.getNode().lookup(".chart-series-line");
                //читаем цвет с колор пикера
                Color color = Color.valueOf(openColor.getValue().toString() );
                //форматирование цвета с колопикера в строку
                String rgb = String.format("%d, %d, %d",
                        (int) (color.getRed() * 255),
                        (int) (color.getGreen() * 255),
                        (int) (color.getBlue() * 255));
                //меняется цвет линии, "отделяем" точки друг от друга
                line.setStyle("-fx-stroke: rgba(" + rgb + ", 1.0); -fx-stroke: transparent;");
                //красим каждую точку в серии
                for(int index = 0; index<series1.getData().size(); index++){
                    XYChart.Data dataPoint = series1.getData().get(index);
                    Node lineSymbol = dataPoint.getNode().lookup(".chart-line-symbol");
                    lineSymbol.setStyle("-fx-background-color:rgba(" + rgb + ", 1.0), white; /*-fx-background-radius: 0;*/ -fx-padding: 2px ;");
                    //lineSymbol.setStyle("-fx-background-color: rgba(" + rgb + ", 1.0);");
                }
                //делаем точки видимыми
                fxChart.setCreateSymbols(true);
                //fxChart.setCreateSymbols(false);

                //имя серии на легенде
                series1.setName("open");
                //красим легенду(сложнее всего)
                //Node legend = series1.getNode().lookup(".chart-legend-item-symbol");
                //legend.setStyle("-fx-background-color: rgba(" + rgb + ", 1.0), white;");
            }
        });

        closeChart.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //close
                //"серия" данных, в которую добавляются все координаты точек
                XYChart.Series<Number, Number> series2 = new XYChart.Series<>();
                //доавление точек
                for (int j = 0; j < i; j++) {
                    series2.getData().add(new XYChart.Data<>(j, close[j]));
                }
                //добавление серии в построение графика
                fxChart.getData().add(series2);
                //обращение к серии в обход CSS
                Node line = series2.getNode().lookup(".chart-series-line");
                //читаем цвет с колор пикера
                Color color = Color.valueOf(closeColor.getValue().toString() );
                //форматирование цвета с колопикера в строку
                String rgb = String.format("%d, %d, %d",
                        (int) (color.getRed() * 255),
                        (int) (color.getGreen() * 255),
                        (int) (color.getBlue() * 255));
                //меняется цвет линии, "отделяем" точки друг от друга
                line.setStyle("-fx-stroke: rgba(" + rgb + ", 1.0); -fx-stroke: transparent;");
                //красим каждую точку в серии
                for(int index = 0; index<series2.getData().size(); index++){
                    XYChart.Data dataPoint = series2.getData().get(index);
                    Node lineSymbol = dataPoint.getNode().lookup(".chart-line-symbol");
                    //lineSymbol.setStyle("-fx-background-color: rgba(" + rgb + ", 1.0), white;");
                    lineSymbol.setStyle("-fx-background-color:rgba(" + rgb + ", 1.0), white; /*-fx-background-radius: 0;*/ -fx-padding: 2px ;");
                }
                //делаем точки видимыми
                fxChart.setCreateSymbols(true);
                //имя серии на легенде
                series2.setName("close");
                //красим легенду(сложнее всего)

            }
        });

        highChart.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //high
                //"серия" данных, в которую добавляются все координаты точек
                XYChart.Series<Number, Number> series2 = new XYChart.Series<>();
                //доавление точек
                for (int j = 0; j < i; j++) {
                    series2.getData().add(new XYChart.Data<>(j, high[j]));
                }
                //добавление серии в построение графика
                fxChart.getData().add(series2);
                //обращение к серии в обход CSS
                Node line = series2.getNode().lookup(".chart-series-line");
                //читаем цвет с колор пикера
                Color color = Color.valueOf(highColor.getValue().toString() );
                //форматирование цвета с колопикера в строку
                String rgb = String.format("%d, %d, %d",
                        (int) (color.getRed() * 255),
                        (int) (color.getGreen() * 255),
                        (int) (color.getBlue() * 255));
                //меняется цвет линии, "отделяем" точки друг от друга
                line.setStyle("-fx-stroke: rgba(" + rgb + ", 1.0); -fx-stroke: transparent;");
                //красим каждую точку в серии
                for(int index = 0; index<series2.getData().size(); index++){
                    XYChart.Data dataPoint = series2.getData().get(index);
                    Node lineSymbol = dataPoint.getNode().lookup(".chart-line-symbol");
                    //lineSymbol.setStyle("-fx-background-color: rgba(" + rgb + ", 1.0), white;");
                    lineSymbol.setStyle("-fx-background-color:rgba(" + rgb + ", 1.0), white; /*-fx-background-radius: 0;*/ -fx-padding: 2px ;");
                }
                //делаем точки видимыми
                fxChart.setCreateSymbols(true);
                //имя серии на легенде
                series2.setName("high");
                //красим легенду(сложнее всего)

            }
        });

        lowChart.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //low
                //"серия" данных, в которую добавляются все координаты точек
                XYChart.Series<Number, Number> series2 = new XYChart.Series<>();
                //доавление точек
                for (int j = 0; j < i; j++) {
                    series2.getData().add(new XYChart.Data<>(j, low[j]));
                }
                //добавление серии в построение графика
                fxChart.getData().add(series2);
                //обращение к серии в обход CSS
                Node line = series2.getNode().lookup(".chart-series-line");
                //читаем цвет с колор пикера
                Color color = Color.valueOf(lowColor.getValue().toString() );
                //форматирование цвета с колопикера в строку
                String rgb = String.format("%d, %d, %d",
                        (int) (color.getRed() * 255),
                        (int) (color.getGreen() * 255),
                        (int) (color.getBlue() * 255));
                //меняется цвет линии, "отделяем" точки друг от друга
                line.setStyle("-fx-stroke: rgba(" + rgb + ", 1.0); -fx-stroke: transparent;");
                //красим каждую точку в серии
                for(int index = 0; index<series2.getData().size(); index++){
                    XYChart.Data dataPoint = series2.getData().get(index);
                    Node lineSymbol = dataPoint.getNode().lookup(".chart-line-symbol");
                    //lineSymbol.setStyle("-fx-background-color: rgba(" + rgb + ", 1.0), white;");
                    lineSymbol.setStyle("-fx-background-color:rgba(" + rgb + ", 1.0), white; /*-fx-background-radius: 0;*/ -fx-padding: 2px ;");
                }
                //делаем точки видимыми
                fxChart.setCreateSymbols(true);
                //имя серии на легенде
                series2.setName("low");
                //красим легенду(сложнее всего)

            }
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
}
