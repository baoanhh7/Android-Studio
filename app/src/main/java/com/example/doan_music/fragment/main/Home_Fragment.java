package com.example.doan_music.fragment.main;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.example.doan_music.R;
import com.example.doan_music.activity.MainActivity;
import com.example.doan_music.adapter.home.TabLayoutAdapter;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

public class Home_Fragment extends Fragment {

    TabLayout tabLayout;
    NavigationView navigationView;
    ViewPager home_viewpager;
    TabLayoutAdapter adapter;
    // Trong Fragment khi muốn ánh xạ thì khai báo qua đối tượng trung gian là View
    View mView;
    TextView txt_nameUser;

    public Home_Fragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        // Sau khi tạo đối tượng trung gian là là View thì gán vào dòng return, sau đó return đối tượng View để sử dụng
        mView = inflater.inflate(R.layout.fragment_home_, container, false);

        MainActivity mainActivity = (MainActivity) getActivity();
//        txt_nameUser.setText(mainActivity.getName());

        addControls();

        // Quản lý hiển thị các trang (Fragment) theo TabLayout
        tabLayout.setupWithViewPager(home_viewpager);

        // Đặt màu sắc cho các tab
        tabLayout.setTabTextColors(Color.WHITE, Color.parseColor("#FF4081")); // Màu sắc chuyển đổi ở đây
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#FF4081")); // Màu sắc cho dấu chỉ mục khi tab được chọn

        return mView;
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void addControls() {
        tabLayout = mView.findViewById(R.id.tab_layout);
        home_viewpager = mView.findViewById(R.id.home_viewpager);
        navigationView = mView.findViewById(R.id.navigation_drawer);

        txt_nameUser = (TextView) mView.findViewById(R.id.txt_nameUser);

        // Khác với khai báo Adapter trong Activity, trong Fragment khai báo Adapter theo kiểu sau
        adapter = new TabLayoutAdapter(getChildFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        home_viewpager.setAdapter(adapter);

    }
}