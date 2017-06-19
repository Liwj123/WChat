package com.example.lenovo.wchat.act;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.lenovo.wchat.R;
import com.example.lenovo.wchat.Utils.StringUtil;
import com.example.lenovo.wchat.adapter.MessageAdapter;
import com.example.lenovo.wchat.fragment.BaseFragment;
import com.example.lenovo.wchat.fragment.FaceFragment;
import com.example.lenovo.wchat.fragment.ImageFragment;
import com.example.lenovo.wchat.fragment.VoiceFregment;
import com.example.lenovo.wchat.manager.MessageManager;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.R.attr.bitmap;
import static com.hyphenate.chat.a.a.a.c;

/**
 * Created by Lenovo on 2017/5/8.
 */

public class MessageActivity extends BaseActivity implements EMMessageListener, View.OnClickListener {

    private String name;
    private RecyclerView recycler_message;
    public EditText et_message;
    private Button send_Message;
    private List<EMMessage> messages = new ArrayList<>();
    private MessageAdapter ma;
    private String txt;
    private String caogao;
    private LinearLayoutManager llm;
    private ImageView yuyin, img, biaoqing, video;
    private ImageFragment imgFragment;
    private FaceFragment faceFragment;
    private VoiceFregment voiceFregment;
    private BaseFragment currFragment;

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
        //根据用户名查询聊天消息
        EMConversation conversation = EMClient
                .getInstance()
                .chatManager()
                .getConversation(name);
        // 标记所有消息为已读
        conversation.markAllMessagesAsRead();

        // 从会话对象中获取所有消息
        ArrayList<EMMessage> msgList = (ArrayList<EMMessage>) conversation
                .getAllMessages();

        // 如果 会话中的消息个数为1  尝试从数据库中再获取19条
        // 要求 最终 消息列表中要有 20条消息
        if (msgList.size() == 1) {
            conversation.loadMoreMsgFromDB(msgList.get(0).getMsgId(), 19);
        }

        //获取此会话的所有消息
        return conversation
                .getAllMessages();
    }

    private void setTestStr() {
        if (!TextUtils.isEmpty(caogao)) {
            et_message.setText(StringUtil.getExpressionString(this, caogao));
            et_message.setSelection(et_message.getText().length());
        }
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
        video = (ImageView) findViewById(R.id.message_video);
        imgFragment = new ImageFragment();
        faceFragment = new FaceFragment();
        voiceFregment = new VoiceFregment();


        yuyin.setOnClickListener(this);
        img.setOnClickListener(this);
        biaoqing.setOnClickListener(this);
        video.setOnClickListener(this);
        send_Message.setOnClickListener(this);

        recycler_message.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        hideKeyBoard();
                        closeFragment();
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
        if (closeFragment()) return;
        setResult();
    }

    private boolean closeFragment() {
        if (currFragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .remove(currFragment)
                    .commit();
            currFragment = null;
            return true;
        }
        return false;
    }

    private void setResult() {
        Intent data = new Intent();
        data.putExtra("user", name);
        data.putExtra("txt", txt);
        setResult(RESULT_OK, data);
        Log.v("map", txt + "-------------" + name);
        super.onBackPressed();
    }

    @Override
    public void onMessageReceived(final List<EMMessage> list) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                EMMessage emMessage = list.get(0);
                if (emMessage.getFrom().equals(name)) {
                    messages.addAll(list);
                    ma.notifyDataSetChanged();
                }
                MessageManager.getInstance().getiMessageList().refChatList();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.message_send:
                String message = et_message.getText().toString();
                if (message.equals("")) {

                } else {
                    EMMessage msg = MessageManager.getInstance().createTxt(message, name,EMMessage.ChatType.Chat);
                    notifyMsg(msg);
                    et_message.setText("");
                }
                break;
            case R.id.message_yuyin:
                openCloseFrame(voiceFregment);
                hideKeyBoard();
                break;
            case R.id.message_img:
                openCloseFrame(imgFragment);
                //点击图片图标是将软键盘收起
                hideKeyBoard();
                break;
            case R.id.message_biaoqing:
                openCloseFrame(faceFragment);
                hideKeyBoard();
                break;
            case R.id.message_video:
                //调用系统的摄像功能
                Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                startActivityForResult(intent, 1010);
                hideKeyBoard();
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
    private void openCloseFrame(BaseFragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        if (fragment.isVisible()) {
            FragmentTransaction ft = fm.beginTransaction();
            ft.remove(fragment);
            ft.commit();
            currFragment = null;
        } else {
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.message_frame, fragment);
            ft.commit();
            currFragment = fragment;
        }
    }

    /**
     * 得到摄像后的数据
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1010:
                    EMMessage msg = MessageManager.getInstance().createVideo(this, data, name,EMMessage.ChatType.Chat);
                    notifyMsg(msg);
                    break;
            }
        }
    }

    private void notifyMsg(EMMessage msg) {
        txt = "";
        messages.add(msg);
        ma.notifyDataSetChanged();
        recycler_message.scrollToPosition(messages.size() - 1);
    }

    public void sendImg(String path) {
        EMMessage msg = MessageManager.getInstance().createImage(path, name, EMMessage.ChatType.Chat);
        notifyMsg(msg);
    }

    public void sendVoice(String path, int time) {
        EMMessage msg = MessageManager.getInstance().createVoice(path, time, name,EMMessage.ChatType.Chat);
        notifyMsg(msg);
    }

}
