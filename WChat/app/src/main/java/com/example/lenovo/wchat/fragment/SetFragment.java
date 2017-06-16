package com.example.lenovo.wchat.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.lenovo.wchat.R;
import com.example.lenovo.wchat.act.MainActivity;
import com.example.lenovo.wchat.act.MessageActivity;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;

/**
 * Created by Lenovo on 2017/5/8.
 */

public class SetFragment extends BaseFragment implements View.OnClickListener {

    private EditText number, text;
    private Button send, logout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_set, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private void initView(View view) {
        number = (EditText) view.findViewById(R.id.set_number);
        text = (EditText) view.findViewById(R.id.set_text);
        send = (Button) view.findViewById(R.id.set_send);
        logout = (Button) view.findViewById(R.id.set_logout);

        send.setOnClickListener(this);
        logout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.set_logout:
                EMClient.getInstance().logout(true);
                ((MainActivity) getActivity()).finish();
                ((MainActivity) getActivity()).intentToLogin();
                break;
            case R.id.set_send:
                String num = number.getText().toString();
                String txt = text.getText().toString();
                if (TextUtils.isEmpty(txt) && TextUtils.isEmpty(num)) {

                } else {
                    EMMessage msg = EMMessage.createTxtSendMessage(txt, num);
                    msg.setChatType(EMMessage.ChatType.Chat);
                    EMClient.getInstance().chatManager().sendMessage(msg);
                    number.setText("");
                    text.setText("");

                }
                break;
        }
    }
}
