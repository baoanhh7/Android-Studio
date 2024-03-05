package com.example.doan_music.fragment.tab_home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.example.doan_music.PlayMusicActivity;
import com.example.doan_music.R;

public class Song_Fragment extends Fragment {

    LinearLayout linear_nhungloihuaboquen;
    View mView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_song_, container, false);

        linear_nhungloihuaboquen = mView.findViewById(R.id.linear_nhungloihuaboquen);

        linear_nhungloihuaboquen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), PlayMusicActivity.class);
                startActivity(i);
            }
        });
        return mView;
    }
}