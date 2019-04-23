package com.example.expensetracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EditExpenseActivity extends AppCompatActivity {

    private int user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_expense);

        // Get user_id
        user_id = getIntent().getIntExtra("USER_ID", 0);
    }

    /*
        Save the expense in the rails database
        This is called when the user presses the SAVE menu button
     */
    private void saveExpense () {
        // Build the URL
        String url = "https://cst-438-project-desktop.herokuapp.com/expenses";
        Log.i("API_REQUEST", url);

        EditText etAmount = findViewById(R.id.et_amount);
        EditText etDescription = findViewById(R.id.et_description);
        EditText etCategory = findViewById(R.id.et_category);


        // Get values from the EditTexts
        String amount = etAmount.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        String category = etCategory.getText().toString().trim();

        // If something was entered into all of the fields
        if (!amount.isEmpty() && Double.parseDouble(amount) > 0 && !description.isEmpty() && !category.isEmpty()) {

            // Convert field entries into a JSON object
            JSONObject expenseData = new JSONObject();
            JSONObject expenseObject = new JSONObject();
            try {
                expenseData.put("user_id", String.valueOf(user_id));
                expenseData.put("amount", String.valueOf(amount));
                expenseData.put("category", category);
                expenseData.put("description", description);
                expenseObject.put("expense", expenseData);
                // expenseJson.put("created_at", Expense.dateFormatJson.parse(Calendar.getInstance().getTime().toString()));
                Log.i("API_REQUEST", expenseObject.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Build the jsonObjectRequest
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, expenseObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Toast.makeText(EditExpenseActivity.this, "Expense Saved", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(EditExpenseActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(EditExpenseActivity.this, "POST Unsuccessful", Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Content-Type", "application/json");
                    params.put("Accept", "application/json");
                    return params;
                }
            };

            // Make the API request
            RequestQueue queue = Volley.newRequestQueue(this);
            queue.add(jsonObjectRequest);

        } else Toast.makeText(this, "Invalid input", Toast.LENGTH_SHORT).show();
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
