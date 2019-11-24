package sample.DataBaseController;

import java.sql.*;
import java.text.SimpleDateFormat;

public class dbHandler {

    public static String addUser(String login, String password, String name, String lastName)
    {
        try{
            Connection conn = dbConnection.getConnection();
            String sql = "INSERT INTO Users(login, pass, firstname, lastname) VALUES(?,?,?,?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, login);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, name);
            preparedStatement.setString(4, lastName);

            int result = preparedStatement.executeUpdate();
            return String.valueOf(result);
        }catch (SQLException e){

            System.out.println("error code " + e.getSQLState());

            System.out.println("пользователь не создан");

            return e.getSQLState();
        }

    }

    public static String loginUser(String login, String password)
    {
        String result = "";
        String log_check = "";
        String pass_check = "";
        Connection conn = dbConnection.getConnection();
        String sql = "SELECT * FROM Users WHERE(login = '"+ login+ "')";
        PreparedStatement pstmt = null;

        try {
            pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                log_check = rs.getString("login");
                //System.out.println("name = " + log_check);
                pass_check = rs.getString("pass");
                //System.out.println("pass = " + pass_check);
            }
            rs.close();

            if(password.equals(pass_check)){
                result = log_check;
            }
            return result;
        } catch (SQLException e) {
            //e.printStackTrace();
            System.out.println(e.getSQLState());
        }
        return result;
    }

    public static String addData(
            String userName,
            Date[] date, double[] open,
            double[] high, double[] low,
            double[] close)
    {
        System.out.println("user is = " + userName + " in addData()");
        try{
            Connection conn = dbConnection.getConnection();
            int result = 0;
            for (int i = 0; i < date.length ; i++) {
                String sql = "INSERT INTO data_values(" +
                        "username, date_value, open_value, high_value, low_value, close_value) " +
                        "VALUES(" +
                        "(select login from users  where login = '" + userName + "'),'" +
                        date[i].toString() + "'," +
                        open[i] + "," +
                        high[i] + "," +
                        low[i] + "," +
                        close[i] + ");";
                System.out.println("added" + date[i]);
                //return "asd";
                PreparedStatement preparedStatement = conn.prepareStatement(sql);
                result = preparedStatement.executeUpdate();
            }
            conn.close();
            return String.valueOf(result);
        }catch (SQLException e){

            System.out.println("error code " + e.getSQLState());
            System.out.println("данные не добавлены");

            return e.getSQLState();
        }
    }


    //YYYY-MM-DD

    //insert into data_values(username, date_value, open_value, high_value, low_value, close_value)
    //values(
    //	(select login from users  where login = 'erik'),
    //	'2000-01-01',
    //	1.1,
    //	1.2,
    //	1.3,
    //	1.4
    //);
}
