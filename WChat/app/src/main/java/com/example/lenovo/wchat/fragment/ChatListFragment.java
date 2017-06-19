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
import com.example.lenovo.wchat.act.GroupMessageActivity;
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
import com.hyphenate.chat.EMMessage;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static android.R.attr.type;
import static android.app.Activity.RESULT_OK;

/**
 * Created by Lenovo on 2017/4/27.
 */

public class ChatListFragment extends BaseFragment implements IMessageList {

    public static final int REQUEST_CODE = 101;
    public static final int REQUEST_CODE_GROUP = 102;
    private RecyclerView recyclerView;
    private ArrayList<EMConversation> list = new ArrayList<>();
    private RecyclerAdapter adapter;
    private Map<String, String> text = new HashMap<>();
    private SwipeRefreshLayout refresh;
    private Gson gson = new Gson();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chat, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        getChatList();
        initView(view);

        //使用消息管理者，调用消息列表的接口对象实现消息的刷新
        MessageManager.getInstance().setiMessageList(this);

        getDeffFromSP();

        super.onViewCreated(view, savedInstanceState);
    }

    private void getDeffFromSP() {
        // 从SP中取得草稿json
        String json = SPUtil.getChatDeff(getActivity());
        // json 转 数据
        Type types = new TypeToken<ArrayList<DeffStringBean>>() {
        }.getType();
        ArrayList<DeffStringBean> jsonArr = gson.fromJson(json, types);
        if (jsonArr == null)
            return;
        //把list的数据给map
        for (DeffStringBean d :
                jsonArr) {
            text.put(d.getKey(), d.getDeff());
        }
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
                paint.setColor(Color.GRAY);
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
                EMConversation.EMConversationType type = list.get(index).getType();
                if (type == EMConversation.EMConversationType.Chat) {
                    intentToMessage(list.get(index).getUserName());
                } else if (type == EMConversation.EMConversationType.GroupChat) {
                    String groupId = EMClient.getInstance().groupManager().getGroup(list.get(index).getLastMessage().getTo()).getGroupId();
                    String groupName = EMClient.getInstance().groupManager().getGroup(list.get(index).getLastMessage().getTo()).getGroupName();
                    intentToMessage(groupId, groupName);
                }
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

    public void intentToMessage(String groupId, String groupName) {
        Intent intent = new Intent(getActivity(), GroupMessageActivity.class);
        intent.putExtra("groupId", groupId);
        intent.putExtra("groupName", groupName);
        intent.putExtra("caogao", text.get(groupId));
        startActivityForResult(intent, REQUEST_CODE_GROUP);
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
                case REQUEST_CODE_GROUP:
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
        // 把 草稿 Map集合 转成 List集合
        List<DeffStringBean> deffs = new ArrayList<>();
        // 拿到 map集合中的  key集合
        Iterator<String> keys = text.keySet().iterator();
        // 遍历 key集合
        while (keys.hasNext()) {
            // 拿到每一次的key的值
            String keyC = keys.next();
            // 通过key的值 从map集合中取到相应的value
            // 并 设置给 实体类
            DeffStringBean deffStringBean = new DeffStringBean();
            deffStringBean.setDeff(text.get(keyC));
            deffStringBean.setKey(keyC);
            // 把 实体类添加到 list集合中
            deffs.add(deffStringBean);
        }
        // 把 实体类集合 转成json字符串
        String json = gson.toJson(deffs);
        // 把 json字符串 存到 SP中
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
