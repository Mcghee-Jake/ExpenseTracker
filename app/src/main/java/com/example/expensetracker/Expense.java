package com.example.expensetracker;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Expense {

    String description, category;
    Date date;
    double amount;

    public Expense() {};

    public Expense(String description, String category, Date date, double amount) {
        this.description = description;
        this.category = category;
        this.date = date;
        this.amount = amount;
    }

    public void fromJson(JSONObject jsonObject) throws Exception {
        this.description = jsonObject.getString("description");
        this.category = jsonObject.getString("category");
        this.amount = jsonObject.getDouble("amount");

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sss");
        this.date = simpleDateFormat.parse(jsonObject.getString("created_at"));

        // You can also try "yyyy-MM-dd'T'HH:mm:ss.sssZZZZ"
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
