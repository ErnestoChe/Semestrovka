package sample.ChartHandler;

import sample.DataBaseController.dbConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class dataValues {
    public static ResultSet values(LocalDate d1, LocalDate d2, String username) throws SQLException {
        String date1 = d1.toString();
        String date2 = d2.toString();

        String sql = "SELECT * FROM data_values where (username = '" + username +"' and date_value BETWEEN '"+ date1 + "' AND '"+ date2+"');";
        Connection conn = dbConnection.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql,ResultSet.TYPE_SCROLL_SENSITIVE,
                ResultSet.CONCUR_UPDATABLE);
        ResultSet rs = pstmt.executeQuery();
        return rs;
    }

    public static double[] mnk(double[] y){
        double[] r = new double[3];
        int n = y.length;
        double sum_y = 0;
        double sum_x_sqr = 0;
        double sum_xy = 0;
        double sum_x = 0;

        double[] x = new double[n];

        for (int i = 0; i < n; i++) {
            x[i] = i;
            sum_y += y[i];
            sum_x_sqr += x[i] * x[i];
            sum_xy += x[i] * y[i];
            sum_x += x[i];
        }
        r[0] = (n * sum_xy - sum_x*sum_y)/(n * sum_x_sqr - sum_x * sum_x);
        r[1] = (sum_y - r[0] * sum_x) / n;
        double[] approx = new double[n];
        for (int i = 0; i < n; i++) {
            approx[i] = r[0] * y[i] + r[1];
        }
        System.out.println("Average mistake for linear = " + avgMistake(y, approx));
        r[2] = avgMistake(y, approx);
        return r;
    }

    public static double[] sqrtStat(double[] y){
        double[] r = new double[4];
        int n = y.length;
        double sum_y = 0;
        double sum_xy = 0;
        double sum_x2y = 0;
        double sum_x = 0;
        double sum_x_2 = 0;
        double sum_x_3 = 0;
        double sum_x_4 = 0;
        for (int i = 0; i < n; i++) {
            sum_y += y[i];
            sum_xy += y[i] * i;
            sum_x2y += y[i] * i * i;
            sum_x += i;
            sum_x_2 += i*i;
            sum_x_3 += i*i*i;
            sum_x_4 += i*i*i*i;
        }
        double det = n * sum_x_2 * sum_x_4 + sum_x_3 * sum_x * sum_x_2 + sum_x_3 * sum_x * sum_x_2
                - sum_x_2 * sum_x_2 * sum_x_2 - sum_x_3 * sum_x_3 * n - sum_x_4 * sum_x * sum_x;
        double det1 = n * sum_x_2 * sum_x2y + sum_y * sum_x * sum_x_3 + sum_xy * sum_x * sum_x_2
                - sum_y * sum_x_2 * sum_x_2 - sum_x_3 * n * sum_xy - sum_x2y * sum_x * sum_x;
        double det2 = n * sum_xy * sum_x_4 + sum_x2y * sum_x * sum_x_2 + sum_x_3 * sum_y * sum_x_2
                - sum_x_2 * sum_x_2 * sum_xy - sum_x_3 * sum_x2y * n - sum_x_4 * sum_y * sum_x;
        double det3 = sum_y * sum_x_2 * sum_x_4 + sum_x_3 * sum_xy * sum_x_2 + sum_x_3 * sum_x * sum_x2y
                - sum_x_2 * sum_x_2 * sum_x2y - sum_x_3 * sum_x_3 * sum_y - sum_x_4 * sum_x * sum_xy;
        r[0] = det1/det;
        r[1] = det2/det;
        r[2] = det3/det;
        double[] approx = new double[n];
        for (int i = 0; i < n; i++) {
            approx[i] = r[0] * y[i] * y[i] + r[1] * y[i] + r[2];
        }
        System.out.println("Average mistake for square = " + avgMistake(y, approx));
        r[3] = avgMistake(y, approx);
        return r;
    }

    public static double[] expStat(double[] y){
        double[] r = new double[3];
        int n = y.length;
        double sum_lny = 0;
        double sum_x2 = 0;
        double sum_lny_x=0;
        double sum_x=0;
        for (int i = 0; i < n; i++) {
            sum_lny += Math.log(y[i]);
            sum_lny_x += Math.log(y[i]) * i;
            sum_x2 += i*i;
            sum_x +=i;
        }
        r[0] = (sum_lny * sum_x2 - sum_lny_x*sum_x) / (n * sum_x2- sum_x*sum_x);
        r[1] = (n * sum_lny_x - sum_lny * sum_x) / (n * sum_x2- sum_x*sum_x);
        double[] approx = new double[n];
        double a = Math.exp(r[0]);
        double b = r[1];
        for (int i = 0; i < n; i++) {
            approx[i] = a * Math.exp(b * i);
        }
        System.out.println("Average mistake for exp = " + avgMistake(y, approx));
        r[2] = avgMistake(y, approx);
        return r;
    }

    public static double[] hyperStat(double[] y){
        double[] r= new double[3];
        int n = y.length;
        double sum_y_x = 0;
        double sum_1_x = 0;
        double sum_y = 0;
        double sum_1_x2 = 0;
        for (int i = 0; i < n; i++) {
            if (i != 0) {
                sum_1_x += 1 / (i);
                sum_1_x2 += 1 / ((i) * (i));
                sum_y_x += y[i] / (i);
            }
            sum_y += y[i];
        }
        r[1] = (n * sum_y_x - sum_1_x * sum_y) / (n * sum_1_x2 - sum_1_x * sum_1_x);
        r[0] = sum_y/n - r[1]*sum_1_x/n;

        double[] approx = new double[n];
        for (int i = 1; i < n; i++) {
            approx[i] = r[0] + r[1]/i;
        }
        System.out.println("Average mistake for hyperbolic = " + avgMistake(y, approx));
        r[2] = avgMistake(y, approx);
        return r;
    }

    public static double avgMistake(double[] orig, double[] approx){
        double ans = 0;
        double n = orig.length;
        double sum = 0;
        for (int i = 0; i < n; i++) {
            sum += Math.abs((orig[i] - approx[i]) / orig[i]);
        }
        double mistake = sum/n * 100;
        return mistake;
    }
}
