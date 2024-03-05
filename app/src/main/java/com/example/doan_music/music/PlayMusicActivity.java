package com.example.doan_music.music;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.doan_music.R;

public class PlayMusicActivity extends AppCompatActivity {

    ImageButton btn_play, btn_pause, btn_back;
    Boolean flag = true;
    SeekBar seekBar;
    TextView txt_time, txt_time_first;

    MediaPlayer myMusic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_music);

        addControls();

        myMusic = MediaPlayer.create(this, R.raw.nhung_loi_hua_bo_quen);
        myMusic.setLooping(true);
        myMusic.seekTo(0);

        String duration = timeSeekbar(myMusic.getDuration());
        txt_time.setText(duration);

        addEvents();
    }

    private String timeSeekbar(int time) {
        String mTime = "";
        int minutes = time / 1000 / 60;
        int seconds = time / 1000 % 60;
        mTime = minutes + ":";
        if (seconds < 10) {
            mTime += "0";
        }
        mTime += seconds;
        return mTime;

    }

    private void addEvents() {
        btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Khai báo Intent công khai để khởi động Service
                Intent i = new Intent(PlayMusicActivity.this, MyService_Music.class);
                startService(i);

                if (flag) {
                    // Chuyển hình ảnh play sang pause
                    btn_play.setImageResource(R.drawable.ic_pause);
                    flag = false;
                } else {
                    // Và ngược lại
                    btn_play.setImageResource(R.drawable.ic_play);
                    flag = true;
                }
            }
        });

//        btn_pause.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Khai báo Intent công khai để khởi động Service
//                Intent intent2 = new Intent(PlayMusicActivity.this, MyService_Music.class);
//                stopService(intent2);
//                // Nhấn vào Stop chuyển ảnh của play sang pause
//                btn_play.setImageResource(R.drawable.ic_play);
//                flag = true;
//            }
//        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        seekBar.setMax(myMusic.getDuration());
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    myMusic.seekTo(progress);
                    seekBar.setProgress(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (myMusic != null) {
                    if (myMusic.isPlaying()) {
                        try {
                            final double current = myMusic.getCurrentPosition();
                            final String time = timeSeekbar((int) current);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    txt_time_first.setText(time);
                                    seekBar.setProgress((int) current);
                                }
                            });
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                        }
                    }
                }
            }
        }).start();
    }

    private void addControls() {
        btn_play = findViewById(R.id.btn_play);
//        btn_pause = findViewById(R.id.btn_pause);
        btn_back = findViewById(R.id.btn_back);

        seekBar = findViewById(R.id.seekBar);

        txt_time = findViewById(R.id.txt_time);
        txt_time_first = findViewById(R.id.txt_time_first);
    }
}