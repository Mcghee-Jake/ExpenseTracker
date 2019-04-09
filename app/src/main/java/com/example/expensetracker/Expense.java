package com.example.expensetracker;

import java.util.Date;

public class Expense {

    String description, category;
    Date date;
    double amount;

    public Expense(String description, String category, Date date, double amount) {
        this.description = description;
        this.category = category;
        this.date = date;
        this.amount = amount;
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
