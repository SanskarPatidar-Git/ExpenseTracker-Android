package com.android.expensetracker.views.addexpense;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "tbl_expenses")
public class ExpenseEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @Ignore
    private Date date;

    @ColumnInfo(name = "expense_of")
    private String expenseOf;

    @ColumnInfo(name = "expense")
    private double expense;

    @ColumnInfo(name = "expense_by")
    private String expenseBy;

    @ColumnInfo(name = "description")
    private String description;

    public ExpenseEntity(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getExpenseOf() {
        return expenseOf;
    }

    public void setExpenseOf(String expenseOf) {
        this.expenseOf = expenseOf;
    }

    public double getExpense() {
        return expense;
    }

    public void setExpense(double expense) {
        this.expense = expense;
    }

    public String getExpenseBy() {
        return expenseBy;
    }

    public void setExpenseBy(String expenseBy) {
        this.expenseBy = expenseBy;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "ExpenseEntity{" +
                "id=" + id +
                ", date=" + date +
                ", expenseOf='" + expenseOf + '\'' +
                ", expense=" + expense +
                ", expenseBy='" + expenseBy + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
