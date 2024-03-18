package com.example.doan_music.activity.admin.album;

import static android.provider.MediaStore.ACTION_IMAGE_CAPTURE;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.doan_music.R;
import com.example.doan_music.data.DatabaseManager;
import com.example.doan_music.data.DbHelper;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class AddAlbumActivity extends AppCompatActivity {

    EditText edtMa, edtTen, edtMaArtist;
    DbHelper dbHelper;
    Button btnSave, btncancel, btn_choose_image_addAblumAdmin;
    ImageButton btn_camera;
    SQLiteDatabase database = null;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_album);
        addControls();
        addEvents();
    }

    private void addEvents() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                byte[] anh = getByteArrayFromImageView(imageView);
                String edtMaArtist1 = edtMaArtist.getText().toString();
                database = openOrCreateDatabase("doanmusic.db", MODE_PRIVATE, null);
                Cursor cursor = database.rawQuery("select * from Artists", null);
                while (cursor.moveToNext()) {
                    Integer maArtist = Integer.valueOf(cursor.getString(0) + "");
                    if (Integer.valueOf(edtMaArtist1) == maArtist) {
                        ContentValues values = new ContentValues();
                        values.put("AlbumID", edtMa.getText().toString() + "");
                        values.put("AlbumName", edtTen.getText().toString());
                        values.put("Ablum_ArtistID", edtMaArtist.getText().toString() + "");
                        values.put("AlbumImage", anh);
                        dbHelper = DatabaseManager.dbHelper(AddAlbumActivity.this);
                        long kq = dbHelper.getReadableDatabase().insert("Albums", null, values);
                        if (kq > 0) {
                            Toast.makeText(AddAlbumActivity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                            cursor.close();
                            finish();
                        }
                        else
                            Toast.makeText(AddAlbumActivity.this, "Thêm thất bại", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
                Toast.makeText(AddAlbumActivity.this, "Không có Artist tương ứng", Toast.LENGTH_SHORT).show();
            }
        });
        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtMa.setText("");
                edtTen.setText("");
                edtMaArtist.setText("");
                startActivity(new Intent(AddAlbumActivity.this, AlbumActivity.class));
            }
        });
        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera();
            }
        });
        btn_choose_image_addAblumAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePhoto();
            }
        });
    }

    private byte[] getByteArrayFromImageView(ImageView img) {
        BitmapDrawable drawable = (BitmapDrawable) img.getDrawable();
        Bitmap bitmap = drawable.getBitmap();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    private void openCamera() {
        Intent takePictureIntent = new Intent(ACTION_IMAGE_CAPTURE);
        if (ActivityCompat.checkSelfPermission(AddAlbumActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AddAlbumActivity.this, new String[]{Manifest.permission.CAMERA}, 1);
            return;
        }
        //if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
        startActivityForResult(takePictureIntent, 99);
        //}
    }

    private void choosePhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 99 && resultCode == Activity.RESULT_OK) {
            // Xử lý ảnh đã chụp tại đây (nếu cần)
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            // Hiển thị ảnh hoặc thực hiện các xử lý khác theo nhu cầu của bạn
            imageView.setImageBitmap(imageBitmap);
        } else if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            try {
                Uri imageUri = data.getData();
                InputStream inputStream = getContentResolver().openInputStream(imageUri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imageView.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void addControls() {
        edtMa = findViewById(R.id.edt_id_albumadmin);
        edtTen = findViewById(R.id.edt_name_albumadmin);
        imageView = findViewById(R.id.img_addAblumAdmin);
        edtMaArtist = findViewById(R.id.edt_idArtist_albumadmin);
        btnSave = findViewById(R.id.btn_save_albumadmin);
        btncancel = findViewById(R.id.btn_cancel_albumadmin);
        btn_choose_image_addAblumAdmin = findViewById(R.id.btn_choose_image_addAblumAdmin);
        btn_camera = findViewById(R.id.btn_camera_albumadmin);
    }
}