package com.example.expensetracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private String username = "jake";
    private int user_id;
    private ExpenseAdapter expenseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeRecyclerView();
        initializeFloatingActionButton();
        getExpenseData();
    }

    private void getExpenseData() {
        Remote remote = new Remote(getApplicationContext());
        List<Expense> expensesList = remote.getExpenses(username);

        // Hide the loading indicator
        ProgressBar progressBar = findViewById(R.id.pb_load_expenses);
        progressBar.setVisibility(View.GONE);

        // Add the expenses to the adapter
        for (Expense expense : expensesList) {
            expenseAdapter.addExpense(expense);
        }
    }

    // Initializes the recyclerView - the UI element used to display all the expense items
    private void initializeRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.rv_expenses);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        expenseAdapter = new ExpenseAdapter();
        recyclerView.setAdapter(expenseAdapter);
    }

    // Initialize the floating action button - used to create a new expense
    private void initializeFloatingActionButton() {
        FloatingActionButton floatingActionButton = findViewById(R.id.fab_new_expense);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newExpenseIntent = new Intent(MainActivity.this, EditExpenseActivity.class);
                //newExpenseIntent.putExtra("USER_ID", user_id);
                startActivity(newExpenseIntent);
            }
        });
    }

}
