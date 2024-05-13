package com.android.expensetracker.views.viewexpense;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.android.expensetracker.R;
import com.android.expensetracker.databinding.ActivityViewExpenseBinding;
import com.android.expensetracker.utility.DateFormat;
import com.android.expensetracker.views.addexpense.ExpenseEntity;
import com.android.expensetracker.views.addexpense.ExpenseRepository;
import com.android.expensetracker.views.monthexpense.ExportSummaryRepository;

import java.util.Collections;
import java.util.List;

public class ViewExpenseActivity extends AppCompatActivity {

    private ActivityViewExpenseBinding binding;
    private ExpenseRepository expenseRepository;
    private ViewExpenseAdapter adapter;
    public boolean isExpenseFound ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityViewExpenseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
        initListeners();
        getAllExpenses();
    }

    private void init(){
        expenseRepository = new ExpenseRepository();
        binding.header.tvTitle.setText("All Expenses");
    }

    private void initListeners(){
        binding.header.imgBack.setOnClickListener(view -> {
            finish();
        });

        binding.btnClearAll.setOnClickListener(view -> {
            if(isExpenseFound){
                showAlertDialog(0,0,true , "Clear All");
            } else
                Toast.makeText(this, "Already cleared", Toast.LENGTH_SHORT).show();
        });
    }

    private void getAllExpenses(){
        List<ExpenseEntity> expenseList = expenseRepository.getAllExpenses();
        if(expenseList.size() > 0)
            isExpenseFound = true;
        Collections.reverse(expenseList);
        setExpenseAdapter(expenseList);
    }

    private void setExpenseAdapter(List<ExpenseEntity> expenseList){
        adapter = new ViewExpenseAdapter(this,expenseList);
        binding.rcvExpenses.setAdapter(adapter);

        adapter.setItemClickListener(new ViewExpenseAdapter.ItemClickListener() {
            @Override
            public void onClickRemove(int id, int position) {
                showAlertDialog(id,position,false , "Delete");
            }

            @Override
            public void onDataLoaded(){

            }
        });
        initExpenseActivities();
    }

    private void showAlertDialog(int id , int position , boolean clearAll , String title) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setIcon(R.drawable.ic_delete);
        dialog.setTitle(title);
        dialog.setMessage("Are you sure you want to remove the Expense ?");

        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(clearAll){
                    expenseRepository.deleteAllExpenses();
                    adapter.deleteAllExpenses();
                } else {
                    expenseRepository.deleteExpense(id);
                    adapter.deleteExpense(position);
                }
                initExpenseActivities();
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        dialog.show();
    }

    private void getMonthBasisExpense(String month){
        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                List<ExpenseEntity> expenseList = adapter.getExpenseList();
                double monthExpense = 0;
                for(ExpenseEntity entity : expenseList){
                    if(entity.getDate().contains(month))
                        monthExpense += entity.getExpense();
                }
                binding.tvExpenseMonth.setText(String.valueOf(monthExpense));
                handler.removeCallbacks(this);
            }
        });
    }

    private void getYearBasisExpense(String year){
        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                List<ExpenseEntity> expenseList = adapter.getExpenseList();
                double yearExpense = 0;
                for(ExpenseEntity entity : expenseList){
                    if(entity.getDate().contains(year))
                        yearExpense += entity.getExpense();
                }

                double exportedExpense = new ExportSummaryRepository().getTotalExportedExpenseOfYear(Integer.parseInt(year));
                binding.tvExpenseYear.setText(String.valueOf(yearExpense+exportedExpense));
                handler.removeCallbacks(this);
            }
        });
    }

    private void getTodayBasisExpense(){
        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                List<ExpenseEntity> expenseList = expenseRepository.getExpensesOfToday();
                double todayExpense = 0;
                for(ExpenseEntity entity : expenseList)
                    todayExpense += entity.getExpense();
                binding.tvExpenseToday.setText(String.valueOf(todayExpense));
                handler.removeCallbacks(this);
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void initExpenseActivities(){
        String currentDate = DateFormat.getCurrentDate();
        String[] dateArr = currentDate.split("-");
        int month = Integer.parseInt(dateArr[1]);
        int year = Integer.parseInt(dateArr[2]);

        binding.tvLabelMonth.setText("Month("+DateFormat.getMonthNameByDigit(month)+")");
        binding.tvLabelYear.setText("Year("+year+")");

        getTodayBasisExpense();
        getMonthBasisExpense(dateArr[1]+"-"+dateArr[2]); // 04-2024
        getYearBasisExpense(dateArr[2]);
    }
}