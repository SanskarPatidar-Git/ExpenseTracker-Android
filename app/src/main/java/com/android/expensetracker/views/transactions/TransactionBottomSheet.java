package com.android.expensetracker.views.transactions;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.android.expensetracker.Constants;
import com.android.expensetracker.R;
import com.android.expensetracker.databinding.BottomsheetLentBorrowTransactionsBinding;
import com.android.expensetracker.utility.DateFormat;
import com.android.expensetracker.views.users.UsersEntity;
import com.android.expensetracker.views.users.UsersRepository;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class TransactionBottomSheet extends BottomSheetDialogFragment {

    private BottomsheetLentBorrowTransactionsBinding binding;
    private int mode ;
    private TransactionEntity transactionEntity;
    private List<UsersEntity> usersList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = BottomsheetLentBorrowTransactionsBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init();
        getUsers();
        initListeners();
    }

    private void init() {
        transactionEntity = new TransactionEntity();
        Bundle bundle = getArguments();
        if(bundle != null){
            mode = bundle.getInt(Constants.MODE);
        }

        if(mode == Constants.LENT_MODE){
            binding.btnAddTransaction.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.green));
        } else
            binding.btnAddTransaction.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.red));
    }

    private void initListeners() {

        binding.etTransactionDate.setOnClickListener(view -> {
            DateFormat.getDateFromCalender(getActivity(), date -> {
                transactionEntity.setDate(date);
                binding.etTransactionDate.setText(date);
            });
        });

        binding.usersSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int position = adapterView.getSelectedItemPosition();
                transactionEntity.setUserId(usersList.get(position).getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        binding.btnAddTransaction.setOnClickListener(view -> {
            if(isDetailsValid()){
                transactionEntity.setMode(mode);
                transactionEntity.setStatus(Constants.TRANSACTION_PENDING);
                new TransactionRepository().save(transactionEntity);
                Toast.makeText(getContext(), "Saved", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });
    }

    public void getUsers(){
        usersList = new UsersRepository().getAllUsers();
        setUserDataToAdapter();
    }

    private void setUserDataToAdapter() {
        List<String> userNameList = new ArrayList<>();
        for(UsersEntity entity : usersList)
            userNameList.add(entity.getName());

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.item_spinner_textview, userNameList);
        adapter.setDropDownViewResource(R.layout.item_spinner_textview);
        binding.usersSpinner.setAdapter(adapter);
    }

    private boolean isDetailsValid(){
        String expense = binding.etTransaction.getText().toString().trim();
        if(TextUtils.isEmpty(expense))
            transactionEntity.setTransaction(0);
        else
            transactionEntity.setTransaction(Double.parseDouble(expense));

        if(transactionEntity.getDate() == null)
            Toast.makeText(getContext(), "Select date", Toast.LENGTH_SHORT).show();
        else if(transactionEntity.getUserId() == 0)
            Toast.makeText(getContext(), "Select user", Toast.LENGTH_SHORT).show();
        else if(transactionEntity.getTransaction() == 0)
            Toast.makeText(getContext(), "Select transaction", Toast.LENGTH_SHORT).show();
        else
            return true;

        return false;
    }

}
