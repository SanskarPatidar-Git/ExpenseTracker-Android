package com.android.expensetracker.views.transactions;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TransactionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void add(TransactionEntity entity);

    @Query("SELECT * FROM tbl_transactions")
    List<TransactionEntity> getTransactions();

    @Update
    void update(TransactionEntity entity);

    @Query("DELETE FROM tbl_transactions WHERE id = :id")
    void deleteById(int id);

    @Query("SELECT SUM(`transaction`) FROM tbl_transactions WHERE mode = 1")
    double getTotalLentTransaction();

    @Query("SELECT SUM(`transaction`) FROM tbl_transactions WHERE mode = 0")
    double getTotalBorrowedTransaction();

    @Query("SELECT * FROM tbl_transactions WHERE user_id = :userId AND mode = :mode")
    TransactionEntity getPreviousTransaction(int userId , int mode);
}
