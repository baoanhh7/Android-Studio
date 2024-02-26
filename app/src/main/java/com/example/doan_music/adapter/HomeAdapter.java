package com.example.doan_music.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan_music.R;
import com.example.doan_music.model.User;

import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int type_header = 0;
    private static final int type_bottom = 1;

    private List<User> listUser;

    public void setData(List<User> list) {
        this.listUser = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (type_header == viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_header, parent, false);
            return new UserHeaderViewHolder(view);
        } else if (type_bottom == viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_bottom, parent, false);
            return new UserBottomViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        User user = listUser.get(position);

        if (type_header == holder.getItemViewType()) {
            UserHeaderViewHolder userHeaderViewHolder = (UserHeaderViewHolder) holder;
            userHeaderViewHolder.img_home_header.setImageResource(user.getResourceImage());
            userHeaderViewHolder.txt_home_header.setText(user.getName());
        } else if (type_bottom == holder.getItemViewType()) {
            UserBottomViewHolder userBottomViewHolder = (UserBottomViewHolder) holder;
            userBottomViewHolder.img_home_bottom.setImageResource(user.getResourceImage());
            userBottomViewHolder.txt_home_bottom.setText(user.getName());
        }
    }

    @Override
    public int getItemCount() {
        if (listUser != null) return listUser.size();
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        User user = listUser.get(position);
        if (user.getHeader()) return type_header;
        else return type_bottom;
    }

    public class UserHeaderViewHolder extends RecyclerView.ViewHolder {
        private ImageView img_home_header;
        private TextView txt_home_header;

        public UserHeaderViewHolder(@NonNull View itemView) {
            super(itemView);

            img_home_header = itemView.findViewById(R.id.img_home_header);
            txt_home_header = itemView.findViewById(R.id.txt_home_header);
        }
    }

    public class UserBottomViewHolder extends RecyclerView.ViewHolder {
        private ImageView img_home_bottom;
        private TextView txt_home_bottom;

        public UserBottomViewHolder(@NonNull View itemView) {
            super(itemView);

            img_home_bottom = itemView.findViewById(R.id.img_home_bottom);
            txt_home_bottom = itemView.findViewById(R.id.txt_home_bottom);
        }
    }
}
