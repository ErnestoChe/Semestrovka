package sample.DataBaseController;

import java.sql.*;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.TimeZone;

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
                String sqlCheck = "SELECT * FROM data_values where(" +
                        "username = '" +userName+ "' and " +
                        "date_value = '" + date[i].toString() +
                        "');";
                PreparedStatement pstmt = conn.prepareStatement(sqlCheck);
                ResultSet rs = pstmt.executeQuery();
                if(!rs.next()){
                    System.out.println("таких данных нет");
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
                    PreparedStatement preparedStatement = conn.prepareStatement(sql);
                    result = preparedStatement.executeUpdate();
                }else{
                    System.out.println("такие данные есть");
                }
                //return "asd";
            }
            conn.close();
            return String.valueOf(result);
        }catch (SQLException e){

            System.out.println("error code " + e.getSQLState());
            System.out.println("данные не добавлены");

            return e.getSQLState();
        }
    }
    //for users

    /**
     *
     * @param user name of user
     * @param state 0 - reg, 1 - log in, 2 - log out
     * @return sql state
     */
    public static String log(String user, int state){
        Connection conn = dbConnection.getConnection();
        String sql = "INSERT INTO logs(login, time_log, msg) VALUES(?,?,?)";
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, user);
            ZonedDateTime receivedTimestamp = ZonedDateTime.now(ZoneId.systemDefault());
            Timestamp ts = new Timestamp(receivedTimestamp.toInstant().toEpochMilli());
            preparedStatement.setTimestamp(
                    2,
                    ts,
                    Calendar.getInstance(TimeZone.getTimeZone(receivedTimestamp.getZone()))
            );
            String msg = "";
            switch (state){
                case 0:
                    msg = user + " reg";
                    break;
                case 1:
                    msg = user + " logged in";
                    break;
                case 2:
                    msg = user + " logged out";
                    break;
            }
            preparedStatement.setString(3, msg);

            int result = preparedStatement.executeUpdate();
            return String.valueOf(result);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return String.valueOf("asd");
    }
    //for koefs

    /**
     *
     * @param user
     * @param state 1 - added data, 2 - calculations
     * @param time_bounds
     * @param instrumnet 1 - lin, 2 - sqr, 3 - exp, 4 hyper
     * @param koefs a, b, c
     * @return
     */
    public static String logData(String user, int state, String time_bounds, int instrumnet, String koefs){
        //TODO расписать добавление и анализ данных(VVV ЭТО НЕ РАБОТАЕТ VVV)
        Connection conn = dbConnection.getConnection();
        String sql = "INSERT INTO logs(login, time_log, msg, time_bounds, instrument, koefs)" +
                " VALUES(?,?,?,?,?,?)";
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, user);
            ZonedDateTime receivedTimestamp = ZonedDateTime.now(ZoneId.systemDefault());
            Timestamp ts = new Timestamp(receivedTimestamp.toInstant().toEpochMilli());
            preparedStatement.setTimestamp(
                    2,
                    ts,
                    Calendar.getInstance(TimeZone.getTimeZone(receivedTimestamp.getZone()))
            );
            preparedStatement.setString(3, "msg");
            preparedStatement.setString(4, time_bounds);
            preparedStatement.setInt(5, instrumnet);
            preparedStatement.setString(6, koefs);
            int result = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "ahahahahah";
    }
}
