package com.example.lenovo.wchat.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lenovo.wchat.R;
import com.example.lenovo.wchat.act.GroupListActivity;
import com.example.lenovo.wchat.adapter.ContactAdapter;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lenovo on 2017/4/27.
 */

public class ContactFragment extends BaseFragment {

    private View group;
    private RecyclerView recycler;
    private List<String> list = new ArrayList<>();
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            list = (List<String>) msg.obj;
            ContactAdapter adapter = new ContactAdapter(getActivity(), list);
            recycler.setAdapter(adapter);
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contact, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private void initView(View view) {
        group = view.findViewById(R.id.contact_group);
        recycler = (RecyclerView) view.findViewById(R.id.contact_recycler);

        getFriendList();

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recycler.setLayoutManager(llm);

        group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), GroupListActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getFriendList() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    list = EMClient.getInstance().contactManager().getAllContactsFromServer();
                    Message msg = handler.obtainMessage();
                    msg.obj = list;
                    msg.what = 1;
                    handler.sendMessage(msg);
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
