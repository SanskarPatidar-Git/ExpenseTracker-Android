package com.android.expensetracker.views.users;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.expensetracker.databinding.ItemUsersBinding;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UsersViewHolder> {

    private Context context;
    private List<UsersEntity> usersList;
    private ItemClickListener itemClickListener;


    public UsersAdapter(Context context, List<UsersEntity> usersList) {
        this.context = context;
        this.usersList = usersList;
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemUsersBinding binding = ItemUsersBinding.inflate(LayoutInflater.from(context),parent,false);
        return new UsersViewHolder(binding);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(@NonNull UsersViewHolder holder, int position) {
        UsersEntity entity = usersList.get(position);
        holder.binding.tvName.setText(entity.getName());
        holder.binding.tvNumber.setText(entity.getNumber());

        holder.binding.imgRemove.setOnClickListener(view -> {
            usersList.remove(position);
            itemClickListener.onClickRemove(entity.getId());
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public static class UsersViewHolder extends RecyclerView.ViewHolder {

        private final ItemUsersBinding binding;
        public UsersViewHolder(@NonNull ItemUsersBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public interface ItemClickListener {
        void onClickRemove(int id);
    }
}
