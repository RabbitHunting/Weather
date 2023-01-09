package com.wbl.weather.db.dao.impl;

import com.wbl.weather.db.bean.WeatherUser;
import com.wbl.weather.db.dao.WeatherUserDao;
import com.wbl.weather.db.utils.JDBCUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class WeatherUserDaoImpl implements WeatherUserDao {

    public boolean login(String username,String password) {
        String sql = "select * from weatherUser where username= ? and password = ?";
        Connection con = JDBCUtils.getConn();

        try {
            PreparedStatement pst = con.prepareCall(sql);
            pst.setString(1, username);
            pst.setString(2, password);
            if (pst.executeQuery().next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.close(con);
        }
        return false;
    }
    public boolean register(WeatherUser weatherUser) {

        String sql = "insert into weatherUser (username,password,phone,name) values (?,?,?,?)";

        Connection con = JDBCUtils.getConn();

        try {
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, weatherUser.getUsername());
            pst.setString(2, weatherUser.getPassword());
            pst.setString(3, weatherUser.getPhone());
            pst.setString(4, weatherUser.getName());

            int value = pst.executeUpdate();
            if (value > 0) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.close(con);
        }
        return false;
    }

    public WeatherUser findUser(String name) {
        String sql = "select * from weatherUser where username = ?";

        Connection con = JDBCUtils.getConn();

        WeatherUser user = null;

        try {
            PreparedStatement pst = con.prepareStatement(sql);

            pst.setString(1,name);

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                int id = rs.getInt(1);
                String username = rs.getString(2);
                String password = rs.getString(3);
                String phone = rs.getString(4);
                String nicheng= rs.getString(5);

                user = new WeatherUser(id,username,password,phone,nicheng);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.close(con);
        }
        return user;
    }
}
