package com.example.login;

import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;

public class SQLConnection {
    String uname, pass, ip, port, database;
    public Connection connectionclass() {
        ip = "81.196.233.145";
        database = "MLC App Profiles";
        uname = "lol";
        pass = "test129";
        port = "1433";

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection = null;
        String ConnectionURL;

        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            ConnectionURL = "jdbc:jtds:sqlserver://" + ip + ":" + port + ";" + "databasename=" + database + ";user="+uname+";password="+pass+";";
            connection = DriverManager.getConnection(ConnectionURL);
        }
        catch (Exception ex) {
            Log.e("Error", ex.getMessage());
        }
        return connection;
    }
}
