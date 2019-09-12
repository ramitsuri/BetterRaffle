package com.betterraffle.main.db;

import com.betterraffle.main.entities.User;
import com.betterraffle.main.helper.Constants;
import com.betterraffle.main.helper.Utils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SQLHelper {

    private static Connection sConnection;

    public Connection getInstance() {
        if (sConnection == null) {
            sConnection = init();
        }
        return sConnection;
    }

    public static Connection init() {
        String dbURL = "jdbc:mysql://127.0.0.1:3306/better_raffle?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
        String username = "root";
        String password = "helloworld";

        Connection conn = null;
        try {
            conn = DriverManager.getConnection(dbURL, username, password);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return conn;
    }

    public static User getUser(int id) {
        User user = null;
        String sql = "SELECT * FROM " + Constants.TABLE_NAME + " WHERE " + Constants.COL_ID + " =?";
        try {
            PreparedStatement statement = sConnection.prepareStatement(sql);
            statement.setInt(1, id);

            ResultSet result = statement.executeQuery(sql);
            while (result.next()) {
                user = getUserFromResult(result);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return user;
    }

    public static String fillToken(int id) {
        String token = null;
        String sql = "UPDATE " + Constants.TABLE_NAME + " set " + Constants.COL_TOKEN + " =? WHERE " + Constants.COL_ID + " =?";
        try {
            PreparedStatement statement = sConnection.prepareStatement(sql);
            token = String.valueOf(Utils.generateToken());
            statement.setString(1, token);
            statement.setInt(2, id);

            statement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return token;
    }

    public static User pickWinner() {
        String sql = "SELECT * FROM " + Constants.TABLE_NAME + " where " + Constants.COL_ID + " is not null";
        try {
            List<User> users = new ArrayList<>();
            PreparedStatement statement = sConnection.prepareStatement(sql);

            ResultSet result = statement.executeQuery(sql);
            while (result.next()) {
                User user = getUserFromResult(result);
                users.add(user);
            }

            int upperBoundExclusive = users.size();
            int winnerIndex = Utils.pickRandomInt(upperBoundExclusive);
            return users.get(winnerIndex);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static void resetTokens() {
        String sql = "UPDATE " + Constants.TABLE_NAME + " set " + Constants.COL_TOKEN + " = null";
        try {
            PreparedStatement statement = sConnection.prepareStatement(sql);

            statement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private static User getUserFromResult(ResultSet result) throws SQLException {
        User user = new User();

        int id = result.getInt(Constants.COL_ID_INDEX);
        user.setId(id);

        String userName = result.getString(Constants.COL_NAME_INDEX);
        user.setName(userName);

        String phone = result.getString(Constants.COL_PHONE_INDEX);
        user.setPhone(phone);

        String token = result.getString(Constants.COL_TOKEN_INDEX);
        user.setToken(token);

        long lastWon = result.getInt(Constants.COL_LAST_WON_INDEX);
        user.setLastWon(new Date(lastWon));

        return user;
    }
}
