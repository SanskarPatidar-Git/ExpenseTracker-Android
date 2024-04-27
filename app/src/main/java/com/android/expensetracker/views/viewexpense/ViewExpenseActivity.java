package com.android.expensetracker.views.viewexpense;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;

import com.android.expensetracker.R;
import com.android.expensetracker.databinding.ActivityViewExpenseBinding;
import com.android.expensetracker.views.addexpense.ExpenseEntity;
import com.android.expensetracker.views.addexpense.ExpenseRepository;

import java.util.Collections;
import java.util.List;

public class ViewExpenseActivity extends AppCompatActivity {

    private ActivityViewExpenseBinding binding;
    private ExpenseRepository expenseRepository;
    private ViewExpenseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityViewExpenseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
        getAllExpenses();
    }

    private void init(){
        expenseRepository = new ExpenseRepository();
        binding.header.tvTitle.setText("All Expenses");
    }
    private void getAllExpenses(){
        List<ExpenseEntity> expenseList = expenseRepository.getAllExpenses();
        Collections.reverse(expenseList);
        setExpenseAdapter(expenseList);
    }

    private void setExpenseAdapter(List<ExpenseEntity> expenseList){
        adapter = new ViewExpenseAdapter(this,expenseList);
        binding.rcvExpenses.setAdapter(adapter);

        adapter.setItemClickListener(new ViewExpenseAdapter.ItemClickListener() {
            @Override
            public void onClickRemove(int id, int position) {
                showAlertDialog(id,position);
            }
        });
    }

    private void showAlertDialog(int id , int position) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setIcon(R.drawable.ic_delete);
        dialog.setTitle("Delete");
        dialog.setMessage("Are you sure you want to remove the Expense ?");

        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                expenseRepository.deleteExpense(id);
                adapter.deleteExpense(position);
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        dialog.show();
    }
}