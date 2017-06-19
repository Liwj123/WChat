package com.example.lenovo.wchat.manager;

import com.example.lenovo.wchat.callback.IGroupList;

/**
 * Created by Lenovo on 2017/6/19.
 */

public class GroupManager {
    private IGroupList iGroupList;
    private static GroupManager groupManager;

    public static synchronized GroupManager getInstance(){
        if(groupManager == null){
            groupManager = new GroupManager();
        }
        return groupManager;
    }

    public IGroupList getiGroupList() {
        return iGroupList;
    }

    public void setiGroupList(IGroupList iGroupList) {
        this.iGroupList = iGroupList;
    }
}
