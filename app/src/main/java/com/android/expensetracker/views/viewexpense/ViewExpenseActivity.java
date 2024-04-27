package com.android.expensetracker.views.viewexpense;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.android.expensetracker.R;
import com.android.expensetracker.databinding.ActivityViewExpenseBinding;
import com.android.expensetracker.views.addexpense.ExpenseEntity;
import com.android.expensetracker.views.addexpense.ExpenseRepository;

import java.util.List;

public class ViewExpenseActivity extends AppCompatActivity {

    private ActivityViewExpenseBinding binding;
    private ExpenseRepository expenseRepository;

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
        setExpenseAdapter(expenseList);
    }

    private void setExpenseAdapter(List<ExpenseEntity> expenseList){
        ViewExpenseAdapter adapter = new ViewExpenseAdapter(this,expenseList);
        binding.rcvExpenses.setAdapter(adapter);

        adapter.setItemClickListener(new ViewExpenseAdapter.ItemClickListener() {
            @Override
            public void onClickRemove(int id, int position) {

            }
        });
    }
}