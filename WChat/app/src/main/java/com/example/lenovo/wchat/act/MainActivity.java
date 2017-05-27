package com.example.lenovo.wchat.act;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.example.lenovo.wchat.R;
import com.example.lenovo.wchat.Utils.SPUtil;
import com.example.lenovo.wchat.adapter.RecyclerAdapter;
import com.example.lenovo.wchat.fragment.BaseFragment;
import com.example.lenovo.wchat.fragment.ChatListFragment;
import com.example.lenovo.wchat.fragment.ContactFragment;
import com.example.lenovo.wchat.fragment.SetFragment;
import com.example.lenovo.wchat.model.DeffStringBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements EMMessageListener, ViewPager.OnPageChangeListener {

    private ViewPager viewPager;
    private List<BaseFragment> list = new ArrayList<>();
    private ChatListFragment chat;
    private TabLayout tabLayout;
    private String[] title = {"消息", "联系人", "其他"};
    private static  Map<String,String> text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //设置标题的文本
        getSupportActionBar().setTitle("微聊");

        //添加消息监听
        EMClient.getInstance().chatManager().addMessageListener(this);

        //加载视图的方法
        initView();

        //将sp文件中的json字符串取出赋值给map集合
        getDeffFromSp();

    }

    public Map<String,String> getDeffFromSp() {
        //调用sp工具类中的方法得到sp中的文本
        String json = SPUtil.getChatDeff(MainActivity.this);
        //实例化gson对象用来解析json字符串
        Gson gson = new Gson();
        //创建type对象用来接收字符串中的文本信息
        Type type = new TypeToken<Map<String,String>>() {
        }.getType();
        //解析
        text = gson.fromJson(json, type);
        Log.e("json",text.toString());
        return text;

    }

    private void initView() {
        viewPager = (ViewPager) findViewById(R.id.main_viewPager);

        //标签控件
        tabLayout = (TabLayout) findViewById(R.id.main_tab);
//        tv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                EMClient.getInstance().logout(true);
//            }
//        });

        initFragment();

        //fragment的适配
        FragmentPagerAdapter fpa = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return list.get(position);
            }

            @Override
            public int getCount() {
                return list.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return title[position];
            }
        };

        //添加页面改变监听
        viewPager.addOnPageChangeListener(this);

        //给fragment设置适配
        viewPager.setAdapter(fpa);

        //将选择框和viewPager关联
        tabLayout.setupWithViewPager(viewPager);

        //设置选择框发生变化时的监听事件
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            //当标签被选择时
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                setDown(tab);
            }

            //当标签没有被选择时
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            //当标签重新选择时
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        //设置预加载缓存个数
        viewPager.setOffscreenPageLimit(3);
        setDown(tabLayout.getTabAt(0));
    }

    //将fragment的页面添加入集合中
    private void initFragment() {
        chat = new ChatListFragment();
        ContactFragment contact = new ContactFragment();
        SetFragment set = new SetFragment();

        list.add(chat);
        list.add(contact);
        list.add(set);
    }

    private void setDown(TabLayout.Tab index) {
        tabLayout.getTabAt(0).setIcon(R.mipmap.skin_tab_icon_conversation_normal);
        tabLayout.getTabAt(1).setIcon(R.mipmap.skin_tab_icon_contact_normal);
        tabLayout.getTabAt(2).setIcon(R.mipmap.skin_tab_icon_plugin_normal);
        if (index == tabLayout.getTabAt(0)) {
            index.setIcon(R.mipmap.skin_tab_icon_conversation_selected);
        } else if (index == tabLayout.getTabAt(1)) {
            index.setIcon(R.mipmap.skin_tab_icon_contact_selected);
        } else if (index == tabLayout.getTabAt(2)) {
            index.setIcon(R.mipmap.skin_tab_icon_plugin_selected);
        }
    }


    //添加消息监听
    /**
     * 当收到消息时
     * @param list
     */
    @Override
    public void onMessageReceived(List<EMMessage> list) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                chat.resetList();
            }
        });
    }

    @Override
    public void onCmdMessageReceived(List<EMMessage> list) {

    }

    @Override
    public void onMessageReadAckReceived(List<EMMessage> list) {

    }

    @Override
    public void onMessageDeliveryAckReceived(List<EMMessage> list) {

    }

    @Override
    public void onMessageChanged(EMMessage emMessage, Object o) {

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EMClient.getInstance().chatManager().removeMessageListener(this);
    }


    /**
     * 添加页面改变监听
     */
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_main_item:

                break;
            case R.id.menu_main_item1:

                break;
        }


        return super.onOptionsItemSelected(item);
    }
}
