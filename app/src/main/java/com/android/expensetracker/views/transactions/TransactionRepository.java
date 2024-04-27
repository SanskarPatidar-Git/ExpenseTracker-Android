package com.android.expensetracker.views.transactions;

import com.android.expensetracker.db.AppDatabase;

public class TransactionRepository {

    private final TransactionDao transactionDao;

    public TransactionRepository(){
        this.transactionDao = AppDatabase.getInstance().getTransactionDao();
    }

    public void save(TransactionEntity entity){
        transactionDao.add(entity);
    }
}
