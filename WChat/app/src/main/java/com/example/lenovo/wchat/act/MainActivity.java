package com.example.lenovo.wchat.act;

import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.lenovo.wchat.R;
import com.example.lenovo.wchat.fragment.BaseFragment;
import com.example.lenovo.wchat.fragment.ChatListFragment;
import com.example.lenovo.wchat.fragment.ContactFragment;
import com.hyphenate.chat.EMClient;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView tv;
    private ViewPager viewPager;
    private List<BaseFragment> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        tv = (TextView) findViewById(R.id.tv);
        viewPager = (ViewPager) findViewById(R.id.main_viewPager);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EMClient.getInstance().logout(true);
            }
        });

        initDate();

        FragmentPagerAdapter fpa = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return list.get(position);
            }

            @Override
            public int getCount() {
                return list.size();
            }
        };
        viewPager.setAdapter(fpa);
    }

    private void initDate() {
        ChatListFragment chat = new ChatListFragment();
        ContactFragment contact = new ContactFragment();
        list.add(chat);
        list.add(contact);
    }
}
