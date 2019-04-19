package com.example.expensetracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.Calendar;

public class EditExpenseActivity extends AppCompatActivity {

    private int user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_expense);

        // Get user_id
        user_id = getIntent().getIntExtra("USER_ID", 0);
    }

    private void saveExpense () {
        // Build the URL
        String url = "https://cst-438-project-desktop.herokuapp.com/expenses";
        Log.i("API_REQUEST", url);

        EditText etAmount = findViewById(R.id.et_amount);
        EditText etDescription = findViewById(R.id.et_description);
        EditText etCategory = findViewById(R.id.et_category);

        // Get values from the EditTexts
        double amount = Double.parseDouble(etAmount.getText().toString());
        String description = etDescription.getText().toString().trim();
        String category = etCategory.getText().toString().trim();

        // If something was entered into all of the fields
        if (amount > 0 && !description.isEmpty() && !category.isEmpty()) {

            // Convert field entries into a JSON object
            JSONObject expenseJson = new JSONObject();
            try {
                expenseJson.put("user_id", user_id);
                expenseJson.put("amount", amount);
                expenseJson.put("description", description);
                expenseJson.put("category", category);
                expenseJson.put("created_at", Expense.dateFormatJson.parse(Calendar.getInstance().getTime().toString()));
            } catch (Exception e) {
                e.printStackTrace();
            }


            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Toast.makeText(EditExpenseActivity.this, "POST Successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(EditExpenseActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(EditExpenseActivity.this, "POST Unsuccessful", Toast.LENGTH_SHORT).show();
                }
            });

            // Make the API request
            RequestQueue queue = Volley.newRequestQueue(this);
            queue.add(jsonObjectRequest);


        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_expense_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.btn_save) {
            saveExpense();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
