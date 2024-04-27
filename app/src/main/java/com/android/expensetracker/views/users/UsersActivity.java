package com.android.expensetracker.views.users;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.android.expensetracker.databinding.ActivityUsersBinding;

import java.util.ArrayList;
import java.util.List;

public class UsersActivity extends AppCompatActivity {

    private ActivityUsersBinding binding;
    private UsersRepository usersRepository;
    private List<Integer> removeUsersIdList ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityUsersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        usersRepository = new UsersRepository();
        initListeners();
        getUsersFromDB();
    }

    private void getUsersFromDB(){
        List<UsersEntity> usersList =  usersRepository.getAllUsers();
        setAdapter(usersList);
    }

    private void setAdapter(List<UsersEntity> usersList){
        UsersAdapter usersAdapter = new UsersAdapter(this, usersList);
        binding.rcvUsers.setAdapter(usersAdapter);

        usersAdapter.setItemClickListener(id -> {
            if(removeUsersIdList == null){
                removeUsersIdList = new ArrayList<>();
            }
            removeUsersIdList.add(id);
        });
    }



    private void initListeners() {

        binding.btnSave.setOnClickListener(view -> {

            String name = binding.etName.getText().toString().trim();
            String number = binding.etNumber.getText().toString().trim();
            if(!TextUtils.isEmpty(name)){
                saveUser(name, number);
            } else
                Toast.makeText(this, "Enter name", Toast.LENGTH_SHORT).show();
        });
    }

    private void saveUser(String name , String number) {
        usersRepository.save(new UsersEntity(name,number));
        deleteUsers();
    }

    private void deleteUsers(){
        if(removeUsersIdList != null){
            usersRepository.deleteUsers(removeUsersIdList);
        }
    }
}