package com.example.lenovo.wchat.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lenovo.wchat.R;
import com.example.lenovo.wchat.adapter.RecyclerAdapter;

import java.util.ArrayList;

/**
 * Created by Lenovo on 2017/4/27.
 */

public class ChatListFragment extends BaseFragment {

    private RecyclerView recyclerView;
    private ArrayList<String> list = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chat,null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }
    private void initView(View view){
        for (int i = 0;i < 50;i++){
            list.add("i="+i);
        }
        recyclerView = (RecyclerView) view.findViewById(R.id.chat_recycler);
        //设置一个布局管理器,否则将不会显示recycler中的条目信息
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        RecyclerAdapter adapter = new RecyclerAdapter(getActivity(),list);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(llm);
    }
}
