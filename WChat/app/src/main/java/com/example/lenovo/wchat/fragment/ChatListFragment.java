package com.example.lenovo.wchat.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lenovo.wchat.R;
import com.example.lenovo.wchat.Utils.SPUtil;
import com.example.lenovo.wchat.act.MainActivity;
import com.example.lenovo.wchat.act.MessageActivity;
import com.example.lenovo.wchat.callback.IMessageList;
import com.example.lenovo.wchat.callback.RecyclerViewClick;
import com.example.lenovo.wchat.adapter.RecyclerAdapter;
import com.example.lenovo.wchat.manager.MessageManager;
import com.example.lenovo.wchat.model.DeffStringBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Lenovo on 2017/4/27.
 */

public class ChatListFragment extends BaseFragment implements IMessageList {

    public static final int REQUEST_CODE = 101;
    private RecyclerView recyclerView;
    private ArrayList<EMConversation> list = new ArrayList<>();
    private RecyclerAdapter adapter;
    private Map<String, String> text;
    private SwipeRefreshLayout refresh;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chat, null);
    }

    @Override
    public void onAttach(Context context) {
        text = ((MainActivity) getActivity()).getDeffFromSp();
        super.onAttach(context);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        getChatList();
        initView(view);

        //使用消息管理者，调用消息列表的接口对象实现消息的刷新
        MessageManager.getInstance().setiMessageList(this);


        super.onViewCreated(view, savedInstanceState);
    }


    private void getChatList() {
        Map<String, EMConversation> map =
                EMClient.getInstance().chatManager().getAllConversations();
        Collection<EMConversation> values = map.values();
        Iterator<EMConversation> it = values.iterator();
        while (it.hasNext()) {
            list.add(it.next());
        }
    }

    private void initView(View view) {

        refresh = (SwipeRefreshLayout) view.findViewById(R.id.chat_refresh);
        recyclerView = (RecyclerView) view.findViewById(R.id.chat_recycler);
        //设置一个布局管理器,否则将不会显示recycler中的条目信息
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        adapter = new RecyclerAdapter(getActivity(), list);

        //设置每一个item之间的分割线
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                super.onDraw(c, parent, state);
                Paint paint = new Paint();
                paint.setColor(Color.BLUE);
                int left = parent.getPaddingLeft();
                int right = parent.getMeasuredWidth() - parent.getPaddingRight();
                int size = parent.getChildCount();
                for (int i = 0; i < size; i++) {
                    View child = parent.getChildAt(i);
                    int top = child.getBottom();
                    int bottom = top + 5;
                    c.drawRect(left, top, right, bottom, paint);
                }
            }

            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.set(0, 0, 0, 5);
            }
        });

        initClickListener(adapter);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(llm);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                MessageManager.getInstance().getiMessageList().refChatList();
                refresh.setRefreshing(false);
            }
        });
    }

    private void initClickListener(RecyclerAdapter adapter) {
        adapter.setClickListener(new RecyclerViewClick() {
            @Override
            public void onClickListener(int index) {
                //Toast.makeText(getActivity(), "点了" + index, Toast.LENGTH_SHORT).show();
                intentToMessage(list.get(index).getUserName());
            }

            @Override
            public boolean onItemLongClick(int index) {

                return false;
            }
        });
    }

    public void intentToMessage(String name) {
        Intent intent = new Intent(getActivity(), MessageActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("caogao", text.get(name));
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 判断返回的信息是否成功
        if (resultCode == RESULT_OK) {
            //判断请求码   requestCode
            switch (requestCode) {
                case REQUEST_CODE:
                    //得到消息界面的草稿信息
                    getDeff(data);
                    //将草稿存入sp文件中
                    saveDeff();
                    break;
            }
        }
    }

    private void getDeff(Intent data) {
        String key = data.getStringExtra("user");
        String value = data.getStringExtra("txt");

        Log.v("map---", key + "------" + value);
        if (TextUtils.isEmpty(value)) {
            text.remove(key);
        } else {
            text.put(key, value);
        }
        adapter.setTextStr(text);
    }

    private void saveDeff() {
        String json = new Gson().toJson(text);
        SPUtil.setChatDeff(getActivity(), json);
    }

    //重新刷新list集合中的数据
    public void resetList() {
        list.clear();
        getChatList();
        adapter.notifyDataSetChanged();
    }

    //实现消息接口的方法
    @Override
    public void refChatList() {
        resetList();
    }
}
