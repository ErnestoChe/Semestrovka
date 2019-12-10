package sample.ChartHandler;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import sample.AlertHandler;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

public class ChartController {

    @FXML
    private LineChart<CategoryAxis, Number> fxChart;
    @FXML
    NumberAxis xAxis;
    @FXML
    CategoryAxis yAxis;
    //charts button
    @FXML
    Button openChart;
    @FXML
    Button closeChart;
    @FXML
    Button highChart;
    @FXML
    Button lowChart;
    @FXML
    Button clearButton;
    //loads data in time diapason
    @FXML
    Button loadData;
    @FXML
    DatePicker dateFrom;
    @FXML
    DatePicker dateTo;
    //colors
    @FXML
    ColorPicker openColor;
    @FXML
    ColorPicker closeColor;
    @FXML
    ColorPicker highColor;
    @FXML
    ColorPicker lowColor;
    //linear approximations
    @FXML
    CheckBox checkOpen;
    @FXML
    CheckBox checkClose;
    @FXML
    CheckBox checkHigh;
    @FXML
    CheckBox checkLow;
    @FXML
    Label linLabelOpen;
    @FXML
    Label linLabelClose;
    @FXML
    Label linLabelHigh;
    @FXML
    Label linLabelLow;
    //sqr approximations
    @FXML
    CheckBox sqrOpen;
    @FXML
    CheckBox sqrClose;
    @FXML
    CheckBox sqrHigh;
    @FXML
    CheckBox sqrLow;
    @FXML
    Label sqrLabelOpen;
    @FXML
    Label sqrLabelClose;
    @FXML
    Label sqrLabelHigh;
    @FXML
    Label sqrLabelLow;
    //exp
    @FXML
    CheckBox expOpen;
    @FXML
    Label expLabelOpen;
    //util
    @FXML
    Label labelUser;


    private ObservableList<Day> daysData = FXCollections.observableArrayList();
    @FXML
    private TableView<Day> tableDays;
    @FXML
    private TableColumn<Day, String> dateColumn;
    @FXML
    private TableColumn<Day, Double> openColumn;
    @FXML
    private TableColumn<Day, Double> closeColumn;
    @FXML
    private TableColumn<Day, Double> highColumn;
    @FXML
    private TableColumn<Day, Double> lowColumn;


    ResultSet rs;

    double max;
    double min;
    int i;
    double[] open;
    double[] close;
    double[] high;
    double[] low;
    Date[] date;

    @FXML
    void initialize() {
        //these are finals for sure
        xAxis.setAutoRanging(false);
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
                daysData.clear();
                int count = 0;
                i = 0;
                max = 1;
                min = 2;
                try {
                    rs = dataValues.values(dateFrom.getValue(), dateTo.getValue(), labelUser.getText());
                    rs.last();
                    count = rs.getRow();
                    if(count == 0){
                        System.out.println("нулевой резултсет, чекай данные))00))");
                        AlertHandler.showAlert("Нет данных за этот период", "Ошибка", true);
                    }else{
                        rs.first();
                        String lowest_date = rs.getString("date_value");
                        String highest_date = "";
                        open = new double[count];
                        close = new double[count];
                        high = new double[count];
                        low = new double[count];
                        date = new Date[count];
                        open[i] = Double.parseDouble(rs.getString("open_value"));
                        close[i] = Double.parseDouble(rs.getString("close_value"));
                        high[i] = Double.parseDouble(rs.getString("high_value"));
                        low[i] = Double.parseDouble(rs.getString("low_value"));
                        date[i] = new SimpleDateFormat("yyyy-MM-dd").parse(lowest_date);
                        daysData.add(new Day(date[i].toString(), open[i], close[i], high[i], low[i]));
                        while(rs.next()){
                            //System.out.println(open[i] + ",");
                            System.out.println(date[i]);
                            //System.out.println(close[i]);
                            //System.out.println(high[i]);
                            //System.out.println(low[i]);
                            i++;
                            open[i] = Double.parseDouble(rs.getString("open_value"));
                            close[i] = Double.parseDouble(rs.getString("close_value"));
                            high[i] = Double.parseDouble(rs.getString("high_value"));
                            low[i] = Double.parseDouble(rs.getString("low_value"));
                            date[i] = new SimpleDateFormat("yyyy-MM-dd").parse(rs.getString("date_value"));
                            daysData.add(new Day(date[i].toString(), open[i], close[i], high[i], low[i]));

                            max = Math.max(max, max(open[i], close[i], high[i], low[i]));
                            min = Math.min(min, min(open[i], close[i], high[i], low[i]));
                            xAxis.setLowerBound(min - 0.001);
                            xAxis.setUpperBound(max + 0.001);
                            highest_date = rs.getString("date_value");
                            date[i] = new SimpleDateFormat("yyyy-MM-dd").parse(highest_date);
                        }
                        //System.out.println(lowest_date + " lowest date ");
                        //System.out.println(highest_date + " highest date");
                        Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(lowest_date);
                        Date date2 = new SimpleDateFormat("yyyy-MM-dd").parse(highest_date);
                        Date pick_date1 = convertToDateViaSqlDate(dateFrom.getValue());
                        Date pick_date2 = convertToDateViaSqlDate(dateTo.getValue());
                        if((pick_date1.compareTo(date1) == -1) || (pick_date2.compareTo(date2) == 1)){  //pick_date1 < date1 || date2 < pick_date2
                            //System.out.println(pick_date1.toString() + " asdasd " + date2.toString());
                            //System.out.println("показаны доступные данные между");
                            String msg = "Показаны доступные данные в диапазоне между  \n" +
                                    (((pick_date1.compareTo(date1) == -1)) ? date1.toString() : pick_date1.toString()) + "\n" +
                                    (((pick_date2.compareTo(date2) == 1)) ? date2.toString() : pick_date2.toString());
                            AlertHandler.showAlert(msg, "Диапазон", false);
                            //System.out.println(((pick_date1.compareTo(date1) == -1)) ? date1 : pick_date1);
                            //System.out.println(((pick_date2.compareTo(date2) == 1)) ? date2 : pick_date2);
                        }
                        xAxis.setTickUnit(0.01);
                        //yAxis.setUpperBound(i-1);
                    }
                } catch (SQLException | ParseException e) {
                    e.printStackTrace();
                }
                dateColumn.setCellValueFactory(new PropertyValueFactory<Day, String>("date"));
                openColumn.setCellValueFactory(new PropertyValueFactory<Day, Double>("open"));
                closeColumn.setCellValueFactory(new PropertyValueFactory<Day, Double>("close"));
                highColumn.setCellValueFactory(new PropertyValueFactory<Day, Double>("high"));
                lowColumn.setCellValueFactory(new PropertyValueFactory<Day, Double>("low"));

                tableDays.setItems(daysData);
                fxChart.getData().clear();

            }
        });

        openChart.setOnAction(event -> {
            buildGraph(fxChart, open, date, openColor, "open");
            if(checkOpen.isSelected()){
                buildLinear(fxChart, open, date, openColor, "open", linLabelOpen);
            }
            if(sqrOpen.isSelected()){
                buildSqr(fxChart, open, date, openColor, "open", sqrLabelOpen);
            }
            if(expOpen.isSelected()){
                buildExp(fxChart, open, date, openColor, "open", expLabelOpen);
            }
        });
        closeChart.setOnAction(event -> {
            buildGraph(fxChart, close, date,closeColor, "close");
            if(checkClose.isSelected()){
                buildLinear(fxChart, close, date, closeColor, "close", linLabelClose);
            }
            if(sqrClose.isSelected()){
                buildSqr(fxChart, close, date, closeColor, "close", sqrLabelClose);
            }
        });
        highChart.setOnAction(event -> {
            buildGraph(fxChart, high, date,  highColor, "high");
            if(checkHigh.isSelected()){
                buildLinear(fxChart, high, date, highColor, "high", linLabelHigh);
            }
            if(sqrHigh.isSelected()){
                buildSqr(fxChart, high, date, highColor, "high", sqrLabelHigh);
            }
        });
        lowChart.setOnAction(event -> {
            buildGraph(fxChart, low, date, lowColor, "low");
            if(checkLow.isSelected()){
                buildLinear(fxChart, low, date, lowColor, "low", linLabelLow);
            }
            if(sqrLow.isSelected()){
                buildSqr(fxChart, low, date, lowColor, "low", sqrLabelLow);
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

    public static XYChart.Series<String, Number> seriesUni(Date[] date, double[] data){
        //XYChart.Series<Number, Number> series2 = new XYChart.Series<>();
        //ArrayList<XYChart.Series<String, Number>> r = new ArrayList<>();
        XYChart.Series<String, Number> series1 = new XYChart.Series<>();
        //доавление точек
        for (int j = 0; j < data.length; j++) {
            series1.getData().add(new XYChart.Data<>(date[j].toString(), data[j]));
        }
        //r.add(series1);
        //MNK
        /*if(mnk){
            XYChart.Series<String, Number> series_mnk = new XYChart.Series<>();
            //series2.setName("mnk");
            double[] method = dataValues.mnk(data);
            series_mnk.getData().add(new XYChart.Data<>(date[0].toString(), (-1) * method[0] + method[1]));
            series_mnk.getData().add(new XYChart.Data<>(date[date.length-1].toString(), (data.length) * method[0] + method[1]));
            System.out.println(method[0]);
            System.out.println(method[1]);
            //fxChart.getData().add(series2);
            r.add(series_mnk);
        }*/
        return series1;
    }

    public static void paint(String color, XYChart.Series<String, Number> series, boolean isMnk){
        Node line = series.getNode().lookup(".chart-series-line");
        if(isMnk){
            for(int index = 0; index<series.getData().size(); index++){
                XYChart.Data dataPoint = series.getData().get(index);
                Node lineSymbol = dataPoint.getNode().lookup(".chart-line-symbol");
                lineSymbol.setStyle("-fx-background-color:rgba(" + color + ", 1.0); /*-fx-background-radius: 0;*/ -fx-padding: 0px ;");
            }
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

    public static void buildGraph(LineChart chart, double[] data, Date[] date, ColorPicker color, String name){
        XYChart.Series<String, Number> list = seriesUni(date, data);
        chart.getData().add(list);
        paint(colorToString(Color.valueOf(color.getValue().toString())), list, false);
        list.setName(name);

        /*if(list.size()!=1){
            chart.getData().add(list.get(1));
            paint(colorToString(Color.valueOf(color.getValue().toString())), list.get(1), true);
            list.get(1).setName(name + " mnk");
        }*/
    }

    public static void buildLinear(LineChart chart, double[] data, Date[] date, ColorPicker color, String name, Label label){
        XYChart.Series<String, Number> series_mnk = new XYChart.Series<>();
        //series2.setName("mnk");
        double[] method = dataValues.mnk(data);
        series_mnk.getData().add(new XYChart.Data<>(date[0].toString(), (-1) * method[0] + method[1]));
        series_mnk.getData().add(new XYChart.Data<>(date[date.length-1].toString(), (data.length) * method[0] + method[1]));
        String a = String.format("%.5f", method[0]);
        String b = String.format("%.5f", method[1]);
        label.setText("a = " + a + "\n b = " + b);
        chart.getData().add(series_mnk);
        paint(colorToString(Color.valueOf(color.getValue().toString())), series_mnk, true);
        series_mnk.setName(name + "mnk");
    }

    public static void buildSqr(LineChart chart, double[] data, Date[] date, ColorPicker color, String name, Label label){
        XYChart.Series<String, Number> series_mnk = new XYChart.Series<>();
        double[] method = dataValues.sqrtStat(data);
        for (int i = 0; i < data.length; i++) {
            series_mnk.getData().add(new XYChart.Data<>(date[i].toString(), (i*i) * method[0] +i * method[1] + method[2]));
        }
        System.out.println(method[0]);
        System.out.println(method[1]);
        System.out.println(method[2]);
        chart.getData().add(series_mnk);
        String a = String.format("%.5f", method[0]);
        String b = String.format("%.5f", method[1]);
        String c = String.format("%.5f", method[2]);
        String ans = "a=" + a + "\n b=" + b + "\n c=" + c;
        label.setText(ans);
        paint(colorToString(Color.valueOf(color.getValue().toString())), series_mnk, true);
        series_mnk.setName(name + "sqr");
    }

    public static void buildExp(LineChart chart, double[] data, Date[] date, ColorPicker color, String name, Label label){
        double[] k = dataValues.expStat(data);
        double a = Math.exp(k[0]);
        double b = k[1];
        System.out.println();
        System.out.println(k[1]);
        XYChart.Series<String, Number> series_mnk = new XYChart.Series<>();
        for (int i = 0; i < data.length; i++) {
            series_mnk.getData().add(new XYChart.Data<>(date[i].toString(), a * Math.exp(b * i)));
        }
        chart.getData().add(series_mnk);
        paint(colorToString(Color.valueOf(color.getValue().toString())), series_mnk, true);
        series_mnk.setName(name + "exp");
    }

    public void setUserLogin(String text) {
        labelUser.setText(text);
    }

    public Date convertToDateViaSqlDate(LocalDate dateToConvert) {
        return java.sql.Date.valueOf(dateToConvert);
    }
}
