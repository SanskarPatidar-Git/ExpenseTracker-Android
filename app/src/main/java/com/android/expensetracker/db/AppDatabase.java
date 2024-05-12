package com.android.expensetracker.db;


import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.android.expensetracker.Constants;
import com.android.expensetracker.MyBaseApplication;
import com.android.expensetracker.views.monthexpense.ExportSummaryDao;
import com.android.expensetracker.views.monthexpense.ExportSummaryEntity;
import com.android.expensetracker.views.transactions.TransactionDao;
import com.android.expensetracker.views.transactions.TransactionEntity;
import com.android.expensetracker.views.users.UsersDao;
import com.android.expensetracker.views.users.UsersEntity;
import com.android.expensetracker.views.addexpense.ExpenseDao;
import com.android.expensetracker.views.addexpense.ExpenseEntity;

@Database(entities = {ExpenseEntity.class , UsersEntity.class , TransactionEntity.class , ExportSummaryEntity.class} , version = 5)
public abstract class AppDatabase extends RoomDatabase{

    private static AppDatabase appDatabase;

    public static AppDatabase getInstance() {

        if (appDatabase == null) {
            appDatabase = Room.databaseBuilder(MyBaseApplication.getInstance(),
                            AppDatabase.class,
                            Constants.DB_NAME)
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();
        }
        return appDatabase;
    }

    public abstract ExpenseDao getExpenseDao();

    public abstract UsersDao getUsersDao();

    public abstract TransactionDao getTransactionDao();

    public abstract ExportSummaryDao getExportSummaryDao();
}
