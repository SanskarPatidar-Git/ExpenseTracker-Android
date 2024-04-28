package com.android.expensetracker.views.viewtransactions;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.expensetracker.Constants;
import com.android.expensetracker.R;
import com.android.expensetracker.databinding.ActivityViewTransactionBinding;
import com.android.expensetracker.views.transactions.TransactionEntity;
import com.android.expensetracker.views.transactions.TransactionRepository;
import com.android.expensetracker.views.users.UsersEntity;
import com.android.expensetracker.views.users.UsersRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ViewTransactionActivity extends AppCompatActivity {

    private ActivityViewTransactionBinding binding;
    private TransactionRepository transactionRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityViewTransactionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
        initListeners();
        getTransactions();
    }

    private void init(){
        transactionRepository = new TransactionRepository();
        binding.header.tvTitle.setText("All Transactions");
    }

    private void initListeners() {

        binding.header.imgBack.setOnClickListener(view -> {
            finish();
        });
    }

    private void getTransactions(){
        List<TransactionEntity> transactionList = transactionRepository.getTransactions();
        List<TransactionEntity> lentTransactionList = new ArrayList<>();
        List<TransactionEntity> borrowedTransactionList = new ArrayList<>();

        for(TransactionEntity entity : transactionList){
            if(entity.getMode() == Constants.LENT_MODE)
                lentTransactionList.add(entity);
            else
                borrowedTransactionList.add(entity);
        }

        List<UsersEntity> userList = getUsers();
        if(lentTransactionList.size() == 0)
            binding.lentTransactionsLayout.setVisibility(View.GONE);
        else {
            Collections.reverse(lentTransactionList);
            setLentTransactionAdapter(lentTransactionList , userList);
        }


        if(borrowedTransactionList.size() == 0)
            binding.borrowTransactionsLayout.setVisibility(View.GONE);
        else {
            Collections.reverse(borrowedTransactionList);
            setBorrowedTransactionAdapter(borrowedTransactionList , userList);
        }

    }

    private void setBorrowedTransactionAdapter(List<TransactionEntity> borrowedTransactionList , List<UsersEntity> userList) {
        ViewTransactionAdapter adapter = new ViewTransactionAdapter(this , borrowedTransactionList , Constants.BORROWED_MODE , userList);
        binding.rcvBorrow.setAdapter(adapter);
        adapter.setItemClickListener(new ViewTransactionAdapter.ItemClickListener() {
            @Override
            public void onClickSave(TransactionEntity entity ,int position) {
                if(entity.getTransaction() == 0){
                    transactionRepository.delete(entity.getId());
                    adapter.deleteTransaction(position);
                } else{
                    transactionRepository.update(entity);
                    adapter.lockTransaction(position);
                    Toast.makeText(ViewTransactionActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onClickCall(String number) {
                intentCall(number);
            }
        });
    }

    private void setLentTransactionAdapter(List<TransactionEntity> lentTransactionList , List<UsersEntity> userList) {
        ViewTransactionAdapter adapter = new ViewTransactionAdapter(this , lentTransactionList , Constants.LENT_MODE , userList);
        binding.rcvLent.setAdapter(adapter);
        adapter.setItemClickListener(new ViewTransactionAdapter.ItemClickListener() {
            @Override
            public void onClickSave(TransactionEntity entity, int position) {
                System.out.println("setLentTransactionAdapter -> "+entity);
                if(entity.getTransaction() == 0){
                    transactionRepository.delete(entity.getId());
                    adapter.deleteTransaction(position);
                } else{
                    transactionRepository.update(entity);
                    adapter.lockTransaction(position);
                    Toast.makeText(ViewTransactionActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onClickCall(String number) {
                intentCall(number);
            }
        });
    }

    private void intentCall(String number){
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + number));
        startActivity(intent);
    }

    private List<UsersEntity> getUsers(){
        return new UsersRepository().getAllUsers();
    }
}