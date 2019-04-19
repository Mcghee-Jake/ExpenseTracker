package com.example.expensetracker;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Remote {

    private final static String BASE_URL = "https://cst-438-project-desktop.herokuapp.com/";
    private Context context;

    public Remote (Context context){
        this.context = context;
    }

    public List<Expense> getExpenses(final String username) {
        // Build the URL
        String apiUrl = BASE_URL + "api/" + username + ".json";
        Log.i("API_REQUEST", apiUrl);

        final List<Expense> expensesList = new ArrayList<>();

        // Create the JsonArrayRequest
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, apiUrl, null, new Response.Listener<JSONArray>() {
            @Override
            // Handle a successful response fom the server
            public void onResponse(JSONArray response) {
                Log.d("Expense", "Response Length: " + response.length());

                // For each object in the JSON array
                for (int i = 0; i < response.length(); i++) {
                    try {
                        // Create the expense from the JSON object
                        Expense expense = new Expense();
                        JSONObject expenseJson = response.getJSONObject(i);
                        expense.fromJson(expenseJson);

                        // Add it to the list
                        expensesList.add(expense);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            // Handle an unsuccessful response fom the server
            public void onErrorResponse(VolleyError error) {
                Log.e("API_REQUEST", "Error - Unable to fetch data from server");
            }
        });

        // Make the API request
        RequestQueue queue = Volley.newRequestQueue(this.context);
        queue.add(jsonArrayRequest);

        if (!expensesList.isEmpty()) return expensesList;
        else return getExpenses(username);
    }


}
