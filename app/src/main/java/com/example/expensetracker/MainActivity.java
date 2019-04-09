package com.example.expensetracker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

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
        generateDummyData();
        getExpenseData();
    }

    private void initializeRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.rv_expenses);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        expenseAdapter = new ExpenseAdapter();
        recyclerView.setAdapter(expenseAdapter);
    }

    private void getExpenseData() {
        String baseUrl = "https://cst-438-project-desktop.herokuapp.com/";
        String apiUrl = baseUrl + "api/" + username + ".json";

        Log.i("API_REQUEST", apiUrl);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, apiUrl, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Toast.makeText(MainActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jsonArrayRequest);
    }


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
