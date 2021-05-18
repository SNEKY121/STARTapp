package com.example.login;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;

public class User {
    private String username;
    private String email;
    private int xp;
    private String cursuri;
    private int streak;
    private byte[] barray;


    Connection connect;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public String getCursuri() {
        return cursuri;
    }

    public void setCursuri(String cursuri) {
        this.cursuri = cursuri;
    }

    public int getStreak() {
        return streak;
    }


    public byte[] getBarray() {
        return barray;
    }

    public void setBarray(byte[] barray) {
        this.barray = barray;
    }

    public void getData() {
        getDataFromDB();
    }

    private void getDataFromDB() {
        try {
            SQLConnection connectionHelper = new SQLConnection();
            connect = connectionHelper.connectionclass();
            if (connect != null) {
                PreparedStatement stmt = connect.prepareStatement("SELECT * FROM " + SQLConnection.profilesTable + " WHERE Username = ?");
                stmt.setString(1, username);
                ResultSet rs = stmt.executeQuery();
                rs.next();
                Calendar c = Calendar.getInstance();

                xp = rs.getInt(4);
                cursuri = rs.getString(2);
                streak = rs.getInt(5);
                barray = rs.getBytes(3);
                int lastDay = rs.getInt(4);
                int thisDay = c.get(Calendar.DAY_OF_YEAR);
                updateProfile("Lastlogin", thisDay);
                if (lastDay == thisDay - 1 || (lastDay == 365 || lastDay == 366) && thisDay == 1) {
                    streak++;
                    updateProfile("Streakcounter", streak);
                    xp += 10 * streak;
                } else {
                    updateProfile("Streakcounter", 0);
                }
                updateProfile("Xp", xp);
            }
        } catch (Exception e) {
            Log.e("getData() ", e.toString());
        }
    }

    private void updateProfile(String column, int val) {
        try {
            SQLConnection connectionHelper = new SQLConnection();
            connect = connectionHelper.connectionclass();
            if (connect != null) {
                PreparedStatement stmt = connect.prepareStatement("UPDATE " + SQLConnection.profilesTable + " WHERE Username = ? SET " + column + " = ?");
                stmt.setString(1, username);
                stmt.setInt(2, val);
                stmt.execute();
            }
        } catch (Exception e) {
            Log.e("", "Check connection");
        }
    }
}
