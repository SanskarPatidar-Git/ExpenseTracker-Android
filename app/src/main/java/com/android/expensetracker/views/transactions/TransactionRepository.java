package com.android.expensetracker.views.transactions;

import com.android.expensetracker.db.AppDatabase;

import java.util.List;

public class TransactionRepository {

    private final TransactionDao transactionDao;

    public TransactionRepository(){
        this.transactionDao = AppDatabase.getInstance().getTransactionDao();
    }

    public void save(TransactionEntity entity){
        transactionDao.add(entity);
    }

    public List<TransactionEntity> getTransactions(){
        return transactionDao.getTransactions();
    }

    public void update(TransactionEntity entity){
        transactionDao.update(entity);
    }

    public void delete(int id){
        transactionDao.deleteById(id);
    }

    public double getTotalLent(){
        return transactionDao.getTotalLentTransaction();
    }

    public double getTotalBorrowed(){
        return transactionDao.getTotalBorrowedTransaction();
    }

    public TransactionEntity getPreviousTransaction(int userId , int mode){
        return transactionDao.getPreviousTransaction(userId, mode);
    }
}
