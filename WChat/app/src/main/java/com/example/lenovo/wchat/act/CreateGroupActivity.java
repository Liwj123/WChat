package com.example.lenovo.wchat.act;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lenovo.wchat.R;
import com.example.lenovo.wchat.manager.GroupManager;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMGroupManager;
import com.hyphenate.exceptions.HyphenateException;

public class CreateGroupActivity extends AppCompatActivity {

    private EditText name,intro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        getSupportActionBar().setTitle("新建群组");
        initView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_group_create,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.menu_group_create:
                String groupName = name.getText().toString();
                String groupIntro = intro.getText().toString();
                EMGroupManager.EMGroupOptions option = new EMGroupManager.EMGroupOptions();
                option.maxUsers = 200;
                option.style = EMGroupManager.EMGroupStyle.EMGroupStylePrivateMemberCanInvite;
                try {
                    EMGroup group = EMClient.getInstance().groupManager().createGroup(groupName, groupIntro, new String[]{}, "", option);
                    if(group != null){
                        Toast.makeText(this, "创建成功", Toast.LENGTH_SHORT).show();
                        GroupManager.getInstance().getiGroupList().refGroupList();
                        finish();
                    }
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "创建失败", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        name = (EditText) findViewById(R.id.group_create_name);
        intro = (EditText) findViewById(R.id.group_create_intro);
    }
}
