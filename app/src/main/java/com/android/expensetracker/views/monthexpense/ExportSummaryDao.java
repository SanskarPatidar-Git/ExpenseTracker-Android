package com.android.expensetracker.views.monthexpense;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ExportSummaryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void add(ExportSummaryEntity entity);

    @Query("SELECT * FROM tbl_export_month_summary WHERE month = :month AND year = :year")
    ExportSummaryEntity get(int month , int year);

    @Query("SELECT SUM(total_expense) FROM tbl_export_month_summary WHERE year = :year")
    double getTotalExportedExpenseOfYear(int year);

    @Query("SELECT SUM(total_expense) FROM tbl_export_month_summary WHERE month = :month AND year = :year")
    double getTotalExportedExpenseOfMonthYear(int month , int year);

}
