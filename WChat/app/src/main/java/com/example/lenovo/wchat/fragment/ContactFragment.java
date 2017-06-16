package com.example.lenovo.wchat.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lenovo.wchat.R;
import com.example.lenovo.wchat.adapter.ContactAdapter;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.util.List;

/**
 * Created by Lenovo on 2017/4/27.
 */

public class ContactFragment extends BaseFragment {

    private RecyclerView recycler;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contact,null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        try {
            List<String> list = EMClient.getInstance().contactManager().getAllContactsFromServer();
            ContactAdapter adapter = new ContactAdapter(getActivity(),list);
            LinearLayoutManager llm = new LinearLayoutManager(getActivity());
            recycler.setLayoutManager(llm);
            recycler.setAdapter(adapter);
        } catch (HyphenateException e) {
            e.printStackTrace();
        }
    }

    private void initView(View view) {
        recycler  = (RecyclerView) view.findViewById(R.id.contact_recycler);
    }
}
