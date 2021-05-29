package com.example.login;

import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;

public class SQLConnection {
    private static final String USERNAME = "lol";
    private static final String IP = "mlcapp.go.ro";
    private static final String DATABASE = "MLC App Profiles";
    private static final String PASSWORD = "test129";
    private static final String PORT = "1433";
    public static final String ACCOUNTS_TABLE = "Accounts";
    public static final String PROFILES_TABLE = "Profiles";
    public static final String COURSESUSERS_TABLE = "CoursesUsers";
    public static final String COURSES_TABLE = "Courses";
    public static final String CAPITOLE_TABLE = "Capitole";
    public static final String QUESTIONS_TABLE = "Questions";

    public static Connection getConnection() {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection = null;
        String ConnectionURL;

        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            ConnectionURL = "jdbc:jtds:sqlserver://" + IP + ":" + PORT + ";" + "databasename=" + DATABASE + ";user=" + USERNAME + ";password=" + PASSWORD + ";";
            connection = DriverManager.getConnection(ConnectionURL);
        }
        catch (Exception ex) {
            Log.e("Error", ex.getMessage());
        }
        return connection;
    }
}
