package com.example.expensetracker;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder> {

    private List<Expense> expenses = new ArrayList<>();
    private DateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy");

    public void addExpense(Expense expense) {
        expenses.add(expense);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ExpenseViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.expense_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder expenseViewHolder, int i) {
        expenseViewHolder.description.setText(expenses.get(i).getDescription());
        expenseViewHolder.category.setText(expenses.get(i).getCategory());
        expenseViewHolder.date.setText(dateFormat.format(expenses.get(i).getDate()));
        expenseViewHolder.amount.setText(expenses.get(i).getAmount().toString());
    }

    @Override
    public int getItemCount() {
        return expenses.size();
    }

    public class ExpenseViewHolder extends RecyclerView.ViewHolder{

        TextView description, category, date, amount;

        public ExpenseViewHolder(@NonNull View itemView) {
            super(itemView);

            description = itemView.findViewById(R.id.tv_description);
            category = itemView.findViewById(R.id.tv_category);
            date = itemView.findViewById(R.id.tv_date);
            amount = itemView.findViewById(R.id.tv_amount);
        }

    }
}
