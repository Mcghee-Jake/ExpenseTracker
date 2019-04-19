package com.example.expensetracker;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Expense {

    protected static DecimalFormat decimalFormat = new DecimalFormat("#.00");
    protected static DateFormat dateFormatJson = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sss");
    protected static DateFormat dateFormatDisplay = new SimpleDateFormat("MMMM dd, yyyy");

    private String description, category;
    private Date date;
    private double amount;


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
        this.date = dateFormatJson.parse(jsonObject.getString("created_at"));
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
