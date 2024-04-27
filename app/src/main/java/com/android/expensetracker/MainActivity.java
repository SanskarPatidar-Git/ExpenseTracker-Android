package com.android.expensetracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.android.expensetracker.databinding.ActivityMainBinding;
import com.android.expensetracker.utility.AppUtil;
import com.android.expensetracker.views.addexpense.AddExpenseActivity;
import com.android.expensetracker.views.transactions.TransactionBottomSheet;
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

        binding.btnLentMoney.setOnClickListener(view -> {
            openBottomSheet(Constants.LENT_MODE);
        });

        binding.btnBorrowMoney.setOnClickListener(view -> {
            openBottomSheet(Constants.BORROWED_MODE);
        });
    }

    private void openBottomSheet(int mode){
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.MODE , mode);

        TransactionBottomSheet bottomSheet = new TransactionBottomSheet();
        bottomSheet.setArguments(bundle);
        bottomSheet.show(getSupportFragmentManager() , "TransactionBottomSheet");
    }
}