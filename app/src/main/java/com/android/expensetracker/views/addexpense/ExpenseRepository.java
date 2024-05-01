package com.android.expensetracker.views.addexpense;

import com.android.expensetracker.db.AppDatabase;
import com.android.expensetracker.utility.DateFormat;

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

    public void deleteExpense(int id){
        expenseDao.delete(id);
    }

    public List<ExpenseEntity> getExpensesOfToday(){
        return expenseDao.getExpensesOfToday(DateFormat.getCurrentDate());
    }

    public void deleteAllExpenses(){
        expenseDao.deleteAll();
    }

    public List<ExpenseEntity> getExpensesByMonth(String date){
        return expenseDao.getExpensesByMonth(date);
    }
}
