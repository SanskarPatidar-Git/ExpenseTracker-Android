package com.android.expensetracker.views.users;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UsersDao {

    @Query("SELECT * FROM tbl_users")
    List<UsersEntity> getAllUsers();

    @Insert
    void saveUser(UsersEntity entity);

    @Query("DELETE FROM tbl_users WHERE id = :id")
    void deleteUser(int id);
}
