package com.android.expensetracker.db;


import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.android.expensetracker.Constants;
import com.android.expensetracker.MyBaseApplication;
import com.android.expensetracker.views.addexpense.ExpenseDao;
import com.android.expensetracker.views.addexpense.ExpenseEntity;

@Database(entities = {ExpenseEntity.class} , version = 1)
public abstract class AppDatabase extends RoomDatabase{

    private static AppDatabase appDatabase;

    public static AppDatabase getInstance() {

        if (appDatabase == null) {
            appDatabase = Room.databaseBuilder(MyBaseApplication.getInstance(),
                            AppDatabase.class,
                            Constants.DB_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return appDatabase;
    }

    public abstract ExpenseDao getExpenseDao();
}
