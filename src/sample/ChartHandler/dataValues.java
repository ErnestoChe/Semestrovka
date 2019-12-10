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
        //String sql = "SELECT * FROM data_values where (username = 'test' and date_value BETWEEN '2018-02-17' AND '2018-12-28');";
        Connection conn = dbConnection.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql,ResultSet.TYPE_SCROLL_SENSITIVE,
                ResultSet.CONCUR_UPDATABLE);
        ResultSet rs = pstmt.executeQuery();
        return rs;
    }

    public static double[] mnk(double[] y){
        double[] r = new double[2];
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
        System.out.println(y.length + " LENGTH");
        return r;
    }

    public static double[] sqrtStat(double[] y){
        double[] r = new double[3];
        double n = y.length;
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

        return r;
    }

    public static double[] expStat(double[] y){
        double[] r = new double[2];
        double n = y.length;
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
        return r;
    }
}
