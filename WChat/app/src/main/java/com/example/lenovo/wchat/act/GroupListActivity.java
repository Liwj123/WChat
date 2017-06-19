package com.example.lenovo.wchat.act;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.lenovo.wchat.R;
import com.example.lenovo.wchat.adapter.GroupAdapter;
import com.example.lenovo.wchat.callback.IGroupItemClick;
import com.example.lenovo.wchat.callback.IGroupList;
import com.example.lenovo.wchat.manager.GroupManager;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lenovo on 2017/6/16.
 */

public class GroupListActivity extends BaseActivity implements IGroupList{

    private View groupList_ll;
    private RecyclerView group_recycler;
    private List<EMGroup> list = new ArrayList<>();
    private GroupAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grouplist);
        getSupportActionBar().setTitle("群聊");
        GroupManager.getInstance().setiGroupList(this);
        getGroupList();
        initView();

    }

    /**
     * 获取自己创建和所加入的群列表
     */
    private void getGroupList() {
        list= EMClient.getInstance().groupManager().getAllGroups();
    }

    private void initView() {
        groupList_ll = findViewById(R.id.group_list_ll);
        group_recycler = (RecyclerView) findViewById(R.id.group_list_recycler);

        groupList_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GroupListActivity.this, CreateGroupActivity.class);
                startActivity(intent);
            }
        });

        LinearLayoutManager llm = new LinearLayoutManager(this);
        adapter = new GroupAdapter(this,list);
        group_recycler.setLayoutManager(llm);
        group_recycler.setAdapter(adapter);

        adapter.setiGroupItemClick(new IGroupItemClick() {
            @Override
            public void groupItemClick(int position) {
                String groupId = list.get(position).getGroupId();
                String groupName = list.get(position).getGroupName();
                Intent intent = new Intent(GroupListActivity.this, GroupMessageActivity.class);
                intent.putExtra("groupId", groupId);
                intent.putExtra("groupName", groupName);
                startActivity(intent);
            }
        });

        //设置每一个item之间的分割线
        group_recycler.addItemDecoration(new RecyclerView.ItemDecoration() {
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

    }

    public void resetGroupList(){
        getGroupList();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void refGroupList() {
        resetGroupList();
    }
}
