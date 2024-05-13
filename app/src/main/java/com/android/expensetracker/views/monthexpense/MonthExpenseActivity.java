package com.android.expensetracker.views.monthexpense;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.expensetracker.R;
import com.android.expensetracker.databinding.ActivityMonthExpenseBinding;
import com.android.expensetracker.utility.DateFormat;
import com.android.expensetracker.views.addexpense.ExpenseEntity;
import com.android.expensetracker.views.addexpense.ExpenseRepository;
import com.android.expensetracker.views.viewexpense.ViewExpenseAdapter;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MonthExpenseActivity extends AppCompatActivity {

    private static final String TAG = "MonthExpenseActivity";
    private ActivityMonthExpenseBinding binding;
    private ExpenseRepository expenseRepository;
    private ExportSummaryRepository exportSummaryRepository;
    private ViewExpenseAdapter adapter;
    private String expenseSummary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMonthExpenseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
        initListeners();
        initSpinners();
        setExpenseAdapter();
    }

    private void init(){
        binding.header.tvTitle.setText("Month Expense");
        expenseRepository = new ExpenseRepository();
        exportSummaryRepository = new ExportSummaryRepository();
    }

    private void initListeners() {

        binding.header.imgBack.setOnClickListener(view -> {
            finish();
        });

        binding.btnSearch.setOnClickListener(view -> {
            binding.progressBar.setVisibility(View.VISIBLE);
            Handler handler = new Handler();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    handler.removeCallbacks(this);
                    getMonthlyExpense();
                }
            });
        });

        binding.checkViewSummary.setOnCheckedChangeListener((compoundButton, b) -> {
            if(b){
                binding.tvSummary.setVisibility(View.VISIBLE);
            } else
                binding.tvSummary.setVisibility(View.GONE);
        });

        binding.btnExportSummary.setOnClickListener(view -> {
            showAlertDialog();
        });

        binding.btnClear.setOnClickListener(view -> {
            deleteExportedSummary();
        });
    }

    private void getMonthlyExpense() {
        String month = DateFormat.getMonthDigitByName((String) binding.monthSpinner.getSelectedItem());
        String year = (String) binding.yearSpinner.getSelectedItem();
        String finalDate = month + "-" +year;
        System.out.println("getMonthlyExpense() :: Date-> "+finalDate);

        List<ExpenseEntity> expenseList = expenseRepository.getExpensesByMonth(finalDate);
        Collections.reverse(expenseList);

        double totalExpense = 0;
        for(ExpenseEntity entity : expenseList){
            totalExpense += entity.getExpense();
        }
        double exportedExpense = exportSummaryRepository.getTotalExportedExpenseOfMonthYear(Integer.parseInt(month) , Integer.parseInt(year));
        binding.tvMonthExpense.setText(String.valueOf(totalExpense+exportedExpense));
        notifyAdapter(expenseList , month , year);
    }


    private void setExpenseAdapter() {
        adapter = new ViewExpenseAdapter(this,new ArrayList<>());
        binding.rcvExpenses.setAdapter(adapter);
        adapter.setItemClickListener(new ViewExpenseAdapter.ItemClickListener() {
            @Override
            public void onClickRemove(int id, int position) {
                expenseRepository.deleteExpense(id);
                adapter.deleteExpense(position);
                getMonthlyExpense();
            }

            @Override
            public void onDataLoaded(){
                binding.progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void notifyAdapter(List<ExpenseEntity> expenseEntityList , String month , String year){

        //We are restricted to export the summary of current month.
        if(DateFormat.getCurrentMonth() == Integer.parseInt(month) && DateFormat.getCurrentYear() == Integer.parseInt(year)){
            binding.exportSummaryLayout.setVisibility(View.GONE);
        } else {
            binding.exportSummaryLayout.setVisibility(View.VISIBLE);
        }

        /*
        1. Check from expenses if any expenses are found. If not(Size == 0) then check is the expenses of current month
           is Exported.
        2. If exported then show the summary or not found then no expenses done of that month.
         */
        if(expenseEntityList.size() == 0){

            //Check that the month is already exported.
            ExportSummaryEntity summaryEntity = exportSummaryRepository.getSummary(Integer.parseInt(month) , Integer.parseInt(year));
            if(summaryEntity == null){
                Toast.makeText(this, "No Expense found", Toast.LENGTH_SHORT).show();
                binding.summaryLayout.setVisibility(View.GONE);
            } else {
                binding.tvMonthExpense.setText(String.valueOf(summaryEntity.getTotalExpense()));
                String summary = summaryEntity.getSummary().replace(" ","\n");
                summaryEntity.setSummary(summary);
                binding.tvSummary.setText(summary);
                binding.summaryLayout.setVisibility(View.VISIBLE);
            }
            binding.progressBar.setVisibility(View.GONE);
            binding.exportSummaryLayout.setVisibility(View.GONE);
        } else {
            getExpenseSummary(expenseEntityList);
            binding.summaryLayout.setVisibility(View.VISIBLE);
        }
        adapter.setNewExpenseList(expenseEntityList);
    }

    private void getExpenseSummary(List<ExpenseEntity> expenseEntityList){
        Map<String , Double> expMap = new HashMap<>();
        for(ExpenseEntity entity : expenseEntityList){
            String key = entity.getExpenseOf();
            Double expense = expMap.get(key);
            if(expense == null)
                expMap.put(key,entity.getExpense());
            else {
                expense += entity.getExpense();
                expMap.put(key,expense);
            }
        }
        System.out.println("==== SUMMARY ==== > "+expMap);

        expenseSummary = expMap.toString();
        expenseSummary = expenseSummary.replace("=","->");
        expenseSummary = expenseSummary.replace(", ","\n");
        expenseSummary = expenseSummary.substring(1,expenseSummary.length()-1);
        binding.tvSummary.setText(expenseSummary);
    }

    private void initSpinners(){

        //Month
        ArrayList<String> monthList = new ArrayList<>();
        monthList.add("Jan");
        monthList.add("Feb");
        monthList.add("Mar");
        monthList.add("Apr");
        monthList.add("May");
        monthList.add("Jun");
        monthList.add("Jul");
        monthList.add("Aug");
        monthList.add("Sep");
        monthList.add("Oct");
        monthList.add("Nov");
        monthList.add("Dec");

        ArrayAdapter<String> monthAdapter = new ArrayAdapter<>(this, R.layout.item_spinner_textview, monthList);
        monthAdapter.setDropDownViewResource(R.layout.item_spinner_textview);
        binding.monthSpinner.setAdapter(monthAdapter);

        int currentMonth = DateFormat.getCurrentMonth();
        binding.monthSpinner.setSelection(currentMonth-1);

        //Year
        ArrayList<String> yearList = new ArrayList<>();
        int currentYear = DateFormat.getCurrentYear();
        for(int year = 2024 ; year <= currentYear ; year++)
            yearList.add(String.valueOf(year));

        ArrayAdapter<String> yearAdapter = new ArrayAdapter<>(this, R.layout.item_spinner_textview, yearList);
        yearAdapter.setDropDownViewResource(R.layout.item_spinner_textview);
        binding.yearSpinner.setAdapter(yearAdapter);

        binding.yearSpinner.setSelection(yearList.size()-1);
    }

    private void showAlertDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setIcon(R.drawable.ic_export);
        dialog.setTitle("Export");
        dialog.setMessage("Are you sure you want to export the Monthly Expense ?");
        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                exportExpense();
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        dialog.show();
    }

    private void exportExpense() {
        String month = DateFormat.getMonthDigitByName((String) binding.monthSpinner.getSelectedItem());
        String year = (String) binding.yearSpinner.getSelectedItem();
        String finalDate = month + "-" +year;
        System.out.println("FinalDate: "+finalDate);

        ExportSummaryEntity summaryEntity = exportSummaryRepository.getSummary(Integer.parseInt(month) , Integer.parseInt(year));
        if(summaryEntity != null){
            Toast.makeText(this, "Conflict: Expense is already Exported.", Toast.LENGTH_SHORT).show();
            showMergeSnackBar(summaryEntity ,finalDate);
            return;
        }
        //Exporting the summary
        ExportSummaryEntity entity = new ExportSummaryEntity();
        entity.setMonth(Integer.parseInt(month));
        entity.setYear(Integer.parseInt(year));
        entity.setTotalExpense(Double.parseDouble(binding.tvMonthExpense.getText().toString()));
        entity.setSummary(expenseSummary);
        exportSummaryRepository.exportSummary(entity);

        //Remove all the expenses
        expenseRepository.deleteMonthExpense(finalDate);
        binding.exportSummaryLayout.setVisibility(View.GONE);
        Toast.makeText(this, "Expenses are Exported", Toast.LENGTH_SHORT).show();
        adapter.deleteAllExpenses();
    }

    private void showMergeSnackBar(ExportSummaryEntity entity , String date){
        Snackbar snackbar = Snackbar.make(binding.getRoot(),"Merge to Exported Expense to resolve this conflicts.",Snackbar.LENGTH_LONG)
                .setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE);
        snackbar.setTextColor(ContextCompat.getColorStateList(this, R.color.white));
        snackbar.setActionTextColor(ContextCompat.getColorStateList(this, R.color.green));
        snackbar.show();

        snackbar.setAction("MERGE", view -> {
            String summary = entity.getSummary();
            System.out.println("Summary : "+summary);
            Map<String , Double> expMap = getMapFromString(summary);
            List<ExpenseEntity> expenseList = adapter.getExpenseList();
            double totalExpense = 0;
            for(ExpenseEntity expenseEntity : expenseList){
                String key = expenseEntity.getExpenseOf();
                Double expense = expMap.get(key);
                totalExpense += expenseEntity.getExpense();
                if(expense == null)
                    expMap.put(key,expenseEntity.getExpense());
                else {
                    expense += expenseEntity.getExpense();
                    expMap.put(key,expense);
                }
            }
            System.out.println("==== FINAL MAP ====> "+expMap);
            expenseSummary = expMap.toString();
            expenseSummary = expenseSummary.replace("=","->");
            expenseSummary = expenseSummary.replace(", ","\n");
            expenseSummary = expenseSummary.substring(1,expenseSummary.length()-1);

            expenseRepository.deleteMonthExpense(date);
            entity.setSummary(expenseSummary);
            entity.setTotalExpense(entity.getTotalExpense() + totalExpense);
            exportSummaryRepository.exportSummary(entity);
            getMonthlyExpense();
        });
    }

    private Map<String , Double> getMapFromString(String summary){
        Map<String,Double> expMap = new HashMap<>();
        Log.d(TAG, "getMapFromString: Summary : "+summary);
        String[] arr = summary.split("\\s+");

        for (String s : arr) {
            String[] keyValueArr = s.split("->");
            System.out.println("key value => {"+ keyValueArr[0] + " "+keyValueArr[1]);
            String key = keyValueArr[0].trim();
            Double expense = expMap.get(key);
            if (expense == null)
                expMap.put(key, Double.parseDouble(keyValueArr[1].trim()));
            else {
                expense += Double.parseDouble(keyValueArr[1].trim());
                expMap.put(key, expense);
            }
        }
        System.out.println("MAP : "+expMap);
        return  expMap;
    }

    private void deleteExportedSummary(){
        Snackbar snackbar = Snackbar.make(binding.getRoot(),"You want to delete the summary permanently?",Snackbar.LENGTH_LONG)
                .setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE);
        snackbar.setTextColor(ContextCompat.getColorStateList(this, R.color.white));
        snackbar.setActionTextColor(ContextCompat.getColorStateList(this, R.color.red));
        snackbar.show();

        snackbar.setAction("DELETE",view -> {
            String month = DateFormat.getMonthDigitByName((String) binding.monthSpinner.getSelectedItem());
            String year = (String) binding.yearSpinner.getSelectedItem();

            if(DateFormat.getCurrentMonth() == Integer.parseInt(month) && DateFormat.getCurrentYear() == Integer.parseInt(year)){
                Toast.makeText(this, "Failed! Expenses of current month is not exported yet.", Toast.LENGTH_SHORT).show();
                return;
            }
            exportSummaryRepository.clearExportedSummary(Integer.parseInt(month) , Integer.parseInt(year));
            Toast.makeText(this, "Cleared", Toast.LENGTH_SHORT).show();
            getMonthlyExpense();
        });
    }

}