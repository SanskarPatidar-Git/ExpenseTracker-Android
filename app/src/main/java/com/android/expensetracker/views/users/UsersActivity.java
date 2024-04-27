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

        init();
        initListeners();
        getUsersFromDB();
    }

    private void init(){
        usersRepository = new UsersRepository();
        binding.header.tvTitle.setText("Users");
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

        binding.header.imgBack.setOnClickListener(view -> {
            finish();
        });

        binding.btnSave.setOnClickListener(view -> {

            String name = binding.etName.getText().toString().trim();
            String number = binding.etNumber.getText().toString().trim();
            boolean isSaved = false;

            if(removeUsersIdList != null){
                deleteUsers();
                isSaved = true;
            }
            if(!TextUtils.isEmpty(name)){
                saveUser(name, number);
                isSaved = true;
            } else if(!isSaved)
                Toast.makeText(this, "Enter name", Toast.LENGTH_SHORT).show();

            if(isSaved){
                Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
                finish();
            }

        });
    }

    private void saveUser(String name , String number) {
        usersRepository.save(new UsersEntity(name,number));
    }

    private void deleteUsers(){
        usersRepository.deleteUsers(removeUsersIdList);
    }
}