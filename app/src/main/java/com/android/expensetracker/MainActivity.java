package com.android.expensetracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.android.expensetracker.databinding.ActivityMainBinding;
import com.android.expensetracker.utility.AppUtil;
import com.android.expensetracker.views.addexpense.AddExpenseActivity;
import com.android.expensetracker.views.users.UsersActivity;
import com.android.expensetracker.views.viewexpense.ViewExpenseActivity;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initListeners();
    }

    private void initListeners() {

        binding.btnAddExpense.setOnClickListener(view -> {
            AppUtil.navigateTo(MainActivity.this , AddExpenseActivity.class);
        });

        binding.btnViewExpense.setOnClickListener(view -> {
            AppUtil.navigateTo(MainActivity.this , ViewExpenseActivity.class);
        });

        binding.tvUsers.setOnClickListener(view -> {
            AppUtil.navigateTo(MainActivity.this , UsersActivity.class);
        });
    }
}