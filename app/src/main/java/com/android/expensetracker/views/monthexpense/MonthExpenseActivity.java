package com.android.expensetracker.views.monthexpense;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MonthExpenseActivity extends AppCompatActivity {

    private ActivityMonthExpenseBinding binding;
    private ExpenseRepository expenseRepository;
    private ViewExpenseAdapter adapter;

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
        binding.tvMonthExpense.setText(String.valueOf(totalExpense));
        notifyAdapter(expenseList);
    }

    private void setExpenseAdapter() {
        adapter = new ViewExpenseAdapter(this,new ArrayList<>());
        binding.rcvExpenses.setAdapter(adapter);
        adapter.setItemClickListener(new ViewExpenseAdapter.ItemClickListener() {
            @Override
            public void onClickRemove(int id, int position) {
                expenseRepository.deleteExpense(id);
                adapter.deleteExpense(position);
            }

            @Override
            public void onDataLoaded(){
                binding.progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void notifyAdapter(List<ExpenseEntity> expenseEntityList){
        if(expenseEntityList.size() == 0){
            Toast.makeText(this, "No Expense found", Toast.LENGTH_SHORT).show();
            binding.summaryLayout.setVisibility(View.GONE);
            binding.progressBar.setVisibility(View.GONE);
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

        String expenseSummary = expMap.toString();
        expenseSummary = expenseSummary.replace("=","  :  ");
        expenseSummary = expenseSummary.replace(", ","\n");
        int length = expenseSummary.length();
        expenseSummary = expenseSummary.substring(1,length-1);
        binding.tvSummary.setText(expenseSummary);
//        expMap.forEach((key, value) -> {
//
//        });
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

//    private void createPieChart(List<ExpenseEntity> expenseList) {
//        ArrayList<PieEntry> entries = new ArrayList<>();
//        ArrayList<Integer> colors = new ArrayList<>();
//
//        float totalExpenses = 0f;
//
//        // Calculate total expenses for the selected time period
//        for (ExpenseModel expense : expenseList) {
//            totalExpenses += expense.getAmount();
//        }
//
//        // Iterate over the expense list to calculate total expenses for each category
//        for (ExpenseModel expense : expenseList) {
//            float amount = (float) expense.getAmount();
//            String category = expense.getExpenseCategory();
//
//            // Skip categories with zero expenses
//            if (amount <= 0) {
//                continue;
//            }
//
//            // Add the category to the pie chart
//            entries.add(new PieEntry(amount / totalExpenses * 100, category));
//
//            // Set color for the category
//            int color = getColorForCategory(category);
//            colors.add(color);
//        }
//
//        // Create the dataset and set the data
//        PieDataSet dataSet = new PieDataSet(entries, "Pie Chart");
//        dataSet.setColors(colors);
//        dataSet.setDrawValues(true);
//        dataSet.setValueTextColor(Color.WHITE); // Value text color
//        dataSet.setValueTextSize(10f); // Value text size
//        dataSet.setSliceSpace(0f); // Space between slices
//
//        PieData data = new PieData(dataSet);
//
//        // Set data and customize the pie chart
//        PieChart pieChart = binding.pieChart;
//        pieChart.setData(data);
//        pieChart.setCenterText("Category Wise Expense");
//        pieChart.setCenterTextSize(18f);
//        pieChart.setHoleRadius(0);
//        pieChart.setTransparentCircleRadius(0);
//        pieChart.setDrawEntryLabels(false);
//        pieChart.getLegend().setEnabled(false);
//        pieChart.animateY(1000);
//
//        // Set hole color for each slice
//        for (int i = 0; i < entries.size(); i++) {
//            int sliceColor = colors.get(i);
//            pieChart.setHoleColor(sliceColor);
//        }
//
//        // Clear the legend container before adding new legend entries
//        LinearLayout legendContainer = binding.linExpenseLegendContainer;
//        legendContainer.removeAllViews();
//
//        // Add legend entries for each category
//        for (int i = 0; i < entries.size(); i++) {
//            PieEntry entry = entries.get(i);
//            int color = colors.get(i);
//            String label = entry.getLabel();
//
//
//            TextView legendEntry = new TextView(getContext());
//            legendEntry.setLayoutParams(new LinearLayout.LayoutParams(
//                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
//            ));
//            legendEntry.setPadding(16, 0, 16, 3);
//            legendEntry.setCompoundDrawablePadding(8);
//            legendEntry.setGravity(Gravity.CENTER_VERTICAL);
//            legendEntry.setCompoundDrawablesWithIntrinsicBounds(R.drawable.legend_dot, 0, 0, 0);
//            legendEntry.setText(label + " (" + String.format("%.2f", entry.getValue()) + "%)");
//            legendEntry.setTextColor(Color.BLACK);
//            legendEntry.setTextSize(12f);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                legendEntry.setCompoundDrawableTintList(ColorStateList.valueOf(color));
//            }
//
//            legendContainer.addView(legendEntry);
//        }
//    }
//
//    private int getColorForCategory(String category) {
//
//        int hash = category.hashCode();
//        int color = Color.HSVToColor(new float[]{
//                (hash & 0xFF) / 255.0f * 360.0f,
//                1.0f,
//                0.8f
//        });
//        return color;
//    }
}