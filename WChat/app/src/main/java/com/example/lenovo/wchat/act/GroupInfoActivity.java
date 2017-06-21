package com.example.lenovo.wchat.act;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.lenovo.wchat.R;
import com.example.lenovo.wchat.adapter.GroupAdapter;
import com.example.lenovo.wchat.adapter.GroupInfoAdapter;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCursorResult;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.List;

public class GroupInfoActivity extends BaseActivity {

    private TextView id, name, owner, number;
    private EditText et;
    private Button add;
    private String groupId;
    private EMGroup group;
    private RecyclerView recycler;
    private List<String> memberList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_info);
        getId();
        initView();
        setGroupInfo();
    }

    private void setGroupInfo() {
        id.setText(groupId);
        name.setText(group.getGroupName());
        owner.setText(group.getOwner());
        number.setText(group.getMemberCount() + "");
    }

    private void getId() {
        groupId = getIntent().getStringExtra("groupId");
    }

    private void initView() {
        id = (TextView) findViewById(R.id.group_info_id);
        name = (TextView) findViewById(R.id.group_info_name);
        owner = (TextView) findViewById(R.id.group_info_owner);
        number = (TextView) findViewById(R.id.group_info_number);
        et = (EditText) findViewById(R.id.group_info_et);
        add = (Button) findViewById(R.id.group_info_add);
        recycler = (RecyclerView) findViewById(R.id.group_info_recycler);
        group = EMClient.getInstance().groupManager().getGroup(groupId);

        //获取群成员
        memberList = group.getMembers();

        GroupInfoAdapter adapter = new GroupInfoAdapter(memberList, this);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        recycler.setLayoutManager(llm);
        recycler.setAdapter(adapter);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String addId = et.getText().toString();

                if (!TextUtils.isEmpty(addId)) {
                    if (group.getOwner().equals(EMClient.getInstance().getCurrentUser())) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    EMClient.getInstance().groupManager().addUsersToGroup(groupId, new String[]{addId});
                                } catch (HyphenateException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    } else {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    EMClient.getInstance().groupManager().inviteUser(groupId, new String[]{addId}, null);//
                                } catch (HyphenateException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }
                }
            }
        });

    }
}
