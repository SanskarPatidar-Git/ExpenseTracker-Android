package com.android.expensetracker.views.addexpense;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.expensetracker.R;
import com.android.expensetracker.databinding.ActivityAddExpenseBinding;
import com.android.expensetracker.utility.DateFormat;
import com.android.expensetracker.utility.DatePickerListener;

import java.util.ArrayList;
import java.util.Date;

public class AddExpenseActivity extends AppCompatActivity {

    private ActivityAddExpenseBinding binding;
    private ExpenseEntity expenseEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAddExpenseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
        initListeners();
        insertDummyData();
    }

    private void init(){
        binding.header.tvTitle.setText("Add Expense");
        expenseEntity = new ExpenseEntity();
    }

    private void initListeners() {

        binding.header.imgBack.setOnClickListener(view -> {
            finish();
        });

        binding.checkTodayDate.setOnCheckedChangeListener((compoundButton, b) -> {
            if(b){
                binding.etExpenseDate.setClickable(false);
                binding.etExpenseDate.setFocusable(false);
                binding.etExpenseDate.setText(DateFormat.getCurrentDate().toString());
                expenseEntity.setDate(DateFormat.getCurrentDate());
            } else {
                binding.etExpenseDate.setClickable(true);
                binding.etExpenseDate.setFocusable(true);
                expenseEntity.setDate(null);
                binding.etExpenseDate.setText("");
            }
        });

        binding.etExpenseDate.setOnClickListener(view -> {
            DateFormat.getDateFromCalender(AddExpenseActivity.this, new DatePickerListener() {
                @Override
                public void onSelectDate(Date date) {
                    expenseEntity.setDate(date);
                    binding.etExpenseDate.setText(date.toString());
                }
            });
        });

        binding.expenseOfSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.radioGroupExpenseBy.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                String expenseBy = radioGroup.getTransitionName();
                expenseEntity.setExpenseBy(expenseBy);
            }
        });

        binding.btnAddExpense.setOnClickListener(view -> {
            if(isExpenseDetailsValid()){
                addExpenseToDB();
            }
        });
    }

    private void addExpenseToDB() {
        new AddExpenseRepository().addExpense(expenseEntity);
    }

    private boolean isExpenseDetailsValid(){
        expenseEntity.setExpense(Double.parseDouble(binding.etExpense.getText().toString()));
        expenseEntity.setDescription(binding.etDescription.getText().toString().trim());
        System.out.println("==== "+expenseEntity);
        if(expenseEntity.getDate() == null)
            Toast.makeText(this, "Select date", Toast.LENGTH_SHORT).show();
        else if(expenseEntity.getExpenseOf() == null)
            Toast.makeText(this, "Select Expense of", Toast.LENGTH_SHORT).show();
        else if(expenseEntity.getExpense() == 0)
            Toast.makeText(this, "Select Expense", Toast.LENGTH_SHORT).show();
        else if(expenseEntity.getExpenseBy() == null)
            Toast.makeText(this, "Select Expense by", Toast.LENGTH_SHORT).show();
        else
            return true;
        return false;
    }

    private void insertDummyData(){
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Pankaj");
        arrayList.add("Sanskar");
        arrayList.add("Chirag");
        arrayList.add("Prajju");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1 , arrayList);
        binding.expenseOfSpinner.setAdapter(adapter);
    }
}