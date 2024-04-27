package com.android.expensetracker.views.addexpense;

import com.android.expensetracker.db.AppDatabase;

import java.util.List;

public class ExpenseRepository {

    private final ExpenseDao expenseDao;

    public ExpenseRepository(){
        this.expenseDao = AppDatabase.getInstance().getExpenseDao();
    }

    public void addExpense(ExpenseEntity entity){
        expenseDao.add(entity);
    }

    public List<ExpenseEntity> getAllExpenses(){
        return expenseDao.getAllExpenses();
    }
}
