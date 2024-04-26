package com.android.expensetracker.views.addexpense;

import com.android.expensetracker.db.AppDatabase;

public class AddExpenseRepository {

    public void addExpense(ExpenseEntity entity){
        AppDatabase.getInstance().getExpenseDao().add(entity);
    }
}
