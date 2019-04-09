package com.example.expensetracker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private String username = "jake";
    private ExpenseAdapter expenseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeRecyclerView();
        getExpenseData();
    }

    // Initializes the recyclerView - the UI element used to display all the expense items
    private void initializeRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.rv_expenses);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        expenseAdapter = new ExpenseAdapter();
        recyclerView.setAdapter(expenseAdapter);
    }

    // Fetches the expense data from the application server
    private void getExpenseData() {
        // Show the loading indicator
        ProgressBar progressBar = findViewById(R.id.pb_load_expenses);
        progressBar.setVisibility(View.VISIBLE);

        // Build the URL
        String baseUrl = "https://cst-438-project-desktop.herokuapp.com/";
        String apiUrl = baseUrl + "api/" + username + ".json";
        Log.i("API_REQUEST", apiUrl);

        // Create the JsonArrayRequest
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, apiUrl, null, new Response.Listener<JSONArray>() {
            @Override
            // Handle a successful response fom the server
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        // Create the expense from the JSON data
                        Expense expense = new Expense();
                        JSONObject expenseJson = response.getJSONObject(i);
                        expense.fromJson(expenseJson);

                        // Add it to the recyclerView
                        expenseAdapter.addExpense(expense);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            // Handle an unsuccessful response fom the server
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "It looks like there was an error pulling data from our servers. Are you properly connected to the internet?", Toast.LENGTH_SHORT).show();
            }
        });

        // Hide the loading indicator
        progressBar.setVisibility(View.GONE);

        // Make the API request
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jsonArrayRequest);
    }

    // This was used for testing - it generates some sample data
    private void generateDummyData() {

        Date date = Calendar.getInstance().getTime();

        Expense expense1 = new Expense("Sprouts", "Groceries", date, 36.16);
        Expense expense2 = new Expense("Arco", "Gas", date, 48.75);
        Expense expense3 = new Expense("Bahn Thai", "Dining Out", date, 23.18);

        expenseAdapter.addExpense(expense1);
        expenseAdapter.addExpense(expense2);
        expenseAdapter.addExpense(expense3);
    }
}
