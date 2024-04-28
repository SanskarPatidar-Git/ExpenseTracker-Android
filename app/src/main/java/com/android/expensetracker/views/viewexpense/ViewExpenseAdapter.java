package com.android.expensetracker.views.viewexpense;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.expensetracker.databinding.ItemExpensesBinding;
import com.android.expensetracker.utility.DateFormat;
import com.android.expensetracker.views.addexpense.ExpenseEntity;

import java.util.List;

public class ViewExpenseAdapter extends RecyclerView.Adapter<ViewExpenseAdapter.ViewExpenseViewHolder> {

    private Context context;
    private List<ExpenseEntity> expenseList;
    private ItemClickListener itemClickListener;

    public ViewExpenseAdapter(Context context, List<ExpenseEntity> expenseList) {
        this.context = context;
        this.expenseList = expenseList;
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ViewExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemExpensesBinding binding = ItemExpensesBinding.inflate(LayoutInflater.from(context) , parent , false);
        return new ViewExpenseViewHolder(binding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewExpenseViewHolder holder, int position) {
        ExpenseEntity entity = expenseList.get(position);

        holder.binding.tvExpenseOf.setText(entity.getExpenseOf());
        holder.binding.tvExpenseBy.setText(entity.getExpenseBy());

        if(TextUtils.isEmpty(entity.getDescription()))
            holder.binding.tvDescription.setVisibility(View.GONE);
        else
            holder.binding.tvDescription.setText(entity.getDescription());
        holder.binding.tvExpense.setText(String.valueOf(entity.getExpense()));

        String date = entity.getDate();
        String[] dateArr = date.split("-");
        holder.binding.tvDate.setText(dateArr[0] + " "+ DateFormat.getMonthNameByDigit(Integer.parseInt(dateArr[1])) + ", " + dateArr[2]);

        holder.binding.btnRemoveExpense.setOnClickListener(view -> {
            itemClickListener.onClickRemove(entity.getId() , holder.getAdapterPosition());
        });
    }

    @Override
    public int getItemCount() {
        return expenseList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void deleteExpense(int position){
        System.out.println("Position : "+position);
        try{
            expenseList.remove(position);
            notifyItemRemoved(position);
        } catch (Exception e){
            System.err.println("Exception Occurred");
            notifyDataSetChanged();
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void deleteAllExpenses(){
        expenseList.clear();
        ((ViewExpenseActivity)context).isExpenseFound = false;
        notifyDataSetChanged();
    }

    public List<ExpenseEntity> getExpenseList(){
        return expenseList;
    }

    public static class ViewExpenseViewHolder extends RecyclerView.ViewHolder {

        private final ItemExpensesBinding binding;
        public ViewExpenseViewHolder(@NonNull ItemExpensesBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public interface ItemClickListener {
        void onClickRemove(int id , int position);
    }
}
