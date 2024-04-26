package com.android.expensetracker.views.addexpense;

import androidx.room.Dao;
import androidx.room.Insert;

@Dao
public interface ExpenseDao {

    @Insert
    void add(ExpenseEntity entity);
}
