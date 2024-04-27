package com.android.expensetracker.views.transactions;

import androidx.room.Dao;
import androidx.room.Insert;

@Dao
public interface TransactionDao {

    @Insert
    void add(TransactionEntity entity);

}
