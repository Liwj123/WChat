package com.example.lenovo.wchat.act;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.lenovo.wchat.R;
import com.example.lenovo.wchat.adapter.MessageAdapter;
import com.example.lenovo.wchat.fragment.FaceFragment;
import com.example.lenovo.wchat.fragment.ImageFragment;
import com.example.lenovo.wchat.manager.MessageManager;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lenovo on 2017/5/8.
 */

public class MessageActivity extends BaseActivity implements EMMessageListener, View.OnClickListener {

    private String name;
    private RecyclerView recycler_message;
    private EditText et_message;
    private Button send_Message;
    private List<EMMessage> messages;
    private MessageAdapter ma;
    private String txt;
    private String caogao;
    private LinearLayoutManager llm;
    private ImageView yuyin, img, biaoqing, add;
    private ImageFragment imgFragment;
    private FaceFragment faceFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        getDataFromIntent();
        initView();

        EMClient.getInstance().chatManager().addMessageListener(this);


        //获取次联系人会话的所有消息
        messages = getList();


        initAdapter();
        setTestStr();
    }

    private List<EMMessage> getList() {
        //获取会话对象
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(name);
        List<EMMessage> msg = conversation.getAllMessages();
        //标记所有消息为已读
        conversation.markAllMessagesAsRead();
        //如果会话消息个数为1 ，尝试从数据库中再获取19条
        if (msg.size() == 1) {
            conversation.loadMoreMsgFromDB(msg.get(0).getMsgId(), 19);
        }
        return conversation.getAllMessages();
    }

    private void setTestStr() {
        et_message.setText(caogao);
        et_message.setSelection(et_message.getText().length());
    }

    //设置recyclerView的适配
    private void initAdapter() {
        ma = new MessageAdapter(messages, this);
        llm = new LinearLayoutManager(this);
        //将最后一条消息显示出来
        llm.setStackFromEnd(true);
        recycler_message.setLayoutManager(llm);
        recycler_message.setAdapter(ma);

    }

    private void initView() {
        recycler_message = (RecyclerView) findViewById(R.id.message_recycler);
        et_message = (EditText) findViewById(R.id.message_et);
        send_Message = (Button) findViewById(R.id.message_send);
        yuyin = (ImageView) findViewById(R.id.message_yuyin);
        img = (ImageView) findViewById(R.id.message_img);
        biaoqing = (ImageView) findViewById(R.id.message_biaoqing);
        add = (ImageView) findViewById(R.id.message_add);
        imgFragment = new ImageFragment();
        faceFragment = new FaceFragment();


        yuyin.setOnClickListener(this);
        img.setOnClickListener(this);
        biaoqing.setOnClickListener(this);
        add.setOnClickListener(this);
        send_Message.setOnClickListener(this);

        recycler_message.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        hideKeyBoard();
                        break;
                }
                return false;
            }
        });

        //监听文本输入框文本变化监听
        et_message.addTextChangedListener(new TextWatcher() {

            //文本改变前
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            //文本改变后
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            //文本改变
            @Override
            public void afterTextChanged(Editable s) {
                txt = s.toString();
//                Toast.makeText(MessageActivity.this, txt, Toast.LENGTH_SHORT).show();
            }
        });

    }

    //加载menu的布局
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_message, menu);
        return true;
    }

    //设置标题栏的点击事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //系统的返回键的id
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.menu_item1:
                Toast.makeText(this, "菜单", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getDataFromIntent() {
        name = getIntent().getStringExtra("name");
        caogao = getIntent().getStringExtra("caogao");

        //修改标题的内容
        getSupportActionBar().setTitle(name);
        //给标题框加一个返回按钮
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    //点击返回键调用的方法
    @Override
    public void onBackPressed() {
        Intent data = new Intent();
        data.putExtra("user", name);
        data.putExtra("txt", txt);
        setResult(RESULT_OK, data);
        Log.v("map", txt + "-------------" + name);
        //清空该fragment的返回栈
        super.onBackPressed();
    }

    @Override
    public void onMessageReceived(final List<EMMessage> list) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                messages.addAll(list);
                ma.notifyDataSetChanged();
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

    //创建图片信息并发送
    public void createImage(String path) {
        EMMessage msg = EMMessage.createImageSendMessage(path, false, name);
        sendMessage(msg);
    }

    //创建文本信息并发送
    public void createTxt(String content) {
        EMMessage msg = EMMessage.createTxtSendMessage(content, name);
        sendMessage(msg);
        txt = "";
    }

    //发送消息
    private void sendMessage(EMMessage msg) {
        msg.setChatType(EMMessage.ChatType.Chat);
        EMClient.getInstance().chatManager().sendMessage(msg);
        messages.add(msg);
        ma.notifyDataSetChanged();
        recycler_message.scrollToPosition(messages.size() - 1);

        //实现在消息界面刷新消息列表
        MessageManager.getInstance().getiMessageList().refChatList();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.message_send:
                String message = et_message.getText().toString();
                if (message.equals("")) {

                } else {
                    createTxt(message);
                    et_message.setText("");
                }
                break;
            case R.id.message_yuyin:

                break;
            case R.id.message_img:
                openCloseFrame(R.id.message_img);
                //图片图标是将软键盘收起
                hideKeyBoard();
                break;
            case R.id.message_biaoqing:
                openCloseFrame(R.id.message_biaoqing);
                hideKeyBoard();
                break;
            case R.id.message_add:

                break;
        }
    }

    /**
     * 将软键盘隐藏
     * 调用系统服务将软键盘收起
     */
    private void hideKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(et_message.getWindowToken(), 0);
    }

    /**
     * 打开关闭 图片fragment
     */
    private void openCloseFrame(int res) {
        FragmentTransaction ft_img = null;
        FragmentTransaction ft_face = null;
        if (res == R.id.message_img) {
            FragmentManager fm = getSupportFragmentManager();
            if (imgFragment.isVisible()) {
                FragmentTransaction ft = fm.beginTransaction();
                ft.remove(imgFragment);
                ft.commit();
                fm.popBackStack();
            } else {
                if (faceFragment.isVisible()) {
                    fm.popBackStack();
                    if (ft_face != null) {
                        ft_face.remove(faceFragment);

                    }
                    FragmentTransaction fragmentTransaction = fm.beginTransaction();
                    fragmentTransaction.add(R.id.message_frame, imgFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                } else {
                    ft_img = fm.beginTransaction();
                    ft_img.add(R.id.message_frame, imgFragment);
                    ft_img.addToBackStack(null);
                    ft_img.commit();
                }
            }
        } else if (res == R.id.message_biaoqing) {
            FragmentManager fm = getSupportFragmentManager();
            if (faceFragment.isVisible()) {
                FragmentTransaction ft = fm.beginTransaction();
                ft.remove(faceFragment);
                ft.commit();
                fm.popBackStack();
            } else {
                if (imgFragment.isVisible()) {
                    fm.popBackStack();
                    if (ft_img != null) {
                        ft_img.remove(imgFragment);
                    }
                    FragmentTransaction fragmentTransaction = fm.beginTransaction();
                    fragmentTransaction.add(R.id.message_frame, faceFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                } else {
                    ft_face = fm.beginTransaction();
                    ft_face.add(R.id.message_frame, faceFragment);
                    ft_face.addToBackStack(null);
                    ft_face.commit();
                }
            }
        }
    }
}
