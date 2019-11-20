package sample.DataBaseController;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class dbHandler {

    public static String addUser(String login, String password, String name, String lastName){
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

    public static String loginUser(String login, String password){
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
}
