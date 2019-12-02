package sample.ChartHandler;

import sample.DataBaseController.dbConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class dataValues {
    public static void main(String[] args) throws SQLException {

    }
    public static ResultSet values(/*LocalDate d1, LocalDate d2*/) throws SQLException {
        //String date1 = d1.toString();
        //String date2 = d2.toString();

        //String sql = "SELECT * FROM data_values where (date_value BETWEEN '"+ date1 + "' AND '"+ date2+"');";
        String sql = "SELECT * FROM data_values where (date_value BETWEEN '2018-02-17' AND '2019-01-31');";
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

        return r;
    }
}
