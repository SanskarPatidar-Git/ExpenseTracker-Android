package com.android.expensetracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.android.expensetracker.databinding.ActivityMainBinding;
import com.android.expensetracker.utility.AppUtil;
import com.android.expensetracker.views.addexpense.AddExpenseActivity;
import com.android.expensetracker.views.monthexpense.MonthExpenseActivity;
import com.android.expensetracker.views.transactions.TransactionBottomSheet;
import com.android.expensetracker.views.transactions.TransactionRepository;
import com.android.expensetracker.views.users.UsersActivity;
import com.android.expensetracker.views.viewexpense.ViewExpenseActivity;
import com.android.expensetracker.views.viewtransactions.ViewTransactionActivity;

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

        binding.btnAllTransactions.setOnClickListener(view -> {
            AppUtil.navigateTo(MainActivity.this , ViewTransactionActivity.class);
        });

        binding.btnMonthlyExpense.setOnClickListener(view -> {
            AppUtil.navigateTo(MainActivity.this , MonthExpenseActivity.class);
        });
    }

    private void openBottomSheet(int mode){
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.MODE , mode);

        TransactionBottomSheet bottomSheet = new TransactionBottomSheet();
        bottomSheet.setArguments(bundle);
        bottomSheet.show(getSupportFragmentManager() , "TransactionBottomSheet");
    }

    @SuppressLint("SetTextI18n")
    public void initTransactions(){
        TransactionRepository repository = new TransactionRepository();
        double totalLent = repository.getTotalLent();
        double totalBorrowed = repository.getTotalBorrowed();

        binding.tvGiven.setText("+"+totalLent);
        binding.tvTaken.setText("-"+totalBorrowed);

        double total = totalLent - totalBorrowed;

        if(total < 0){
            binding.tvTotal.setText(String.valueOf(total));
            binding.tvTotal.setTextColor(ContextCompat.getColorStateList(this,R.color.red));
        } else {
            binding.tvTotal.setText("+"+total);
            binding.tvTotal.setTextColor(ContextCompat.getColorStateList(this,R.color.green));
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        initTransactions();
    }
}