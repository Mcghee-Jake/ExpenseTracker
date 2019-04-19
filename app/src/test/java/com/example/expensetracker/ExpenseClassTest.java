package com.example.expensetracker;

import org.json.JSONObject;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;

public class ExpenseClassTest {


    @Test
    public void correctConversionFromJsonObject() throws Exception {


        // Mock the JSONObject
        JSONObject jsonObject = Mockito.mock(JSONObject.class);
        Mockito.when(jsonObject.getDouble("amount")).thenReturn(2.22);
        Mockito.when(jsonObject.getString("description")).thenReturn("Sprouts");
        Mockito.when(jsonObject.getString("category")).thenReturn("Groceries");
        Mockito.when(jsonObject.getString("created_at")).thenReturn("2019-04-09T10:09:27.213-07:00");

        Expense expense = new Expense();
        expense.fromJson(jsonObject); // Here is the method under test

        // Tests
        assertEquals(jsonObject.getDouble("amount"), expense.getAmount(), .01);
        assertEquals(jsonObject.getString("description"), expense.getDescription());
        assertEquals(jsonObject.getString("category"), expense.getCategory());
        assertEquals(Expense.dateFormatJson.parse(jsonObject.getString("created_at")), expense.getDate());
    }

}
