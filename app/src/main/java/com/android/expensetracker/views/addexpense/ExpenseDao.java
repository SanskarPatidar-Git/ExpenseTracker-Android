package com.android.expensetracker.views.addexpense;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ExpenseDao {

    @Insert
    void add(ExpenseEntity entity);

    @Query("SELECT * FROM tbl_expenses")
    List<ExpenseEntity> getAllExpenses();

    @Query("DELETE FROM tbl_expenses WHERE id = :id")
    public void delete(int id);

    @Query("SELECT * FROM tbl_expenses WHERE expense_date = :currentDate")
    public List<ExpenseEntity> getExpensesOfToday(String currentDate);
}
