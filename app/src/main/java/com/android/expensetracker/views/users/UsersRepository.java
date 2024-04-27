package com.android.expensetracker.views.users;

import com.android.expensetracker.db.AppDatabase;

import java.util.List;

public class UsersRepository {

    private UsersDao usersDao;

    public UsersRepository(){
        usersDao = AppDatabase.getInstance().getUsersDao();
    }

    public void save(UsersEntity entity){
        usersDao.saveUser(entity);
    }

    public List<UsersEntity> getAllUsers(){
        return usersDao.getAllUsers();
    }

    public void deleteUsers(List<Integer> idList){
        for(Integer id : idList){
            usersDao.deleteUser(id);
        }
    }
}
