package com.example.doan_music.music;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.doan_music.R;
import com.example.doan_music.data.DbHelper;
import com.example.doan_music.model.LrcLine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlayMusicActivity extends AppCompatActivity {

    ImageButton btn_play, btn_back, btn_next, btn_pre, btn_toggle, btn_shuffle,btn_volume, btn_heart;
    SeekBar seekBar,seekbar1;
    TextView txt_time, txt_time_first;
    MediaPlayer myMusic;
    AudioManager audioManager;
    ArrayList<Integer> arr = new ArrayList<>();
    ArrayList<Integer> shuffle = new ArrayList<>();
    ImageView imageView_songs;
    TextView txt_artist_song, txt_name_song;
    Integer currentPosition;

    boolean Isshuffle = true;
    private boolean frag = true;
    private boolean frag_heart = true;
    SQLiteDatabase database = null;
    DbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_music);

        addControls();
        arr = (ArrayList<Integer>) getIntent().getSerializableExtra("arrIDSongs");
        Integer IDSong = getIntent().getIntExtra("SongID", -1);
        shuffle = arr;
        currentPosition = arr.indexOf(IDSong);
        myMusic = new MediaPlayer();
        //myMusic = MediaPlayer.create(this, R.raw.nhung_loi_hua_bo_quen);
        loadData();
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        myMusic.seekTo(0);

        myMusic.start();
        myMusic.setLooping(false);
        seekbar1.setVisibility(View.GONE);
        // tạo biến duration để lưu thời gian bài hát
        String duration = timeSeekbar(myMusic.getDuration());
        txt_time.setText(duration);
        loadNameArtist();
        addEvents();
        volume();
        // Bắt đầu cập nhật lời bài hát
        if (frag) {
            myMusic.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    playNextSong(arr);
                }
            });
        }

        SharedPreferences sharedPreferences = getSharedPreferences("stateHeart", MODE_PRIVATE);
        frag_heart = sharedPreferences.getBoolean("is_favorite", false);
        updateHeartButton();
    }

    private void volume() {
        int maxV= audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int curV = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        seekbar1.setMax(maxV);
        seekbar1.setProgress(curV);
        seekbar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,progress,0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void updateHeartButton() {
        if (frag_heart) {
            btn_heart.setImageResource(R.drawable.ic_red_heart);
        } else {
            btn_heart.setImageResource(R.drawable.ic_heart);
        }
    }

    private void loadNameArtist() {
        Integer IDSong = getIntent().getIntExtra("SongID", -1);
        database = openOrCreateDatabase("doanmusic.db", MODE_PRIVATE, null);
        Cursor cursor = database.rawQuery("select * " +
                "from Artists " +
                "JOIN Songs ON Artists.ArtistID =Songs.ArtistID " +
                "WHERE Songs.SongID = ? ", new String[]{String.valueOf(IDSong)});
        while (cursor.moveToNext()) {
            String ten = cursor.getString(1);
            txt_artist_song.setText(ten);
        }

    }

    private void loadData() {
        Integer IDSong = getIntent().getIntExtra("SongID", -1);
        database = openOrCreateDatabase("doanmusic.db", MODE_PRIVATE, null);
        Cursor cursor = database.rawQuery("select * from Songs", null);
        while (cursor.moveToNext()) {
            Integer Id = cursor.getInt(0);
            String ten = cursor.getString(2);
            byte[] img = cursor.getBlob(3);
            String linkSong = cursor.getString(5);
            if (IDSong.equals(Id)) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(img, 0, img.length);
                imageView_songs.setImageBitmap(bitmap);
                try {
                    myMusic.setDataSource(linkSong);
                    myMusic.prepare();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                txt_name_song.setText(ten);
            }
        }
        cursor.close();
    }

    public String timeSeekbar(int time) {
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
        btn_volume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int visibility = seekbar1.getVisibility();
                if(visibility == View.GONE)
                {
                    seekbar1.setVisibility(View.VISIBLE);
                }
                else
                    seekbar1.setVisibility(View.GONE);
            }
        });
        btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myMusic.isPlaying()) {
                    myMusic.pause();
                    btn_play.setImageResource(R.drawable.ic_play);

                } else {
                    myMusic.start();
                    btn_play.setImageResource(R.drawable.ic_pause);

                    // Load animation từ file xml
                    Animation animation = AnimationUtils.loadAnimation(PlayMusicActivity.this, R.anim.animation);
                    // Áp dụng animation vào ImageView
                    imageView_songs.startAnimation(animation);
                }
            }
        });
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myMusic != null) {
                    btn_play.setImageResource(R.drawable.ic_pause);
                }
                if (currentPosition < arr.size() - 1) {
                    currentPosition++;
                } else {
                    currentPosition = 0;
                }
                if (myMusic.isPlaying()) {
                    myMusic.stop();
                    myMusic.reset();
                }
                Integer idSong = arr.get(currentPosition);
                database = openOrCreateDatabase("doanmusic.db", MODE_PRIVATE, null);
                Cursor cursor = database.rawQuery("select * from Songs", null);
                while (cursor.moveToNext()) {
                    Integer Id = cursor.getInt(0);
                    String ten = cursor.getString(2);
                    byte[] img = cursor.getBlob(3);
                    String linkSong = cursor.getString(5);
                    if (idSong.equals(Id)) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(img, 0, img.length);
                        imageView_songs.setImageBitmap(bitmap);
                        try {
                            myMusic.setDataSource(linkSong);
                            myMusic.prepare();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        txt_name_song.setText(ten);
                    }

                    int farovite = cursor.getInt(6);
                    setFavorite(farovite);

                }
                cursor.close();
                database = openOrCreateDatabase("doanmusic.db", MODE_PRIVATE, null);
                Cursor cursor1 = database.rawQuery("select * " +
                        "from Artists " +
                        "JOIN Songs ON Artists.ArtistID =Songs.ArtistID " +
                        "WHERE Songs.SongID = ? ", new String[]{String.valueOf(idSong)});
                while (cursor1.moveToNext()) {
                    String ten = cursor1.getString(1);
                    txt_artist_song.setText(ten);
                }

                String duration = timeSeekbar(myMusic.getDuration());
                txt_time.setText(duration);
                seekBar.setMax(myMusic.getDuration());
                myMusic.start();

            }
        });
        btn_pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myMusic != null) {
                    btn_play.setImageResource(R.drawable.ic_pause);
                }
                if (currentPosition > 0) {
                    currentPosition--;
                } else {
                    currentPosition = arr.size() - 1;
                }
                if (myMusic.isPlaying()) {
                    myMusic.stop();
                    myMusic.reset();
                }
                Integer idSong = arr.get(currentPosition);
                database = openOrCreateDatabase("doanmusic.db", MODE_PRIVATE, null);
                Cursor cursor = database.rawQuery("select * from Songs", null);
                while (cursor.moveToNext()) {
                    Integer Id = cursor.getInt(0);
                    String ten = cursor.getString(2);
                    byte[] img = cursor.getBlob(3);
                    String linkSong = cursor.getString(5);

                    if (idSong.equals(Id)) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(img, 0, img.length);
                        imageView_songs.setImageBitmap(bitmap);
                        try {
                            myMusic.setDataSource(linkSong);
                            myMusic.prepare();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        txt_name_song.setText(ten);
                    }

                    int farovite = cursor.getInt(6);
                    setFavorite(farovite);

                }
                cursor.close();

                database = openOrCreateDatabase("doanmusic.db", MODE_PRIVATE, null);
                Cursor cursor1 = database.rawQuery("select * " +
                        "from Artists " +
                        "JOIN Songs ON Artists.ArtistID =Songs.ArtistID " +
                        "WHERE Songs.SongID = ? ", new String[]{String.valueOf(idSong)});
                while (cursor1.moveToNext()) {
                    String ten = cursor1.getString(1);
                    txt_artist_song.setText(ten);
                    break;

                }

                String duration = timeSeekbar(myMusic.getDuration());
                txt_time.setText(duration);
                seekBar.setMax(myMusic.getDuration());
                myMusic.start();
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myMusic.isPlaying()) {
                    myMusic.stop();
                    myMusic.reset();
                }
                finish();
            }
        });

        btn_heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (frag_heart) {
                    frag_heart = false;
                } else {
                    frag_heart = true;
                }
                // sau khi set trạng thái thì sẽ return ngược của trạng thái đó, thì bạn sẽ đảo ngược lại để
                // lấy trạng thái ban đầu để set true = 1, false = 0
                boolean stateFavorite = !frag_heart;

                Integer IDSong = getIntent().getIntExtra("SongID", -1);
                database = openOrCreateDatabase("doanmusic.db", MODE_PRIVATE, null);

                ContentValues values = new ContentValues();

                // Chuyển đổi boolean thành integer (1 hoặc 0)
                values.put("StateFavorite", stateFavorite ? 1 : 0);
                int kq = database.update("Songs", values, "SongID = ?", new String[]{String.valueOf(IDSong)});

                if (kq > 0) {
                    addSongToLoveList(IDSong);
                } else {
                    removeSongFromLoveList(IDSong);
                }
                // Cập nhật biến frag_heart
                frag_heart = !stateFavorite;

                // Lưu trạng thái mới vào SharedPreferences
                SharedPreferences sharedPreferences = getSharedPreferences("stateHeart", MODE_PRIVATE);
                sharedPreferences.edit().putBoolean("is_favorite", frag_heart).apply();
                updateHeartButton();
            }
        });

        btn_toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (frag) {

                    // Thực hiện các hành động khi nút được bật
                    btn_toggle.setImageResource(R.drawable.ic_repeatactive);
                    myMusic.setLooping(true);
                    frag = false;
                } else {
                    btn_toggle.setImageResource(R.drawable.ic_repeat);
                    myMusic.setLooping(false);
                    frag = true;
                }
            }
        });
        btn_shuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Isshuffle) {
                    btn_shuffle.setImageResource(R.drawable.ic_shufferactive);
                    Isshuffle = false;
                    Collections.shuffle(arr);
                } else {
                    btn_shuffle.setImageResource(R.drawable.ic_shuffer);
                    arr = shuffle;
                    Isshuffle = true;
                }
            }
        });

        // set giới hạn Max cho thanh seekBar
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

    private void removeSongFromLoveList(Integer songid) {


        SharedPreferences preferences = getSharedPreferences("data", Context.MODE_PRIVATE);
        int maU = preferences.getInt("maU1", -1);

        // Mở hoặc tạo cơ sở dữ liệu
        SQLiteDatabase database = openOrCreateDatabase("doanmusic.db", MODE_PRIVATE, null);
        database.beginTransaction();

        try {
            // Xóa bài hát khỏi bảng User_SongLove dựa trên UserID và SongID
            int kq = database.delete("User_SongLove", "UserID = ? AND SongID = ?", new String[]{String.valueOf(maU), String.valueOf(songid)});

            if (kq > 0) {
                // Cập nhật trạng thái yêu thích của bài hát trong bảng Songs
                ContentValues values = new ContentValues();
                values.put("Favorite", 0); // Đặt trạng thái yêu thích thành không yêu thích (0)

                // Cập nhật bảng Songs dựa trên SongID
                database.update("Songs", values, "SongID = ?", new String[]{String.valueOf(songid)});
            }

            // Đánh dấu giao dịch thành công nếu không có lỗi xảy ra
            database.setTransactionSuccessful();
        } finally {
            // Kết thúc giao dịch, đảm bảo rằng dữ liệu được lưu trữ an toàn
            database.endTransaction();
        }
    }

    private void addSongToLoveList(Integer songid) {
        SharedPreferences preferences = getSharedPreferences("data", Context.MODE_PRIVATE);
        int maU = preferences.getInt("maU1", -1);

        database = openOrCreateDatabase("doanmusic.db", MODE_PRIVATE, null);
        database.beginTransaction();

        try {
            // Lặp qua các bản ghi trong bảng Songs
            Cursor cursor = database.rawQuery("SELECT * FROM Songs", null);
            while (cursor.moveToNext()) {
                int favorite = cursor.getInt(6);

                // Xây dựng ContentValues mới
                ContentValues values = new ContentValues();

                // Chuyển đổi trạng thái yêu thích của bài hát
                if (favorite == 1) {
                    favorite = 0;
                } else {
                    favorite = 1;
                    // Đặt giá trị cho cột Favorite
                    values.put("Favorite", favorite);
                    // Đặt giá trị cho cột UserID
                    values.put("UserID", maU);
                    // Đặt giá trị cho cột SongID
                    values.put("SongID", songid);

                    // Thực hiện thêm dòng vào bảng User_SongLove
                    long kq = database.insert("User_SongLove", null, values);
                    if (kq > 0) {
                        break;
                    }
                }

            }
            cursor.close();

            // Đánh dấu giao dịch thành công nếu không có lỗi xảy ra
            database.setTransactionSuccessful();
        } finally {
            // Kết thúc giao dịch, đảm bảo rằng dữ liệu được lưu trữ an toàn
            database.endTransaction();
        }
    }

    private void setFavorite(int farovite) {
        if (farovite == 1) {
            btn_heart.setImageResource(R.drawable.ic_red_heart);
        } else {
            btn_heart.setImageResource(R.drawable.ic_heart);
        }
    }

    private void playNextSong(@NonNull ArrayList<Integer> arr) {
        myMusic.reset();
        if (currentPosition < arr.size() - 1) {
            currentPosition++;
        } else {
            currentPosition = 0;
        }
        Integer idSong = arr.get(currentPosition);
        database = openOrCreateDatabase("doanmusic.db", MODE_PRIVATE, null);
        Cursor cursor = database.rawQuery("select * from Songs", null);
        while (cursor.moveToNext()) {
            Integer Id = cursor.getInt(0);
            String ten = cursor.getString(2);
            byte[] img = cursor.getBlob(3);
            String linkSong = cursor.getString(5);
            if (idSong.equals(Id)) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(img, 0, img.length);
                imageView_songs.setImageBitmap(bitmap);
                try {
                    myMusic.reset();
                    myMusic.setDataSource(linkSong);
                    myMusic.prepare();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                txt_name_song.setText(ten);
            }
        }
        cursor.close();
        database = openOrCreateDatabase("doanmusic.db", MODE_PRIVATE, null);
        Cursor cursor1 = database.rawQuery("select * " +
                "from Artists " +
                "JOIN Songs ON Artists.ArtistID =Songs.ArtistID " +
                "WHERE Songs.SongID = ? ", new String[]{String.valueOf(idSong)});
        while (cursor1.moveToNext()) {
            String ten = cursor1.getString(1);
            txt_artist_song.setText(ten);
        }

        String duration = timeSeekbar(myMusic.getDuration());
        txt_time.setText(duration);
        seekBar.setMax(myMusic.getDuration());
        myMusic.start();
    }

    private void addControls() {
        btn_play = findViewById(R.id.btn_play);
        btn_back = findViewById(R.id.btn_back);
        txt_artist_song = findViewById(R.id.txt_artist_song);
        txt_name_song = findViewById(R.id.txt_name_song);
        seekBar = findViewById(R.id.seekBar);
        imageView_songs = findViewById(R.id.imageView_songs);
        txt_time = findViewById(R.id.txt_time);
        txt_time_first = findViewById(R.id.txt_time_first);
        btn_pre = findViewById(R.id.btn_pre);
        btn_next = findViewById(R.id.btn_next);
        btn_volume = findViewById(R.id.btn_volume);
        seekbar1 = findViewById(R.id.seekBar_volume);
        btn_toggle = findViewById(R.id.btn_toggle);
        btn_shuffle = findViewById(R.id.btn_shuffle);
        btn_heart = findViewById(R.id.btn_heart);

    }
}