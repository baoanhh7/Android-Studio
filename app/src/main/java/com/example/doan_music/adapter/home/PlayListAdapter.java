package com.example.doan_music.adapter.home;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan_music.R;
import com.example.doan_music.model.Playlists;

import java.util.List;

public class PlayListAdapter extends RecyclerView.Adapter<PlayListAdapter.PlayListViewHolder> {

    List<Playlists> list;

    public void setData(List<Playlists> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PlayListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_playlist, parent, false);
        return new PlayListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayListViewHolder holder, int position) {

        Playlists playlists = list.get(position);
        holder.txt_id.setText(playlists.getPlaylistID() + "");
        holder.txt_name.setText(playlists.getPlaylistName());

        Bitmap bitmap = BitmapFactory.decodeByteArray(playlists.getPlaylistImage(), 0, playlists.getPlaylistImage().length);
        holder.img_playlist.setImageBitmap(bitmap);

    }

    @Override
    public int getItemCount() {
        if (list != null) return list.size();
        return 0;
    }

    public class PlayListViewHolder extends RecyclerView.ViewHolder {

        ImageView img_playlist;
        TextView txt_id, txt_name;

        public PlayListViewHolder(@NonNull View itemView) {
            super(itemView);

            img_playlist = itemView.findViewById(R.id.img_playlist);
            txt_name = itemView.findViewById(R.id.txt_name);
            txt_id = itemView.findViewById(R.id.txt_id);
        }
    }
}
