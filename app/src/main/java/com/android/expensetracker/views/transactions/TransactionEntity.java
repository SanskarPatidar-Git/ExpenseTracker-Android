package com.android.expensetracker.views.transactions;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "tbl_transactions")
public class TransactionEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "user_id")
    private int userId;

    @ColumnInfo(name = "transaction_date")
    private String date;

    @ColumnInfo(name = "transaction")
    private double transaction;

    @ColumnInfo(name = "mode")
    private int mode;

    @ColumnInfo(name = "status")
    private int status;

    @Ignore
    private boolean isEditable;

    public TransactionEntity(){

    }

    public TransactionEntity(int userId, String date, double transaction, int mode, int status) {
        this.userId = userId;
        this.date = date;
        this.transaction = transaction;
        this.mode = mode;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getTransaction() {
        return transaction;
    }

    public void setTransaction(double transaction) {
        this.transaction = transaction;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isEditable() {
        return isEditable;
    }

    public void setEditable(boolean editable) {
        isEditable = editable;
    }

    @Override
    public String toString() {
        return "TransactionEntity{" +
                "id=" + id +
                ", userId=" + userId +
                ", date='" + date + '\'' +
                ", transaction=" + transaction +
                ", mode=" + mode +
                ", status=" + status +
                '}';
    }
}
