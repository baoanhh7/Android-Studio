package com.example.doan_music.fragment.tab_home;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan_music.R;
import com.example.doan_music.activity.home.PlayListActivity;
import com.example.doan_music.activity.home.SongsAlbumActivity;
import com.example.doan_music.adapter.home.CategoryAdapter;
import com.example.doan_music.adapter.home.HomeAdapter;
import com.example.doan_music.data.DatabaseManager;
import com.example.doan_music.data.DbHelper;
import com.example.doan_music.m_interface.IClickItemCategory;
import com.example.doan_music.m_interface.OnItemClickListener;
import com.example.doan_music.model.Album;
import com.example.doan_music.model.Category;
import com.example.doan_music.model.Playlists;

import java.util.ArrayList;
import java.util.List;

public class All_Fragment extends Fragment {
    private RecyclerView rcv_all_header, rcv_all_bottom;
    private HomeAdapter allAdapter_header;
    private CategoryAdapter allCateAdapter_bottom;
    DbHelper dbHelper;
    SQLiteDatabase database = null;
    View view;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_all_, container, false);

        addControls();

        // set layout của recyclerView thành 2 cột
        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(), 2);
        rcv_all_header.setLayoutManager(gridLayoutManager);

        // set layout của recyclerView theo hướng ngang
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false);
        rcv_all_bottom.setLayoutManager(linearLayoutManager);

        // set data cho recyclerView
        allCateAdapter_bottom.setData(getlistuserBottom());

        addEvents();

        return view;
    }

    private void addEvents() {
        allAdapter_header.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(String data) {
                dbHelper = DatabaseManager.dbHelper(requireContext());
                database = dbHelper.getReadableDatabase();

                Cursor cursor = database.rawQuery("select * from Albums", null);
                while (cursor.moveToNext()) {
                    int id = cursor.getInt(0);
                    String name = cursor.getString(1);
                    if (name.equals(data)) {
                        Intent intent = new Intent(requireContext(), SongsAlbumActivity.class);
                        intent.putExtra("albumID", id);
                        startActivity(intent);
                        break;
                    }
                }
            }
        });
    }

    @NonNull
    private List<Album> getlistuserHeader() {
        List<Album> list = new ArrayList<>();

        dbHelper = DatabaseManager.dbHelper(requireContext());
        database = dbHelper.getReadableDatabase();

        list.clear();
        Cursor cursor = database.rawQuery("select * from Albums", null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String ten = cursor.getString(1);
            byte[] anh = cursor.getBlob(2);
            int idArtist = cursor.getInt(3);

            list.add(new Album(id, ten, anh, idArtist));
        }
        cursor.close();
        database.close();

        return list;
    }

    @NonNull
    private List<Category> getlistuserBottom() {

        // list 0
        List<Playlists> list = new ArrayList<>();

        dbHelper = DatabaseManager.dbHelper(requireContext());
        database = dbHelper.getReadableDatabase();

        Cursor cursor = database.rawQuery("Select * from Playlists", null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            byte[] img = cursor.getBlob(2);

            Playlists playlists = new Playlists(id, name, img);
            list.add(playlists);
        }
        cursor.close();

        // list 1
        List<Playlists> list1 = new ArrayList<>();

        dbHelper = DatabaseManager.dbHelper(requireContext());
        database = dbHelper.getReadableDatabase();

        Cursor cursor1 = database.rawQuery("Select * from Playlists", null);
        while (cursor1.moveToNext()) {
            int id = cursor1.getInt(0);
            String name = cursor1.getString(1);
            byte[] img = cursor1.getBlob(2);

            Playlists playlists = new Playlists(id, name, img);
            list1.add(playlists);
        }
        cursor1.close();

        // list 2
        List<Playlists> list2 = new ArrayList<>();

        dbHelper = DatabaseManager.dbHelper(requireContext());
        database = dbHelper.getReadableDatabase();

        Cursor cursor2 = database.rawQuery("Select * from Playlists", null);
        while (cursor2.moveToNext()) {
            int id = cursor2.getInt(0);
            String name = cursor2.getString(1);
            byte[] img = cursor2.getBlob(2);

            Playlists playlists = new Playlists(id, name, img);
            list1.add(playlists);
        }
        cursor2.close();

        // list 3
        List<Playlists> list3 = new ArrayList<>();

        dbHelper = DatabaseManager.dbHelper(requireContext());
        database = dbHelper.getReadableDatabase();

        Cursor cursor3 = database.rawQuery("Select * from Playlists", null);
        while (cursor3.moveToNext()) {
            int id = cursor3.getInt(0);
            String name = cursor3.getString(1);
            byte[] img = cursor3.getBlob(2);

            Playlists playlists = new Playlists(id, name, img);
            list1.add(playlists);
        }
        cursor3.close();

        List<Category> categoryList = new ArrayList<>();
        categoryList.add(new Category("Danh sách phát dành cho bạn", list));
        categoryList.add(new Category("Bài hát được yêu thích", list1));
        categoryList.add(new Category("Lựa chọn của Spotify", list2));
        categoryList.add(new Category("Mới nghe gần đây", list3));

        return categoryList;
    }

    private void addControls() {
        rcv_all_header = view.findViewById(R.id.rcv_all_header);
        rcv_all_bottom = view.findViewById(R.id.rcv_all_bottom);

        allAdapter_header = new HomeAdapter(requireContext(), getlistuserHeader());

        allCateAdapter_bottom = new CategoryAdapter(new IClickItemCategory() {
            @Override
            public void onClickItemCategory(Category category) {
                Intent i = new Intent(requireContext(), PlayListActivity.class);
                i.putExtra("c", category.getName());
                startActivity(i);
            }
        });

        rcv_all_header.setAdapter(allAdapter_header);
        rcv_all_bottom.setAdapter(allCateAdapter_bottom);
    }
}