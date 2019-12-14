package sample.ChartHandler;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import sample.AlertHandler;
import sample.DataBaseController.dbHandler;
import sample.Formatter;
import sample.MainWindow.MainWindowController;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ChartController {

    @FXML
    private LineChart<CategoryAxis, Number> fxChart;
    @FXML
    NumberAxis xAxis;
    @FXML
    CategoryAxis yAxis;
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
    //ДИКИЙ РЕФАКТОР
    @FXML
    ComboBox<String> comboPrice;
    ObservableList<String> price = FXCollections.observableArrayList(
            "open",
            "close",
            "high",
            "low"
    );
    @FXML
    ComboBox<String> comboInstrument;
    ObservableList<String> instrument = FXCollections.observableArrayList(
            "linear",
            "square",
            "exp",
            "hyper"
    );
    @FXML
    Button universalAdd;
    @FXML
    Button universalClearAdd;
    @FXML
    Label koefsLabel;
    @FXML
    Label avgMistakeLabel;
    @FXML
    Button backToMain;

    ResultSet rs;

    double max;
    double min;
    int i;
    double[] open;
    double[] close;
    double[] high;
    double[] low;
    Date[] date;
    String lowest_date, highest_date;

    @FXML
    void initialize() {
        comboPrice.setItems(price);
        comboInstrument.setItems(instrument);
        fxChart.setAnimated(false);
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
    }
    @FXML
    void back(){
        Stage stage;
        stage = (Stage) backToMain.getScene().getWindow();
        backToMain.getScene().getWindow().hide();

        FXMLLoader loader = new FXMLLoader(
                getClass().
                        getResource("../MainWindow/main.fxml")
        );
        Parent root = null;
        try {
            root = (Parent) loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        MainWindowController mwc = loader.getController();
        mwc.setUserLogin(labelUser.getText());
        //stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setMaximized(false);
        stage.show();
    }
    @FXML
    void loadInfo(){
        fxChart.setLegendVisible(false);
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
                lowest_date = rs.getString("date_value");
                highest_date = "";
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
                Date pick_date1 = Formatter.convertToDateViaSqlDate(dateFrom.getValue());
                Date pick_date2 = Formatter.convertToDateViaSqlDate(dateTo.getValue());
                if((pick_date1.compareTo(date1) == -1) || (pick_date2.compareTo(date2) == 1)){  //pick_date1 < date1 || date2 < pick_date2
                    String lowest_date = ((pick_date1.compareTo(date1) == -1)) ? date1.toString() : pick_date1.toString();
                    this.lowest_date = lowest_date;
                    String highest_date = ((pick_date2.compareTo(date2) == 1)) ? date2.toString() : pick_date2.toString();
                    this.highest_date = highest_date;
                    String msg = "Показаны доступные данные в диапазоне между  \n" +
                            lowest_date + "\n" +
                            highest_date;
                    AlertHandler.showAlert(msg, "Диапазон", false);
                }
                xAxis.setTickUnit(0.01);
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
    @FXML
    void draw(){}
    @FXML
    void drawUniversal(){
        String color = openColor.getValue().toString();
        System.out.println(color + " YA COLOR");
        double[] tmp = null;
        switch(comboPrice.getValue()){
            case "open":
                tmp = open;
                break;
            case "close":
                tmp = close;
                break;
            case "high":
                tmp = high;
                break;
            case "low":
                tmp = low;
                break;
        }
        LineChart chart = fxChart;
        buildGraph(chart, tmp, date, color);
        XYChart.Series<String, Number> approx = buildUniversal(tmp);
        chart.getData().add(approx);
        paint(Formatter.colorToString(Color.valueOf(color.toString())), approx, true);
    }
    @FXML
    void saveResults(){
        int inst=0;
        switch (comboInstrument.getValue()){
            case "linear":
                inst = 1;
                break;
            case "square":
                inst = 2;
                break;
            case "exp":
                inst = 3;
                break;
            case "hyper":
                inst = 4;
                break;
        }
        String time_bounds = lowest_date + "-" + highest_date;
        dbHandler.logData(labelUser.getText(), 2, time_bounds, inst, koefsLabel.getText() +" "+ avgMistakeLabel.getText());
        System.out.println(koefsLabel.getText() +" "+ avgMistakeLabel.getText() + " " + inst + " " + time_bounds);
    }

    public XYChart.Series<String, Number> buildUniversal(double[] y){
        int inst = 0;
        XYChart.Series<String, Number> series_mnk = new XYChart.Series<>();
        double[] method = null;
        switch (comboInstrument.getValue()){
            case "linear":
                method = dataValues.mnk(y);
                series_mnk.getData().add(new XYChart.Data<>(date[0].toString(), (-1) * method[0] + method[1]));
                series_mnk.getData().add(new XYChart.Data<>(date[date.length-1].toString(), (y.length) * method[0] + method[1]));
                inst = 1;
                break;
            case "square":
                method = dataValues.sqrtStat(y);
                for (int i = 0; i < y.length; i++) {
                    series_mnk.getData().add(new XYChart.Data<>(date[i].toString(), (i*i) * method[0] +i * method[1] + method[2]));
                }
                inst = 2;
                break;
            case "exp":
                method = dataValues.expStat(y);
                double a = Math.exp(method[0]);
                double b = method[1];
                for (int i = 0; i < y.length; i++) {
                    series_mnk.getData().add(new XYChart.Data<>(date[i].toString(), a * Math.exp(b * i)));
                }
                inst = 3;
                break;
            case "hyper":
                method = dataValues.hyperStat(y);
                for (int i = 1; i < y.length; i++) {
                    series_mnk.getData().add(new XYChart.Data<>(date[i].toString(), method[0] + method[1]/i));
                }
                inst = 4;
                break;
        }
        printAns(method);
        return series_mnk;
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

    public void buildGraph(LineChart chart, double[] data, Date[] date, String color){
        XYChart.Series<String, Number> series1 = new XYChart.Series<>();
        //доавление точек
        for (int j = 0; j < data.length; j++) {
            series1.getData().add(new XYChart.Data<>(date[j].toString(), data[j]));
        }
        chart.getData().add(series1);
        paint(Formatter.colorToString(Color.valueOf(color.toString())), series1, false);
    }

    public void setUserLogin(String text) {
        labelUser.setText(text);
    }
    public void printAns(double[]y){
        String a = String.format("%.5f", y[0]);
        String b = String.format("%.5f", y[1]);
        String c = String.format("%.5f", y[2]);
        String answer, mistake;
        if(y.length == 4){
            String d = String.format("%.5f", y[3]);
            mistake = d + "%";
            avgMistakeLabel.setText(mistake);
            answer = "a="+a+"\n b="+b+" \n c=" + c;
        }else{
            mistake = c + "%";
            avgMistakeLabel.setText(mistake);
            answer = "a="+a+"\n b="+b;
        }
        koefsLabel.setText(answer);

    }
}
