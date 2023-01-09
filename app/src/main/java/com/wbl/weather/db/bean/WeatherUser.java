package com.wbl.weather.db.bean;

public class WeatherUser {
    private int id;
    private String username;
    private String password;
    private String phone;
    private String name;

    public WeatherUser () {

    }
    public WeatherUser(int id, String username, String password, String phone, String name) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
