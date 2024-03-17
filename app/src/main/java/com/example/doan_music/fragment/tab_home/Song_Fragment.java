package com.example.doan_music.fragment.tab_home;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan_music.R;
import com.example.doan_music.adapter.home.FavoriteSongAdapter;
import com.example.doan_music.data.DbHelper;
import com.example.doan_music.m_interface.OnItemClickListener;
import com.example.doan_music.model.Song;
import com.example.doan_music.music.PlayMusicActivity;

import java.util.ArrayList;
import java.util.List;

public class Song_Fragment extends Fragment {
    RecyclerView rcv_lovesong;
    List<Song> songList;
    ArrayList<Integer> arr = new ArrayList<>();

    FavoriteSongAdapter favoriteSongAdapter;

    DbHelper dbHelper;
    SQLiteDatabase database = null;

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_song_, container, false);
        addControls();

        createData();

        favoriteSongAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(String data) {
                dbHelper = new DbHelper(requireContext());
                database = requireContext().openOrCreateDatabase("doanmusic.db", MODE_PRIVATE, null);
                Cursor cursor = database.rawQuery("select * from Songs", null);
                while (cursor.moveToNext()) {
                    int id = cursor.getInt(0);
                    String songName = cursor.getString(2);

                    if (data.equals(songName)) {
                        Intent intent = new Intent(requireContext(), PlayMusicActivity.class);
                        intent.putExtra("SongID", id);
                        intent.putExtra("arrIDSongs", arr);

                        startActivity(intent);
                        break;
                    }
                }
                cursor.close();
            }
        });

        return view;
    }

    private void createData() {
        dbHelper = new DbHelper(requireContext());
        database = dbHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery("select * from Songs", null);
//        songList.clear();

        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String songName = cursor.getString(2);
            byte[] image = cursor.getBlob(3);
            String linkSong = cursor.getString(5);
            int favorite = cursor.getInt(6);

            if (favorite == 1) {
                Song song = new Song(id, songName, image, linkSong, favorite);
                songList.add(song);
                arr.add(id);
            }
        }
        favoriteSongAdapter.notifyDataSetChanged();
        cursor.close();
    }

    private void addControls() {
        rcv_lovesong = view.findViewById(R.id.rcv_lovesong);
        songList = new ArrayList<>();

        favoriteSongAdapter = new FavoriteSongAdapter(requireContext(), songList);

        rcv_lovesong.setAdapter(favoriteSongAdapter);
    }
}