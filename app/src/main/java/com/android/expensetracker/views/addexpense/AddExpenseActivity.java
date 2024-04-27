package com.android.expensetracker.views.addexpense;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.Toast;

import com.android.expensetracker.R;
import com.android.expensetracker.databinding.ActivityAddExpenseBinding;
import com.android.expensetracker.utility.DateFormat;

import java.util.ArrayList;

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
                binding.etExpenseDate.setEnabled(false);
                binding.etExpenseDate.setFocusable(false);
                String currentDate = DateFormat.getCurrentDate();
                binding.etExpenseDate.setText(currentDate);
                expenseEntity.setDate(currentDate);
            } else {
                binding.etExpenseDate.setEnabled(true);
                binding.etExpenseDate.setFocusable(true);
                expenseEntity.setDate(null);
                binding.etExpenseDate.setText("");
            }
        });

        binding.etExpenseDate.setOnClickListener(view -> {
            DateFormat.getDateFromCalender(AddExpenseActivity.this, date -> {
                expenseEntity.setDate(date);
                binding.etExpenseDate.setText(date);
            });
        });

        binding.expenseOfSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String expenseOf = (String) adapterView.getSelectedItem();
                expenseEntity.setExpenseOf(expenseOf);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });


        binding.radioGroupExpenseBy.setOnCheckedChangeListener((radioGroup, i) -> {
            RadioButton radioButton = findViewById(i);
            if (radioButton != null) {
                String expenseBy = radioButton.getText().toString();
                expenseEntity.setExpenseBy(expenseBy);
            }

        });

        binding.btnAddExpense.setOnClickListener(view -> {
            if(isExpenseDetailsValid()){
                addExpenseToDB();
                Toast.makeText(this, "Expense Added", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void addExpenseToDB() {
        new ExpenseRepository().addExpense(expenseEntity);
    }

    private boolean isExpenseDetailsValid(){
        String expense = binding.etExpense.getText().toString().trim();
        if(TextUtils.isEmpty(expense))
            expenseEntity.setExpense(0);
        else
            expenseEntity.setExpense(Double.parseDouble(expense));
        expenseEntity.setDescription(binding.etDescription.getText().toString().trim());
        System.out.println("AddExpenseActivity::isExpenseDetailsValid() ExpenseEntity -> "+expenseEntity);

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
        arrayList.add("Lunch");
        arrayList.add("Breakfast");
        arrayList.add("Tea");
        arrayList.add("Petrol");
        arrayList.add("Movie");
        arrayList.add("Other");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.item_spinner_textview, arrayList);
        adapter.setDropDownViewResource(R.layout.item_spinner_textview);
        binding.expenseOfSpinner.setAdapter(adapter);

    }
}