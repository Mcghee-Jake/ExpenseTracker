package com.example.expensetracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final int RC_SIGN_IN = 01;
    private ExpenseAdapter expenseAdapter;
    private int user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        authorizeUser();
        initializeRecyclerView();
        initializeFloatingActionButton();
    }

    private void authorizeUser() {

        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder()
                        .build());

        // Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String userEmail = user.getEmail();
                getExpenseData(userEmail);
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
            }
        }
    }

    private void getExpenseData(final String userEmail) {

        String formattedEmail = new StringBuilder(userEmail).deleteCharAt(userEmail.length() - 4).toString();
        Log.i("FORMATTED EMAIL", formattedEmail);

        // Build the URL
        String BASE_URL = "https://cst-438-project-desktop.herokuapp.com/";
        String apiUrl = BASE_URL + formattedEmail + ".json";
        Log.i("API_REQUEST", apiUrl);

        // Create the JsonArrayRequest
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, apiUrl, null, new Response.Listener<JSONArray>() {
            @Override
            // Handle a successful response fom the server
            public void onResponse(JSONArray response) {
                Log.d("Expense", "Response Length: " + response.length());

                // Get user_id
                try {
                    user_id = response.getJSONObject(0).getInt("user_id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // For each object in the JSON array
                for (int i = 0; i < response.length(); i++) {
                    try {
                        // Create the expense from the JSON object
                        Expense expense = new Expense();
                        JSONObject expenseJson = response.getJSONObject(i);
                        expense.fromJson(expenseJson);

                        // Display the expense by adding it to the recyclerview adapter
                        expenseAdapter.addExpense(expense);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                // Hide the loading indicator
                ProgressBar progressBar = findViewById(R.id.pb_load_expenses);
                progressBar.setVisibility(View.GONE);

            }
        }, new Response.ErrorListener() {
            @Override
            // Handle an unsuccessful response fom the server
            public void onErrorResponse(VolleyError error) {
                Log.e("API_REQUEST", "Error - Unable to fetch data from server");
                getExpenseData(userEmail);
            }
        });

        // Make the API request
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        queue.add(jsonArrayRequest);
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
                newExpenseIntent.putExtra("USER_ID", user_id);
                startActivity(newExpenseIntent);
            }
        });
    }

}
