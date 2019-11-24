package sample.DataBaseController;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;



public final class dbConnection {

    public static Connection getConnection()
    {
        String dbPassword = "dlxkj1gwf18y";
        String user = "postgres";
        String url = "jdbc:postgresql://localhost:5432/Kursa4?user="+user+"&password=" + dbPassword;
        try {
            Class.forName("org.postgresql.Driver");

        } catch (ClassNotFoundException e) {
            System.out.println("ERROR JDBC driver connection:");
            e.printStackTrace();
        }
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return conn;
    }
}
