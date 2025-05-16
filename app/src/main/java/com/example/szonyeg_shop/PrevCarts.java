package com.example.szonyeg_shop;

import android.annotation.SuppressLint;
import android.os.Build;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class PrevCarts {
    private Date date;
    private String user;
    private ArrayList<Map<String,Object>> items;
    private int total;

    public PrevCarts(Date date, String user, ArrayList<Map<String, Object>> items, int total) {
        this.date = date;
        this.user = user;
        this.items = items;
        this.total = total;
    }

    public PrevCarts() {
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public ArrayList<Map<String, Object>> getItems() {
        return items;
    }

    public void setItems(ArrayList<Map<String, Object>> items) {
        this.items = items;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    @SuppressLint("NewApi")
    public java.time.LocalDateTime getDateAsLocalDateTime() {
        if (this.date == null) {
            return null;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return this.date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        }

        return LocalDateTime.now();
    }

}
