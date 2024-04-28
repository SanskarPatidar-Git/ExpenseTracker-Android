package com.android.expensetracker.views.viewtransactions;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.widget.TextViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.expensetracker.Constants;
import com.android.expensetracker.R;
import com.android.expensetracker.databinding.ItemTransactionBinding;
import com.android.expensetracker.utility.DateFormat;
import com.android.expensetracker.views.transactions.TransactionEntity;
import com.android.expensetracker.views.users.UsersEntity;

import java.util.List;

public class ViewTransactionAdapter extends RecyclerView.Adapter<ViewTransactionAdapter.ViewTransactionViewHolder> {

    private final Context context;
    private final List<TransactionEntity> transactionList;
    private ItemClickListener itemClickListener;
    private final List<UsersEntity> userList;
    private int mode;

    public ViewTransactionAdapter(Context context, List<TransactionEntity> transactionList, int mode , List<UsersEntity> userList) {
        this.context = context;
        this.transactionList = transactionList;
        this.userList = userList;
        this.mode = mode;
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ViewTransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTransactionBinding binding = ItemTransactionBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewTransactionViewHolder(binding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewTransactionViewHolder holder, int position) {

        TransactionEntity entity = transactionList.get(position);

        UsersEntity user = getUserById(entity.getUserId());
        if(user == null){
            holder.binding.tvName.setText("Unknown User");
            holder.binding.tvNumber.setText("");
        } else {
            holder.binding.tvName.setText(user.getName());
            holder.binding.tvNumber.setText(user.getNumber());
        }
        holder.binding.etTransaction.setText(String.valueOf(entity.getTransaction()));

        String date = entity.getDate();
        String[] dateArr = date.split("-");
        holder.binding.tvDate.setText(dateArr[0] + " " + DateFormat.getMonthNameByDigit(Integer.parseInt(dateArr[1])) + ", " + dateArr[2]);

        holder.binding.btnSave.setOnClickListener(view -> {
            String transaction = holder.binding.etTransaction.getText().toString().trim();
            if (TextUtils.isEmpty(transaction))
                Toast.makeText(context, "Enter transaction", Toast.LENGTH_SHORT).show();
            else {
                entity.setTransaction(Double.parseDouble(transaction));
                itemClickListener.onClickSave(entity, holder.getAdapterPosition());
            }
        });

        if (entity.isEditable()) {
            holder.binding.etTransaction.setEnabled(true);
            holder.binding.etTransaction.setFocusable(true);
        } else {
            holder.binding.etTransaction.setEnabled(false);
            holder.binding.etTransaction.setFocusable(false);
        }

        //View Color changes according to mode
        ColorStateList colorStateList ;
        if(mode == Constants.LENT_MODE){
            colorStateList = ContextCompat.getColorStateList(context,R.color.green);
        } else {
            colorStateList = ContextCompat.getColorStateList(context,R.color.red);
        }
        holder.binding.btnSave.setBackgroundTintList(colorStateList);
        holder.binding.etTransaction.setTextColor(colorStateList);
        TextViewCompat.setCompoundDrawableTintList(holder.binding.etTransaction , colorStateList);


        holder.binding.imgEdit.setOnClickListener(view -> {
            entity.setEditable(true);
            notifyItemChanged(holder.getAdapterPosition());
        });

        holder.binding.imgCall.setOnClickListener(view -> {
            if(user!= null)
                itemClickListener.onClickCall(user.getNumber());
            else
                Toast.makeText(context, "Unknown User, Number not found !", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    public void deleteTransaction(int position) {
        try {
            transactionList.remove(position);
            notifyItemRemoved(position);
        } catch (Exception e) {
            System.err.println("EXCEPTION FOUND ===========>");
        }
    }

    public void lockTransaction(int position){
        TransactionEntity entity = transactionList.get(position);
        entity.setEditable(false);
        notifyItemChanged(position);
    }

    public UsersEntity getUserById(int id){
        for(UsersEntity entity : userList){
            if(entity.getId() == id)
                return entity;
        }
        return null;
    }

    public static class ViewTransactionViewHolder extends RecyclerView.ViewHolder {

        private final ItemTransactionBinding binding;

        public ViewTransactionViewHolder(@NonNull ItemTransactionBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public interface ItemClickListener {
        void onClickSave(TransactionEntity entity, int position);

        void onClickCall(String number);
    }
}
