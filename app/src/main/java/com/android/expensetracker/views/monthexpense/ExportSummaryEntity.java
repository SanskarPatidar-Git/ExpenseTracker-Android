package com.android.expensetracker.views.monthexpense;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tbl_export_month_summary")
public class ExportSummaryEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "month")
    private int month;

    @ColumnInfo(name = "year")
    private int year;

    @ColumnInfo(name = "summary")
    private String summary;

    @ColumnInfo(name = "total_expense")
    private Double totalExpense;

    public ExportSummaryEntity(){

    }

    public ExportSummaryEntity(int month, int year, String summary, Double totalExpense) {
        this.month = month;
        this.year = year;
        this.summary = summary;
        this.totalExpense = totalExpense;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Double getTotalExpense() {
        return totalExpense;
    }

    public void setTotalExpense(Double totalExpense) {
        this.totalExpense = totalExpense;
    }

    @Override
    public String toString() {
        return "ExportSummaryEntity{" +
                "id=" + id +
                ", month=" + month +
                ", year=" + year +
                ", summary='" + summary + '\'' +
                ", totalExpense=" + totalExpense +
                '}';
    }
}
