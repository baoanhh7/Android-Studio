package com.example.doan_music.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.doan_music.R;
import com.example.doan_music.adapter.MainAdapter;
import com.example.doan_music.data.DbHelper;
import com.example.doan_music.fragment.drawer.AllSongs_Fragment;
import com.example.doan_music.loginPackage.LoginActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

//public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
public class MainActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    BottomNavigationView bottom_navigation;
    NavigationView navigationView;
    ViewPager view_pager;

    // Tạo class MainAdapter đã làm trước đó
    MainAdapter adapter;
    Integer maU;
    DbHelper dbHelper = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addControls();

        // Drawer
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);

        toggle.syncState();

        // Tạo 1 adapter theo viewpager
        adapter = new MainAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        // Cung cấp dữ liệu cho viewpager bằng MainAdapter
        view_pager.setAdapter(adapter);

        // Quản lý hiển thị các trang (viewPager)
        // 1. Vuốt, set trạng thái vuốt để chuyển trang
        view_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        bottom_navigation.getMenu().findItem(R.id.menu_home).setChecked(true);
                        break;
                    case 1:
                        bottom_navigation.getMenu().findItem(R.id.menu_search).setChecked(true);
                        break;
                    case 2:
                        bottom_navigation.getMenu().findItem(R.id.menu_library).setChecked(true);
                        break;
                    case 3:
                        bottom_navigation.getMenu().findItem(R.id.menu_spotify).setChecked(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        // 2.Click
        bottom_navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.menu_home) {
                    view_pager.setCurrentItem(0);
                } else if (itemId == R.id.menu_search) {
                    view_pager.setCurrentItem(1);
                } else if (itemId == R.id.menu_library) {
                    view_pager.setCurrentItem(2);
                } else if (itemId == R.id.menu_spotify) {
                    view_pager.setCurrentItem(3);
                }
                return true;
            }
        });

        // Select item in Drawer
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.allSongs) replace(new AllSongs_Fragment());
                else if (id == R.id.logout) {
//                    replace(new Settings_Fragment());
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                } else if (id == R.id.home) {
                    Intent i = new Intent(MainActivity.this, MainActivity.class);
                    startActivity(i);

                    finish();
                }
                // Xử lý xong sẽ đóng Drawer
                drawerLayout.closeDrawer(GravityCompat.START);

                return true;
            }
        });
    }

    public Integer getMyVariable() {
        return maU;
    }

    private void addControls() {
        bottom_navigation = findViewById(R.id.bottomNavigationView);
        view_pager = findViewById(R.id.view_pager);

        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_drawer);
        // Lấy Intent đã được chuyển từ Login_userActivity
        Intent intent = getIntent();

        // Kiểm tra xem có dữ liệu "maU" được chuyển không
        if (intent.hasExtra("maU")) {
            // Lấy dữ liệu từ Intent
            maU = intent.getIntExtra("maU",0);
        }

    }

    // Nhấn nút back device để trở về(sử dụng nút trong device)
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else
            super.onBackPressed();
    }

    public void replace(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment).commit();
    }
}
