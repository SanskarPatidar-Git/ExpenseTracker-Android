package com.android.expensetracker.views.monthexpense;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ExportSummaryDao {

    @Insert
    void add(ExportSummaryEntity entity);

    @Query("SELECT * FROM tbl_export_month_summary WHERE month = :month AND year = :year")
    ExportSummaryEntity get(int month , int year);
}
